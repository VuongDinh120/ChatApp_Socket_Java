/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileserver;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VuongDinh
 */
public class UDP_File_Client {

    private static final int PIECES_OF_FILE_SIZE = 1024 * 32;
    private DatagramSocket serverSocket;
    private int listenPort;

    public UDP_File_Client(DatagramSocket serverSocket, int port) {
        this.serverSocket = serverSocket;
        this.listenPort = port;
    }

    public static int getPIECES_OF_FILE_SIZE() {
        return PIECES_OF_FILE_SIZE;
    }

    public int getPort() {
        return listenPort;
    }

    public DatagramSocket getServerSocket() {
        return serverSocket;
    }

    public void setPort(int port) {
        this.listenPort = port;
    }

    public void setServerSocket(DatagramSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void openServer() {
        try {
            serverSocket = new DatagramSocket(listenPort);
            System.out.println("Server is opened on port " + listenPort);
            byte[] incomingData = new byte[PIECES_OF_FILE_SIZE];
            byte[] replyData = new byte[PIECES_OF_FILE_SIZE];

            while (true) {
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                serverSocket.receive(incomingPacket);
                System.out.println("Packet Received!");

                byte[] data = incomingPacket.getData();

                ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(data));

                try {
                    FileInfo fileInfo = (FileInfo) iStream.readObject();
                    System.out.println("Student object received = " + fileInfo);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                System.out.println("ip Address!" + incomingPacket.getAddress());

                System.out.println("port!" + incomingPacket.getPort());

                System.out.println("message Received!" + new String(incomingPacket.getData()));

                InetAddress IPAddress = incomingPacket.getAddress();

                int port = incomingPacket.getPort();

                //lấy dữ liệu nhận và gửi dữ liệu lại cho client
                String reply = "Thank you for the message";
                replyData = reply.getBytes();
                //gửi dữ liệu lại cho client
                DatagramPacket replyPacket
                        = new DatagramPacket(replyData, replyData.length, IPAddress, port);
                serverSocket.send(replyPacket);

                //Chờ phản hồi
                Thread.sleep(2000);
                System.exit(0);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
