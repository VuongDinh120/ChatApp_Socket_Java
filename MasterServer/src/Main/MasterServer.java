/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import Model.FileInfo;
import java.io.DataInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author VuongDinh
 */
public class MasterServer {

    private static final int MasterServerPort = 3000;
    private static final String MasterServerHost = "127.0.0.1";
    private static final int NUM_OF_THREAD = 10;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //Tạo socket server, chờ tại cổng '3000'
        ServerSocket MasterServerSocket = new ServerSocket(MasterServerPort);
        ExecutorService executor = Executors.newFixedThreadPool(NUM_OF_THREAD);
        List<FileInfo> masterFileList = new ArrayList<>();
        Handler Tasks;
        System.out.println("Starting Master server socket.");
        while (true) {
            System.out.println("Listening in address [127.0.0.1]:[3000]");
            System.out.println("ServerSocket awaiting connections...");
            Socket socket = MasterServerSocket.accept();
            System.out.println("Connection from " + socket + "!");

            Tasks = new Handler(socket, masterFileList);
            Thread serviceThread = new Thread(Tasks);
            executor.execute(serviceThread);
            masterFileList = (List<FileInfo>) Tasks.get();
            showAvailableFiles(masterFileList);
        }
    }

    private static void showAvailableFiles(List<FileInfo> masterFileList) {
        System.out.println("Available files to download:");
        if (masterFileList.isEmpty()) {
            System.out.println("Nothing.");
            return;
        }
        for (int i = 0; i < masterFileList.size(); i++) {
            System.out.println("\t[" + i + "]. " + masterFileList.get(i).toString());
        }
    }

}
