package fr.loatchi.downloader;

import java.util.ArrayList;
import java.util.List;

public class CdpTree {
    CdpFile file;
    List<CdpTree> children = new ArrayList<>();
    public CdpTree(CdpFile file){
        this.file = file;
    }

    public boolean isEnd(){
        return !file.isDir;
    }

    public List<CdpTree> getChildren() {
        return children;
    }

    public CdpTree addChild(CdpFile file){
        CdpTree child = new CdpTree(file);
        getChildren().add(child);
        return child;
    }

    public void addChild(CdpTree tree){
        getChildren().add(tree);
    }

    public String getName(){
        return file.name;
    }

    @Override
    public String toString() {
        if(file.isDir)
            return "D: " + file.name;
        else
            return "F: " + file.name;
    }
}
