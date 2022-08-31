package fr.cdp.downloader.gui;

import javax.swing.*;

public class CdpOutputLabel extends JLabel {

    String[] fields = {"<br>", "<br>", "<br>", "<br>", "<br>"};

    public CdpOutputLabel(){
        super(""); // 5 \n
    }

    private String toDisplay(){
        StringBuilder builder = new StringBuilder("<html>");
        for (String field : fields) builder.append(field);
        return builder.append("</html>").toString() ;
    }

    public void updateText(){
        setText(toDisplay());
    }

    public void setDownloadingFile(String filename, int percent){
        fields[2] = "Téléchargement: <font color='cyan'>" + filename + "</font>"
                + "<font color='pink'>" + "[" + percent + "]" + "</font><br>";
        updateText();
    }

    public void downloadingFinished(String outputDir){
        fields[3] = "Chemin vers l'archive: <font color='green'>" + outputDir + "</font><br>";
        updateText();
    }

    public void setLoginState(boolean state, String possibleMessage){
        StringBuilder builder = new StringBuilder();
        builder.append("Connecté: ");
        if(state)
            builder.append("<font color='green'>✓</font>");
        else{
            builder.append("<font color='red'>⨯</font> ");
            builder.append("<font color='red'>").append(possibleMessage).append("</font>");
        }

        builder.append("<br>");

        fields[0] = builder.toString();
        updateText();
    }

    public void setParsingFile(String filename){
        fields[1] = "Analyse des fichiers: <font color='cyan'>" + filename + "</font><br>";
        updateText();
    }

    public void parsingFinished(){
        fields[1] = "Analyse des fichiers: <font color='green'>Terminée</font><br>";
        updateText();
    }

    public void setError(String error){
        fields[4] = "<font color='red'>ERREUR: " + error + "</font><br>";
        updateText();
    }

    public void finish(){
        fields[4] = "ARCHIVE PROPREMENT TÉLÉCHARGÉE";
        updateText();
    }

}
