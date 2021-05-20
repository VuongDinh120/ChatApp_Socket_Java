/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Model.FileInfo;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VuongDinh
 */
public class FileServer {

    private static final String MasterHost = "127.0.0.1";
    private static final int MasterPort = 3000;
    public static final int NUM_OF_THREAD = 10;
    public static int SERVER_PORT = 4000;
    public static final String SERVER_HOST = "127.0.0.1";
    public static String sourcePath;

    public static void main(String[] args) {

        SERVER_PORT = setupPort();
        sourcePath = inputStorePath();
        List<FileInfo> files = getListFile(sourcePath);
        MasterServerHandler sendInfoToMaster = new MasterServerHandler(MasterPort, MasterHost);
        sendInfoToMaster.sendFiles(files);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                ObjectOutputStream out = null;
                try {
                    System.out.println("Running Shutdown Hook");
                    Socket socket = new Socket(MasterHost, MasterPort);
                    out = new ObjectOutputStream(socket.getOutputStream());
                    System.out.println("Send message");
                    out.writeUTF("fileserver:exit");
                    out.flush();

                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        out.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        ExecutorService executor = Executors.newFixedThreadPool(NUM_OF_THREAD);
        DatagramSocket ServerSocket = null;
        try {
            System.out.println("Waiting client...");

            ServerSocket = new DatagramSocket(SERVER_PORT);
            while (true) {
                byte[] filename_buf = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(filename_buf, 1024);
                ServerSocket.receive(receivePacket);
                String filename = new String(receivePacket.getData(), 0, receivePacket.getLength());
                int ClientPort = receivePacket.getPort();
                InetAddress ClientAddress = receivePacket.getAddress();
                System.out.println(sourcePath + "\\" + filename);

                String filePath = sourcePath + "\\" + filename;
                File f = new File(filePath);
                if (f.exists() && !f.isDirectory()) {
                    ClientHandler handler = new ClientHandler(ServerSocket, filePath, ClientPort, ClientAddress);
                    Thread serviceThread = new Thread(handler);
//                    System.out.println("thread: " + serviceThread.getId());

                    executor.execute(serviceThread);

                } else {
                    System.out.println(" Not found this file " + filename);
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

    public static List<FileInfo> getListFile(String path) {
        File folder = new File(path);
        List<FileInfo> list = new ArrayList<>();
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                String name = file.getName();
                long size = file.length();
                FileInfo fi = new FileInfo(name, size, SERVER_PORT, SERVER_HOST);
                list.add(fi);
            }
        }
        return list;
    }

    public static String inputStorePath() {
        Scanner scan = new Scanner(System.in);
        String path;
        File fi;
        do {
            System.out.println("Enter your files source location: ");
            path = scan.nextLine();
            fi = new File(path);

            if (!fi.isDirectory()) {
                System.out.println("Error: This is not a right path.");
            }

        } while (!fi.isDirectory());
        return path;
    }

    public static int setupPort() {
        Scanner scan = new Scanner(System.in);
        int port;
        do {
            System.out.println("Enter file server port: ");
            port = scan.nextInt();

            if (port <= 2000) {
                System.out.println("Error: port must be >= 2000");
            }

        } while (port <= 2000);
        return port;
    }

}
