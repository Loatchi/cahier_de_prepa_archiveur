package fr.cdp;

import fr.cdp.downloader.CdpDownloader;
import fr.cdp.downloader.CdpDownloaderEventListener;

import java.io.File;

public class Main {

    public static void main(String[] arguments){
        String login = null;
        String password = null;
        String cdpIdentifier = null;
        String directory = "./cahier_de_prepa";

        boolean gui = true;

        try{

            for(int i=0; i < arguments.length; i++){
                String arg = arguments[i];

                if(arg.equals("--cdp-id")){
                    cdpIdentifier = arguments[i+1];
                }

                if(arg.equals("--login")){
                    login = arguments[i+1];
                }

                if(arg.equals("--password")){
                    password = arguments[i+1];
                }

                if(arg.equals("--no-gui")){
                    gui = false;
                }

                if(arg.equals("--dir")){
                    directory = arguments[i+1];
                }

                if(arg.equals("--help")){
                    printHelpMessage();
                    return;
                }

            }
        } catch (ArrayIndexOutOfBoundsException e){
            System.err.println("Impossible to parse arguments, aborting.\nSee --help.");
        }

        if(!gui){

            if(login == null || password == null || cdpIdentifier == null)
                System.err.println("You must initialize all parameters: --login, --password, --cdp-id\nSee --help.");

            CdpDownloader downloader = new CdpDownloader(login, password,
                    cdpIdentifier, CdpDownloaderEventListener.defaultEventListener());

            File dir = new File(directory);

            if(dir.exists() && dir.isDirectory()){
                System.err.println("Directory: " + directory + " already exists, aborting.");
                System.exit(1);
            }

            downloader.download(directory);
        }

    }

    private static void printHelpMessage() {
        String helpMessage = """
                Une application pour télécharger une archive cahier de prépa sur son ordinateur.
                Les paramètres:
                --login STRING      L'identifiant cahier de prépa.
                --password STRING   Le mot de passe cahier de prépa.
                --no-gui            Si le paramètre est présent, l'application se lance en
                                    sans interface graphique (directement dans un terminal)
                --dir DIRECTORY     Le répertoire où sera téléchargé l'archive (défaut=./cahier_de_prepa)              
                """;
        System.out.println(helpMessage);
    }

}
