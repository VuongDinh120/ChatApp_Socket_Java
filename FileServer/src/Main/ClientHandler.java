/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Model.FileInfo;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author VuongDinh
 */
public class ClientHandler implements Runnable {

    private DatagramSocket ServerSocket;
    private String filePath;
    private int ClientPort;
    private InetAddress ClientAddress;
    private final int PIECES_OF_FILE_SIZE = 1024 * 32;

    public ClientHandler(DatagramSocket ServerSocket, String filePath, int ClientPort, InetAddress ClientAddress) {
        this.ServerSocket = ServerSocket;
        this.filePath = filePath;
        this.ClientPort = ClientPort;
        this.ClientAddress = ClientAddress;
    }

    public FileInfo getFileInfo(String filePath) {
        // get file size
        File fileSend = new File(filePath);
        long fileLength = fileSend.length();
        int piecesOfFile = (int) (fileLength / PIECES_OF_FILE_SIZE);
        int lastByteLength = (int) (fileLength % PIECES_OF_FILE_SIZE);
        // check last bytes of file
        if (lastByteLength > 0) {
            piecesOfFile++;
        }

        // read file info
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFilename(fileSend.getName());
        fileInfo.setFileSize(fileSend.length());
        fileInfo.setPiecesOfFile(piecesOfFile);
        fileInfo.setLastByteLength(lastByteLength);
        return fileInfo;
    }

    public void waitMillisecond(long millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            DatagramPacket sendPacket;

            FileInfo fileInfo = getFileInfo(filePath);
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));

            // send file info
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(fileInfo);
            sendPacket = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length,
                    ClientAddress, ClientPort);
            ServerSocket.send(sendPacket);

            oos.close();
            baos.close();
            // send file content
            System.out.println("Sending file...");
            // send pieces of file
            for (int i = 0, count = 0; i < fileInfo.getPiecesOfFile(); i++, count += PIECES_OF_FILE_SIZE) {
                System.out.println("Sending Packet " + i);
                int buff;
                if (i == fileInfo.getPiecesOfFile() - 1) {
                    buff = fileInfo.getLastByteLength();
                } else {
                    buff = PIECES_OF_FILE_SIZE;
                }
                byte[] fileBytess = new byte[buff];
                System.arraycopy(bytes, count, fileBytess, 0, fileBytess.length);
                sendPacket = new DatagramPacket(fileBytess, buff,
                        ClientAddress, ClientPort);
                ServerSocket.send(sendPacket);
                waitMillisecond(40);
            }

            System.out.println("File has sent!!");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
