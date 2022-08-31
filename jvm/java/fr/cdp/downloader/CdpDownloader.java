package fr.cdp.downloader;

import fr.cdp.downloader.exceptions.CdpCredentialException;
import fr.cdp.downloader.utils.Utils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CdpDownloader {

    String cookie;
    public final String BASE_URL = "https://cahier-de-prepa.fr/";
    Pattern regexDir = Pattern.compile("<a href=\".*?\\?rep=(\\d+)\">(?:<.*?>)*(.*?)?<.*?/a>");
    Pattern regexFile = Pattern.compile("<span class=\"docdonnees\">\\(([a-z]+), .*?, (.*?)\\).*?<a href=\".*?\\?id=(\\d+)\">(?:<.*?>)*(.*?)?<.*?/a>");
    Pattern regexMainPage = Pattern.compile("<a href=\"docs\\?([^\"]*)\">");

    String subDirUrl = null;

    String url = null;

    CdpDownloaderEventListener listener;

    public CdpDownloader(String login, String password, String urlString, CdpDownloaderEventListener handler){
        listener = handler;
        try {
            URL url = new URL(BASE_URL + urlString + "/ajax.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            String payload = "csrf-token=undefined" +
                    "&login=" + URLEncoder.encode(login, StandardCharsets.UTF_8)
                    + "&motdepasse=" + URLEncoder.encode(password, StandardCharsets.UTF_8)
                    + "&connexion=1}";
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
                os.flush();
            }

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                JSONParser parser = new JSONParser();

                JSONObject text = (JSONObject) parser.parse(response.toString());
                String message = text.get("message").toString();
                String state = text.get("etat").toString();

                if (!state.equals("ok")) {
                    throw new CdpCredentialException(login, password, url.toString(), message);
                }

            }
            cookie = connection.getHeaderField("Set-Cookie").split(";")[0];
            subDirUrl = urlString;
            this.url = BASE_URL + urlString;
            connection.disconnect();
        } catch (CdpCredentialException | IOException | ParseException e) {
            handler.onCookieRetrievedException(e);
            return;
        }

        handler.onCookieRetrieved(cookie);
    }

    private void _createTree(CdpTree tree, String usedUrl, List<Integer> directoriesExplored, String path){

        String html;
        try {
            html = Utils.getHTML(cookie, usedUrl);
        } catch (InterruptedException | URISyntaxException | IOException e) {
            listener.onCdpFileFetchException(e, usedUrl);
            return;
        }
        int idx = html.indexOf("<p class=");

        // empty directory
        if(idx == -1){
            return;
        }

        html = html.substring(idx);
        Matcher matcher = regexDir.matcher(html);

        while(matcher.find()){
            int redirect = Integer.parseInt(matcher.group(1));
            String name = matcher.group(2);

            String newUrl = url + "/docs?rep=" + redirect;
            CdpFile file = new CdpFile(true, name, newUrl, -1);
            CdpTree child = tree.addChild(file);

            if(!directoriesExplored.contains(redirect)){
                directoriesExplored.add(redirect);
                listener.onCdpFileFetched(path, file);
                _createTree(child, newUrl, directoriesExplored, String.valueOf(Paths.get(path, name)));
            }

        }

        matcher = regexFile.matcher(html);

        while(matcher.find()){
            String fileExtension = matcher.group(1);
            String[] data = matcher.group(2).split("&nbsp;");
            long fileSize = Long.parseLong(data[0]) * Utils.getFileSizeMultiplier(data[1]);
            int redirect = Integer.parseInt(matcher.group(3));
            String name = matcher.group(4);

            String usedName = name + "." + fileExtension;

            String newUrl = url + "/download?id=" + redirect;
            CdpFile file = new CdpFile(false, usedName, newUrl, fileSize);
            tree.addChild(file);
            listener.onCdpFileFetched(path, file);
        }

    }

    public CdpTree createTree(){

        CdpTree root = new CdpTree(new CdpFile(true, "ROOT", null, -1));
        List<Integer> directoriesExplored = new ArrayList<>();

        for(CdpTree dir : parseMainPage()){
            _createTree(dir, dir.file.link, directoriesExplored, "ROOT");
            root.addChild(dir);
        }

        listener.onCdpTreeCreated(root);

        return root;
    }

    private List<CdpTree> parseMainPage(){
        Matcher matcher;
        try {
            matcher = regexMainPage.matcher(Utils.getHTML(cookie, url));
        } catch (InterruptedException | URISyntaxException | IOException e) {
            listener.onMainPageParsedException(e);
            return new ArrayList<>();
        }

        List<CdpTree> directories = new ArrayList<>();

        while(matcher.find()){
            directories.add(new CdpTree(new CdpFile(true, matcher.group(1), url + "/docs?" + matcher.group(1), -1)));
        }

        listener.onMainPageParsed(directories);

        return directories;
    }

    private void _download(String directory, CdpTree tree){
        File dir = Paths.get(directory, tree.getName()).toFile();

        if(!tree.isEnd()){

            if(dir.mkdir()){
                for(CdpTree child: tree.getChildren()){
                    _download(directory + "/" + tree.file.name, child);
                }
            }

        } else {
            try {
                Utils.downloadDirectLink(listener, cookie, directory + "/" + tree.file.name, tree.file);
            } catch (IOException e) {
                listener.onCdpFileDownloadException(e, tree.file);
            }
            listener.onCdpFileDownloaded(directory, tree.file);
        }

    }

    public void download(String directory){
        CdpTree root = createTree();
        if(new File(directory).mkdir()){
            for(CdpTree tree : root.getChildren()){
                _download(directory, tree);
            }
        }
        listener.onCdpDownloadFinish(directory);
    }

}
