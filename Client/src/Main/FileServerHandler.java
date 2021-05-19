/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Model.DownloadFileInfo;
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

/**
 *
 * @author VuongDinh
 */
public class FileServerHandler implements Runnable {

    private String filename;
    private String destPath;
    private InetAddress ip;
    private int port;
    private final int PIECES_OF_FILE_SIZE = 1024 * 32;

    public FileServerHandler(String filename, String destPath, InetAddress ip, int port) {
        this.filename = filename;
        this.destPath = destPath;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket();

            DatagramPacket dp = new DatagramPacket(filename.getBytes(), filename.length(), ip, port);
            System.out.println("Sending file name ...");
            ds.send(dp);

            byte[] receiveData = new byte[PIECES_OF_FILE_SIZE];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            ds.receive(receivePacket);
            ByteArrayInputStream bais = new ByteArrayInputStream(receivePacket.getData());
            ObjectInputStream ois = new ObjectInputStream(bais);
            DownloadFileInfo fileInfo = (DownloadFileInfo) ois.readObject();
            ois.close();
            bais.close();
            // show file info
            if (fileInfo != null) {
                System.out.println("File name: " + fileInfo.getFilename());
                System.out.println("File size: " + fileInfo.getFileSize());
                System.out.println("Pieces of file: " + fileInfo.getPiecesOfFile());
                System.out.println("Last bytes length: " + fileInfo.getLastByteLength());
            }

            // get file content
            System.out.println("Receiving file...");
            File fileReceive = new File(destPath + fileInfo.getFilename());
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(fileReceive));
            // write pieces of file
            for (int i = 0; i < (fileInfo.getPiecesOfFile() - 1); i++) {
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                ds.receive(receivePacket);
                System.out.println("Receive packet " + i);
                bos.write(receiveData, 0, PIECES_OF_FILE_SIZE);
            }
            // write last bytes of file
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            ds.receive(receivePacket);
            bos.write(receiveData, 0, fileInfo.getLastByteLength());
            bos.flush();
            System.out.println("Done!");
            System.out.println("Complete receive.");

            // close stream
            bos.close();
            ds.close();
        } catch (SocketException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            if (!ds.isClosed()) {
                ds.close();
            }
        }
    }
}
