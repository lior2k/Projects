package src.chatapp.main;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private final ServerSocket server_socket;
    private final Vector<HandleClient> clients;
    private final ExecutorService pool;

    private final ArrayList<String> fileNames;

    public Server() throws IOException {
        server_socket = new ServerSocket(5000);
        clients = new Vector<>();
        pool = Executors.newCachedThreadPool();
        fileNames = new ArrayList<>();
        initFiles();
        System.out.println("----- Server is running! -----");
    }

    private void initFiles() {
        File folder = new File("resources/files");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                fileNames.add(file.getName());
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket client = server_socket.accept();
                HandleClient hc = new HandleClient(client);
                clients.add(hc);
                pool.execute(hc);
            }
        } catch(IOException E) {
            E.printStackTrace();
        }
    }

    public void broadcast(String message) {
        for(HandleClient client : clients) {
            if (client != null) {
                client.sendMessage(message);
            }
        }
    }

    public HandleClient getClient(String name) {
        HandleClient ret = null;
        for(HandleClient client : clients) {
            if (client.user_name.equals(name)) {
                ret = client;
                break;
            }
        }
        return ret;
    }

    public class HandleClient implements Runnable {
        private final BufferedReader in;
        private final PrintWriter out;
        private String user_name;

        public HandleClient(Socket client) throws IOException {
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        }

        private void updateOnline() {
            StringBuilder st = new StringBuilder("!update_online!");
            for (HandleClient client : clients) {
                st.append(",").append(client.user_name);
            }
            System.out.println("debug: sending online users: " + st);
            broadcast(st.toString());
        }

        private void updateFileList() {
            StringBuilder st = new StringBuilder("!update_files!");
            for (String file_name : fileNames) {
                st.append(",").append(file_name);
            }
            System.out.println("debug: sending file list: " + st);
            broadcast(st.toString());
        }

        private void sendMessage(String message) {
            out.println(message);
        }

        private HashMap<Integer, byte[]> readFile(String fileName) throws IOException {
            byte[] bytes;
            HashMap<Integer, byte[]> fileData = new HashMap<>();
            FileInputStream fileStream = new FileInputStream("resources\\files\\" + fileName);
            int bufSize = 2048;
            byte[] dataBuffer = new byte[bufSize - 2];
            int i = 1;
            String sequence_num;
            while (fileStream.read(dataBuffer) != -1) {
                sequence_num = (i < 10) ? "0" + i : "" + i;
                bytes = new byte[bufSize];
                bytes[0] = sequence_num.getBytes()[0];
                bytes[1] = sequence_num.getBytes()[1];
                System.arraycopy(dataBuffer, 0, bytes, 2, bufSize-2);
                fileData.put(i, bytes);
                i++;
            }
            fileStream.close();
            return fileData;
        }

        @Override
        public void run() {
            try {
                String client_username = in.readLine();
                this.user_name = client_username;
                System.out.println(client_username + " connected!");
                broadcast(client_username + " joined the chat!");
                updateOnline();
                updateFileList();
                String message;
                String[] messageSplit;
                HashMap<Integer, byte[]> fileData;
                while((message = in.readLine()) != null) {
                    System.out.println("debug: received " + message);
                    if (message.startsWith("!quit")) {
                        broadcast(client_username + " left the chat!");
                        clients.remove(this);
                        updateOnline();
                        break;
                    } else if (message.startsWith("<>")) {
                        messageSplit = message.split("<>");
                        String client_name = messageSplit[1];
                        String msg = messageSplit[2];
                        HandleClient target = getClient(client_name);
                        if (target != null) {
                            target.out.println("<>private<>" + this.user_name + "<>" + msg);
                        }
                    } else if (message.startsWith("><fileRequest><")) {
                        messageSplit = message.split("><");
                        String fileName = messageSplit[2]; // including suffix (type) eg .txt
                        if (!fileNames.contains(fileName)) {
                            System.out.println("File - '" + fileName + "' - not found, download process stopped");
                        } else {
                            // get and send file metadata;
                            fileData = readFile(fileName);
                            String metaData = "!file_data!," + fileName + "," + fileData.size(); // fileData = num of packets.
                            out.println(metaData);
                            //send file in a new thread over udp connection
                            SendFileThread sft = new SendFileThread(fileData);
                            sft.start();
                        }
                    } else {
                        broadcast(client_username + ": " + message);
                    }
                }
            } catch (IOException E) {
                System.out.println("exception during handle client run method");
                E.printStackTrace();
            }
        }
    }

    public static class SendFileThread extends Thread {
        private final HashMap<Integer, byte[]> fileData;
        private DatagramSocket udpSocket;
        private InetAddress ip;
        int listenPort = 2000;
        int sendPort = 2002;
        int sockTimeOut = 2500;
        int bufSize = 2048;

        public SendFileThread(HashMap<Integer, byte[]> fileData) {
            this.fileData = fileData;
            try {
                udpSocket = new DatagramSocket(listenPort);
                udpSocket.setReuseAddress(true);
                udpSocket.setSoTimeout(sockTimeOut);
                ip = InetAddress.getLocalHost();
            } catch (SocketException | UnknownHostException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            byte[] bufferRec;
            try {
                while (true) {
                    bufferRec = new byte[bufSize /2];
                    udpSocket.receive(new DatagramPacket(bufferRec, bufferRec.length));
                    String client_answer = data(bufferRec).toString();
                    if (client_answer.equals("done")) {
                        System.out.println("received done");
                        break;
                    }

                    String[] missing_packets = client_answer.split(",");
                    System.out.println("Client requested the following: " + client_answer);
                    System.out.print("Sending: ");
                    for (String packet_num : missing_packets) {
                        System.out.print(Integer.parseInt(packet_num) + ", ");
                        udpSocket.send(new DatagramPacket(fileData.get(Integer.parseInt(packet_num)), bufSize, ip, sendPort));
                    }
                    System.out.println();
                }
            } catch (IOException e) {
                System.out.println("Timed out, rerunning...");
                this.run();
            }
            udpSocket.close();
        }

        public static StringBuilder data(byte[] a) {
            if (a == null)
                return null;
            StringBuilder ret = new StringBuilder();
            int i = 0;
            while (i < a.length && a[i] != 0)
                ret.append((char) a[i++]);
            return ret;
        }
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.run();
        } catch (IOException E) {
            System.out.println("exception during server constructor");
            E.printStackTrace();
            System.exit(0);
        }
    }

}