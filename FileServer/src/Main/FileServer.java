/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author VuongDinh
 */
public class FileServer {

    private static final String FileServerHost = "127.0.0.1";
    private static final int FileServerPort = 6000;
    public static final int NUM_OF_THREAD = 10;
    public static final int SERVER_PORT = 3000;
    public static final String sourcePath = "D:\\download\\";

    public static void main(String[] args) throws IOException {
        List<File> files = getListFile("D:\\Music\\Chrono Cross Original Soundtrack Revival Disc [FLAC]");
        MasterServerHandler sendInfoToMaster = new MasterServerHandler(FileServerHost, FileServerPort);
        sendInfoToMaster.createConnection(files);
        
        
        ExecutorService executor = Executors.newFixedThreadPool(NUM_OF_THREAD);
        DatagramSocket ServerSocket = null;
        try {
            ServerSocket = new DatagramSocket(SERVER_PORT);
            while (true) {
                byte[] filename_buf = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(filename_buf, 1024);
                ServerSocket.receive(receivePacket);
                String filename = new String(receivePacket.getData(), 0, receivePacket.getLength());
                int ClientPort = receivePacket.getPort();
                InetAddress ClientAddress = receivePacket.getAddress();
                System.out.println(sourcePath + filename);

                String filePath = sourcePath + filename;
                File f = new File(filePath);
                if (f.exists() && !f.isDirectory()) {
                    ClientHandler handler = new ClientHandler(ServerSocket, filePath, ClientPort, ClientAddress);
                    Thread serviceThread = new Thread(handler);
                    System.out.println("thread: " + serviceThread.getId());

                    executor.execute(serviceThread);
                    System.out.println("----------------");
                } else {
                    System.out.println(filename + " not exists.Error");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ServerSocket != null) {
                ServerSocket.close();
            }
        }
    }

    public static List<File> getListFile(String path) {
        File folder = new File(path);
        List<File> list = new ArrayList<>();
        File[] listOfFiles = folder.listFiles();

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                list.add(listOfFile);
            }
        }
        return list;
    }
}
