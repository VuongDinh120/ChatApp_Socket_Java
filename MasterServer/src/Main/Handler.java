/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Model.FileInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VuongDinh
 */
public class Handler implements Runnable {

    private Socket socket;
    List<FileInfo> MasterListFile;
    private Object result = null;

    public Handler(Socket socket, List<FileInfo> MasterListFile) {
        this.socket = socket;
        this.MasterListFile = MasterListFile;
    }

    public List<FileInfo> getMasterListFile() {
        return MasterListFile;
    }

    @Override
    public void run() {
        try {

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            String received = in.readUTF();
            String[] message = received.split(":", 2);
            String sender = message[0];
            String action = message[1];
            System.out.println(sender + action);

            switch (sender) {
                case "client" -> {
                    if (action.equals("get_data")) {
                        System.out.println("Sending files data to client");
                        System.out.println(MasterListFile);
                        out.writeObject(MasterListFile);
                        out.flush();
                    }
                }
                case "fileserver" -> {
                    if (action.equals("send_data")) {
                        out.writeUTF("Receiced message!");
                        out.flush();

                        List<FileInfo> listOfFiles = (List<FileInfo>) in.readObject();
                        System.out.println("Received [" + listOfFiles.size() + "] files from: " + socket);

                        out.writeUTF("Master Server has receive your files info.");
                        out.flush();

                        System.out.println("All files:");
                        listOfFiles.forEach((FileInfo fi) -> {
                            System.out.println(fi.toString());
                            MasterListFile.add(fi);
                        });
                    }
                    if (action.equals("exit")) {
                        System.out.println("File Server exit.");
                    }
                }
                default ->
                    System.out.println("Error: wrong code.");
            }
            result = MasterListFile;
            System.out.println("Finish task.");
            socket.close();
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        synchronized (this) {
            notifyAll();
        }
    }

    public synchronized Object get()
            throws InterruptedException {
        while (result == null) {
            wait();
        }

        return result;
    }

}
