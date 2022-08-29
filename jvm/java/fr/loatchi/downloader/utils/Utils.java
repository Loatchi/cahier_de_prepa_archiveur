package fr.loatchi.downloader.utils;

import fr.loatchi.downloader.CdpDownloaderEventListener;
import fr.loatchi.downloader.CdpFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

public class Utils {

    public static String getHTML(String loginCookie, String link)
            throws InterruptedException, URISyntaxException, IOException {

        URL url = new URL(link);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("GET");
        http.setRequestProperty("Cookie", loginCookie);

        var responseCode = http.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in .readLine()) != null) {
                response.append(inputLine);
            } in .close();

            return response.toString();
        } else {
            throw new RuntimeException("yay");
        }
    }

    public static void downloadDirectLink(CdpDownloaderEventListener listener,
                                          String cookie, String fileName, CdpFile file)
            throws IOException {
        URL url = new URL(file.getURL());
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("GET");
        http.setRequestProperty("Cookie", cookie);

        long filesize = file.getFilesize();

        try (BufferedInputStream inputStream = new BufferedInputStream(http.getInputStream());
        FileOutputStream outputStream = new FileOutputStream(fileName)){

            byte[] data = new byte[1024];
            int count;
            long total = 0;
            float percent;
            long start = System.currentTimeMillis();
            while ((count = inputStream.read(data, 0, 1024)) != -1) {
                outputStream.write(data, 0, count);
                total += count;
                percent = (float) (total) / (float) filesize * 100;
                if(percent > 100F)
                    percent = 100F;
                long now = System.currentTimeMillis();
                if(now - start > listener.waitingTimeBetweenPercent){
                    start = now;
                    listener.onCdpFileDownloadProgress(file, percent);
                }
            }
        }

    }

    public static long getFileSizeMultiplier(String datum) {
        return switch(datum.toLowerCase()){
            case "b" -> 1;
            case "ko", "kb" -> 1000;
            case "mo", "mb" -> 1000 * 1000;
            case "go", "gb" -> 1000 * 1000 * 1000;
            default -> throw new NullPointerException();
        };
    }
}
