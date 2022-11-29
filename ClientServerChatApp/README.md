# Server Client Chat Application

This project revolves around implementing an online chat including reliable file transfer over udp.

#### Selective Repeat Protocol - SRP

By requesting a certain file the server transmits the relevant data for the download proccess, within that data the client finds the file size, he then knows to expect filesize/2048 amount of **different** packets heading his way. Each packet has that Sequence Number header, that way the client reveals which packets he gets and which packets are still missing. Using SRP protocol the client and server are communicating back and forth accordingly until the client receives every packet sequence starting with 01 up to the amount of total packets.

![segments drawio](https://user-images.githubusercontent.com/92747945/156886555-f774e33b-b04b-4066-beed-aaa5ab3c54e7.png)

## Reliable UDP

* We begin with the client sending the server the 
    file-request command.
* The server then sends the relevant data for the transfer process (aka File size, Available port, etc)
* Based on the data received from the server, the client then knows how many segments of data to look forward to.
* The server breaks the File's data into data segments.
    each segment consists of 2 bytes that represents the segment's sequence number (e.g 00,01,02,...,99) and another 2046 bytes of the file's data.
* According to the number of required segments, the client then sends which missing packets are needed to completely download the file.
* To which the server responds by sending back all of the missing segments that were sent by the client's request.
* This method prevents packet lose because the back and forth between the client and the server continues until the client recevied every segment.
* The two above stages works in a loop up until the client received all the data needed, when that happens the client sends 'check' message to the server to finish the download process.

## Diagrams

### Messages Diagram:
![Messages](https://i.imgur.com/uHethui.jpeg)
### Download Diagram:
![Download process](https://i.imgur.com/dsZ4dhl.jpg)

## Features

- Sending messages to all existing members on the server or to one or more specific members.
- Downloading existing files from the server.
- Upload new files to the server for other users to download.

## How to run
To run this project you have to have javafx libraries on your computer, if you dont have jfx you can download it from https://gluonhq.com/products/javafx/.
After downloading jfx you will have to compile ClientApplication.java, ClientController.java together with jfx libaries. See example below:
```
javac --module-path C:\Users\lior2\javafx-sdk-18\lib --add-modules javafx.controls,javafx.fxml ClientController.java ClientApplication.java
```
Run the server from your IDE or compile using 'Javac Server.java' then run 'Java Server'.
Run the client using Java command. See below:
```
java --module-path %JAVA_FX% --add-modules javafx.controls,javafx.fxml ClientApplication
```

## Author

- [@lior2k](https://www.github.com/lior2k)
