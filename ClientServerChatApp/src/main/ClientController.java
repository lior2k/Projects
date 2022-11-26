package src.chatapp.main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

public class ClientController {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private boolean runThread = true;

    @FXML
    private TextArea PublicMessageBox;
    @FXML
    private TextArea PrivateMessageBox;
    @FXML
    private TextArea OnlineUsersBox;
    @FXML
    private TextArea FileListBox;

    @FXML
    private TextField LoginTextField;
    @FXML
    private TextField SendMessageBox;
    @FXML
    private TextField SendToBox;
    @FXML
    private TextField FileNameBox;

    @FXML
    private Button SendMessageButton;
    @FXML
    private Button LoginButton;
    @FXML
    private Button UploadButton;
    @FXML
    private Button DownloadButton;


    public ClientController(String server_address) throws IOException {
        socket = new Socket(server_address, 5000);
        in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        InputThread inputThread = new InputThread();
        Thread thread = new Thread(inputThread);
        thread.start();
    }

    @FXML
    private void onLoginButtonClicked() {
        if (!LoginTextField.getText().isEmpty()) {
            out.println(LoginTextField.getText());
            LoginTextField.setText("Username: " + LoginTextField.getText());
            LoginTextField.setEditable(false);
            LoginButton.setDisable(true);
            SendMessageButton.setDisable(false);
            UploadButton.setDisable(false);
            DownloadButton.setDisable(false);
        }
    }

    @FXML
    private void onSendMessageButtonClicked() {
        String outMessage = SendMessageBox.getText();
        String sendTo = SendToBox.getText();
        if (sendTo.isEmpty()) {
            if (!outMessage.isEmpty()) {
                out.println(outMessage);
            }
        } else {
            out.println("<>" + sendTo + "<>" + outMessage);
        }
    }

    @FXML
    private void onUploadButtonClicked() {

    }

    @FXML
    private void onDownloadButtonClicked() {
        String fileName = FileNameBox.getText();
        if (!fileName.isEmpty()) {
            FileNameBox.setText(null);
            out.println("><fileRequest><"+fileName);
        }
    }

    @FXML
    private void onLogoutButtonClicked() {
        try {
            out.println("!quit");
            runThread = false;
            socket.close();
            System.exit(0);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public class InputThread implements Runnable {

        @Override
        public void run() {
            String inMessage;
            try {
                while (runThread) {
                    inMessage = in.readLine();
                    if (inMessage != null) {
                        String time = LocalTime.now().truncatedTo(ChronoUnit.SECONDS).toString();
                        if (inMessage.contains("!update_online!,")) {
                            OnlineUsersBox.setText("");
                            String[] names = inMessage.split(",");
                            for (int i = 1; i < names.length; i++) {
                                OnlineUsersBox.appendText(names[i] + "\n");
                            }
                        } else if (inMessage.contains("!update_files!")) {
                            FileListBox.setText("File List:\n");
                            String[] files = inMessage.split(",");
                            for (int i = 1; i < files.length; i++) {
                                FileListBox.appendText(files[i] + "\n");
                            }
                        } else if (inMessage.startsWith("!file_data!")) {
                            System.out.println("received !file_data! - starting download");
                            DownloadThread dlt = new DownloadThread(inMessage);
                            dlt.start();
                        } else if (inMessage.contains("<>private<>")) {
                            String[] messageSplit = inMessage.split("<>");
                            String sentFrom = messageSplit[2];
                            String msg = messageSplit[3];
                            PrivateMessageBox.appendText("[" + time + "] " + sentFrom + ": " + msg + "\n");
                        } else {
                            PublicMessageBox.appendText("[" + time + "] " + inMessage + '\n');
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class DownloadThread extends Thread {

        String fileName;
        int numOfPackets;
        DatagramSocket udpSocket;
        InetAddress ip;
        HashMap<Integer, byte[]> fileData;

        public DownloadThread(String metaData) {
            String[] data = metaData.split(",");
            this.fileName = data[1];
            this.numOfPackets = Integer.parseInt(data[2]);
            this.initFileMap();
            try {
                int listenPort = 2002;
                udpSocket = new DatagramSocket(listenPort);
                udpSocket.setReuseAddress(true);
                int sockTimeOut = 2500;
                udpSocket.setSoTimeout(sockTimeOut);
                ip = InetAddress.getLocalHost();
            } catch (SocketException | UnknownHostException e) {
                e.printStackTrace();
            }
        }

        private void initFileMap() {
            this.fileData = new HashMap<>();
            for (int i = 1; i <= numOfPackets; i++) {
                fileData.put(i, null);
            }
        }

        private String checkSum() {
            StringBuilder str = new StringBuilder();
            for (Integer i : fileData.keySet()) {
                if (fileData.get(i) == null) {
                    str.append(i).append(",");
                }
            }
            return str.toString();
        }

        private void saveFileFromMap() {
            String[] tmp = fileName.split("\\.");
            try {
                FileOutputStream newFile = new FileOutputStream(tmp[0] + "DL." + tmp[1]);
                for (int i = 1; i <= numOfPackets; i++) {
                    newFile.write(fileData.get(i));
                }
                newFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String missing_packets = checkSum();
//                    System.out.println("debug: missing packets: " + missing_packets);
                    int sendPort = 2000;
                    if (missing_packets.equals("")) {
//                        System.out.println("debug: received all packets, done.");
                        byte[] bufferSend = "done".getBytes();
                        udpSocket.send(new DatagramPacket(bufferSend, bufferSend.length, ip, sendPort));
                        break;
                    }

                    byte[] packets_num = missing_packets.getBytes();
                    udpSocket.send(new DatagramPacket(packets_num, packets_num.length, ip, sendPort));

                    int missing_packets_num = missing_packets.split(",").length - 1;
                    for (int i = 0; i <= missing_packets_num; i++) {
                        int bufSize = 2048;
                        byte[] bufferRec = new byte[bufSize];
                        udpSocket.receive(new DatagramPacket(bufferRec, bufferRec.length));
                        int seq_num = Integer.parseInt("" + (char)bufferRec[0] + (char)bufferRec[1]);
                        if (fileData.get(seq_num) == null) {
//                            System.out.println("received packet num: " + seq_num);
                            byte[] packetData = new byte[bufSize - 2];
                            System.arraycopy(bufferRec, 2, packetData, 0, bufSize - 2);
                            fileData.put(seq_num, packetData);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Timed out, rerunning...");
                this.run();
            }
            saveFileFromMap();
            udpSocket.close();
        }
    }

}
