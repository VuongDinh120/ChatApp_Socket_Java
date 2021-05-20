/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import Model.DownloadFileInfo;
import Model.FileInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author VuongDinh
 */
public class Client {

    private static final int PIECES_OF_FILE_SIZE = 1024 * 32;
    private static String destPath = "D:\\New folder\\";
    public static final int NUM_OF_THREAD = 10;
    public static final int SERVER_PORT = 3000;
    public static final String SERVER_IP = "127.0.0.1";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List<FileInfo> masterFileList = new ArrayList<>();
        MasterServerHandler masterHandler = new MasterServerHandler(SERVER_PORT, SERVER_IP);
        masterFileList = masterHandler.getFiles();

        System.out.println("Download all files.");
        showListFile(masterFileList);
        destPath = inputStorePath()+"\\";
        System.out.println(masterFileList);
        try {
            ExecutorService executor = Executors.newFixedThreadPool(NUM_OF_THREAD);

            for (FileInfo fi : masterFileList) {
                InetAddress ipAddress = InetAddress.getByName(fi.getHost());
                FileServerHandler handler = new FileServerHandler(fi.getFileName(), destPath, ipAddress, fi.getPort());
                Thread serviceThread = new Thread(handler);
                executor.execute(serviceThread);
            }
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
        System.out.println("COMPLETE DOWNLOAD YOUR FILE. SEE YOU LATE!");
        System.out.println("EXIT.");
    }

    public static String inputStorePath() {
        Scanner scan = new Scanner(System.in);
        String path;
        File fi;
        do {
            System.out.println("Enter your stored files location: ");
            path = scan.nextLine();
            fi = new File(path);

            if (!fi.isDirectory()) {
                System.out.println("Error: This is not a right path.");
            }

        } while (!fi.isDirectory());
        return path;
    }

//    public static List<FileInfo> selectFilesDownload(List<FileInfo> masterFileList) {
//        Scanner scan = new Scanner(System.in);
//        String str = "";
//        do {
//
//            System.out.println("Select file  to download: ");
//            scan.nextLine();
//            
//        } while (!"".equals(str));
//
//        return ;
//    }

    public static void showListFile(List<FileInfo> masterFileList) {
        System.out.println("List available files to download:");
        for (int i = 0; i < masterFileList.size(); i++) {
            System.out.println("\t" + i + ". " + masterFileList.get(i).toString());
        }
    }
}
