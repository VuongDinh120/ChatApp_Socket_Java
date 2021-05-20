/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Model.FileInfo;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;

/**
 *
 * @author VuongDinh
 */
public class MasterServerHandler {

    private int MasterPort;
    private String MasterHost;

    public MasterServerHandler(int MasterPort, String MasterHost) {
        this.MasterPort = MasterPort;
        this.MasterHost = MasterHost;
    }

    public void sendFiles(List<FileInfo> files) {
        try {
            //Tạo socket cho client kết nối đến server qua ID address và port number
            Socket socket = new Socket(MasterHost, MasterPort);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            System.out.println("Send message");
            out.writeUTF("fileserver:send_data");
            out.flush();

            System.out.println("Wait response.");
            String message = in.readUTF();
            System.out.println(">> FROM MASTER SERVER: " + message);

            System.out.println("Send files..");
            out.writeObject(files);
//            out.flush();
            System.out.println("Send finish.");

            message = in.readUTF();
            System.out.println(">> FROM MASTER SERVER: " + message);

            System.out.println("Complete starting file server.");
            socket.close();
        } catch (EOFException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
