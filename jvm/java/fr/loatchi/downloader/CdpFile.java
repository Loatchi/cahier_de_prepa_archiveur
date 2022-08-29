package fr.loatchi.downloader;

public class CdpFile {
    long filesize;
    boolean isDir;
    String name;
    String link;

    public CdpFile(boolean isDir, String name, String link, long fileSize){
        this.isDir = isDir;
        this.name = name;
        this.link = link;
        this.filesize = fileSize;
    }

    public long getFilesize() {
        return filesize;
    }

    public String getURL() {
        return link;
    }
}
