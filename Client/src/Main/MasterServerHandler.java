/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Model.FileInfo;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public List<FileInfo> getFiles() {
        List<FileInfo> listOfFiles = null;
        try {
            //Tạo socket cho client kết nối đến server qua ID address và port number
            Socket socket = new Socket(MasterHost, MasterPort);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            System.out.println("Send message");
            out.writeUTF("client:get_data");
            out.flush();

            System.out.println("Wait response.");
            listOfFiles = (List<FileInfo>) in.readObject();

            System.out.println("Complete get file from master.");
            socket.close();

        } catch (EOFException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return listOfFiles;
    }
}
