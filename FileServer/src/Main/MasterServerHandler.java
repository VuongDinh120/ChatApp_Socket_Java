/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HostnameVerifier;

/**
 *
 * @author VuongDinh
 */
public class MasterServerHandler {

    private int FileServerPort;
    private String FileServerHost;

    public MasterServerHandler() {
    }

    public MasterServerHandler(String FileServerHost, int FileServerPort) {
        this.FileServerPort = FileServerPort;
        this.FileServerHost = FileServerHost;
    }

    public String getFileServerHost() {
        return FileServerHost;
    }

    public int getFileServerPort() {
        return FileServerPort;
    }

    public void setFileServerHost(String FileServerHost) {
        this.FileServerHost = FileServerHost;
    }

    public void setFileServerPort(int FileServerPort) {
        this.FileServerPort = FileServerPort;
    }

    public void createConnection(List<File> files) throws IOException {

        //Tạo socket cho client kết nối đến server qua ID address và port number
        Socket clientSocket = new Socket(FileServerHost, FileServerPort);

        // get the output stream from the socket.
        OutputStream outputStream = clientSocket.getOutputStream();
        // create an object output stream from the output stream so we can send an object through it
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        //Tạo inputStream nối với Socket
        BufferedReader inFromServer
                = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        //Gửi chuỗi ký tự tới Server thông qua outputStream đã nối với Socket (ở trên)
        System.out.println("Sending messages to the ServerSocket");
        objectOutputStream.writeObject(files);

        System.out.println(">> FROM SERVER: " + inFromServer.readLine());
        //Đóng liên kết socket
        clientSocket.close();
    }

}
