/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package masterserver;

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
import model.fileInfo;

/**
 *
 * @author VuongDinh
 */
public class MasterServer {

    private static final int MasterServerPort = 4000;
    private static final String MasterServerHost = "127.0.0.1";

    private static String getFileSizeMegaBytes(File file) {
        return (double) file.length() / (1024 * 1024) + " mb";
    }

    private static String getFileSizeKiloBytes(File file) {
        return (double) file.length() / 1024 + "  kb";
    }

    private static String getFileSizeBytes(File file) {
        return file.length() + " bytes";
    }
    
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //Tạo socket server, chờ tại cổng '3000'
        ServerSocket fileServerSocket = new ServerSocket(MasterServerPort);

        //chờ yêu cầu từ client
        System.out.println("ServerSocket awaiting connections...");
        Socket connectionSocket = fileServerSocket.accept();
        System.out.println("Connection from " + connectionSocket + "!");

        // get the input stream from the connected socket
        InputStream inputStream = connectionSocket.getInputStream();
        // create a DataInputStream so we can read data from it.
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        // Tạo outputStream, nối tới socket
        DataOutputStream outToClient
                = new DataOutputStream(connectionSocket.getOutputStream());

        //Đọc thông tin file từ socket
        List<File> listOfFiles = (List<File>) objectInputStream.readObject();
        outToClient.writeBytes("Master Server has receive your files info.");
        System.out.println("Received [" + listOfFiles.size() + "] files from: " + connectionSocket);
        // print out the text of every message
        System.out.println("All files:");
        listOfFiles.forEach((File fi) -> {
            System.out.println(fi.getName() + "\t size: " + getFileSizeBytes(fi));
            System.out.println("\t" + fi.getPath());
        });

        //ghi dữ liệu ra socket
        System.out.println("Closing sockets.");
        connectionSocket.close();
        fileServerSocket.close();
    }

}
