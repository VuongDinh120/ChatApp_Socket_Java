/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;

/**
 *
 * @author VuongDinh
 */
public class FileInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String fileName;
    private long size;
    private int port;
    private String host;

    public FileInfo(String fileName, long size, int port, String host) {
        this.fileName = fileName;
        this.size = size;
        this.port = port;
        this.host = host;
    }

    public String getFileName() {
        return fileName;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public long getSize() {
        return size;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return fileName + "\n\t\t\tsize: " + size + " byte\tsource: " + host + ":" + port;
    }

}
