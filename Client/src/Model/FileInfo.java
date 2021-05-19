/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author VuongDinh
 */
public class FileInfo {

    private String fileName;
    private String sourcePath;
    private float sizeMB;
    private int port;
    private String host;

    public FileInfo(String fileName, String sourcePath, float sizeMB, int port, String host) {
        this.fileName = fileName;
        this.sourcePath = sourcePath;
        this.sizeMB = sizeMB;
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

    public float getSizeMB() {
        return sizeMB;
    }

    public String getSourcePath() {
        return sourcePath;
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

    public void setSizeMB(float sizeMB) {
        this.sizeMB = sizeMB;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

}
