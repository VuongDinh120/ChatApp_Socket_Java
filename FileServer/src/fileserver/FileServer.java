/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author VuongDinh
 */
public class FileServer {

    private static final String FileServerHost = "127.0.0.1";
    private static final int FileServerPort = 6000;

    public static void main(String[] args) throws IOException {
        List<File> files = getListFile("D:\\Music\\Chrono Cross Original Soundtrack Revival Disc [FLAC]");
        TCP_File_Master sendInfoToMaster = new TCP_File_Master(FileServerHost, FileServerPort);
        sendInfoToMaster.createConnection(files);
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
