package fr.loatchi.downloader;

import java.util.List;

public abstract class CdpDownloaderEventListener {

    public int waitingTimeBetweenPercent;

    public CdpDownloaderEventListener(int waitingTimeBetweenPercent){
        this.waitingTimeBetweenPercent = waitingTimeBetweenPercent;
    }

    public static CdpDownloaderEventListener defaultEventListener() {
        return new CdpDownloaderEventListener(200) {
            @Override
            public void onCdpFileDownloaded(String location, CdpFile file) {
                System.out.println("Downloaded: " + file.name);
            }

            @Override
            public void onCdpFileDownloadException(Exception e, CdpFile file) {
                System.out.println("Error occurred on: " + file.name);
            }

            @Override
            public void onCdpFileDownloadProgress(CdpFile file, float progress) {
                System.out.println("Downloading: " + file.name + " [" + progress + "]");
            }

            @Override
            public void onCdpFileFetched(String location, CdpFile file) {
                System.out.println("Found: " + file.name);
            }

            @Override
            public void onCdpFileFetchException(Exception e, String url) {
                System.out.println("Error occurred on fetch: " + url);
            }

            @Override
            public void onCdpTreeCreated(CdpTree result) {
                System.out.println("Properly created the tree");
            }

            @Override
            public void onMainPageParsed(List<CdpTree> directories) {
                System.out.println("Properly Parsed Main Page");
            }

            @Override
            public void onMainPageParsedException(Exception e) {
                System.out.println("Error on Parse Main Page");
            }

            @Override
            public void onCookieRetrieved(String cookie) {
                System.out.println("Properly extracted the cookie");
            }

            @Override
            public void onCookieRetrievedException(Exception e) {
                System.out.println("Cookie Extracted error");
            }
        };
    }

    public abstract void onCdpFileDownloaded(String location, CdpFile file);
    public abstract void onCdpFileDownloadException(Exception e, CdpFile file);
    public abstract void onCdpFileDownloadProgress(CdpFile file, float progress);
    public abstract void onCdpFileFetched(String location, CdpFile file);
    public abstract void onCdpFileFetchException(Exception e, String url);
    public abstract void onCdpTreeCreated(CdpTree result);
    public abstract void onMainPageParsed(List<CdpTree> directories);
    public abstract void onMainPageParsedException(Exception e);
    public abstract void onCookieRetrieved(String cookie);
    public abstract void onCookieRetrievedException(Exception e);


}
