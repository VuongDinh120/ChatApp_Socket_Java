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

    private String destinationDirectory;
    private String sourceDirectory;
    private String filename;
    private long fileSize;
    private int piecesOfFile;
    private int lastByteLength;
    private String status;

    public FileInfo() {
    }

    public FileInfo(String destinationDirectory, String sourceDirectory, String filename, long fileSize, int piecesOfFile, int lastByteLength, String status) {
        this.destinationDirectory = destinationDirectory;
        this.sourceDirectory = sourceDirectory;
        this.filename = filename;
        this.fileSize = fileSize;
        this.piecesOfFile = piecesOfFile;
        this.lastByteLength = lastByteLength;
        this.status = status;
    }

    public String getDestinationDirectory() {
        return destinationDirectory;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getFilename() {
        return filename;
    }

    public int getLastByteLength() {
        return lastByteLength;
    }

    public int getPiecesOfFile() {
        return piecesOfFile;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSourceDirectory() {
        return sourceDirectory;
    }

    public String getStatus() {
        return status;
    }

    public void setDestinationDirectory(String destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setLastByteLength(int lastByteLength) {
        this.lastByteLength = lastByteLength;
    }

    public void setPiecesOfFile(int piecesOfFile) {
        this.piecesOfFile = piecesOfFile;
    }

    public void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
}
