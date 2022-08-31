package fr.cdp.downloader;

import fr.cdp.downloader.exceptions.CdpCredentialException;
import fr.cdp.downloader.gui.CdpOutputLabel;

import java.util.List;

public abstract class CdpDownloaderEventListener {

    public int waitingTimeBetweenPercent;
    public static int DEFAULT_WAITING_TIME_BETWEEN_PERCENT = 200;

    public CdpDownloaderEventListener(int waitingTimeBetweenPercent){
        this.waitingTimeBetweenPercent = waitingTimeBetweenPercent;
    }

    public static CdpDownloaderEventListener defaultEventListener() {
        return new CdpDownloaderEventListener(DEFAULT_WAITING_TIME_BETWEEN_PERCENT) {
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

            @Override
            public void onCdpDownloadFinish(String directory) {
                System.out.println("Finished download the entire archive: " + directory);
            }
        };
    }

    public static CdpDownloaderEventListener guiEventListener(CdpOutputLabel label){
        return new CdpDownloaderEventListener(DEFAULT_WAITING_TIME_BETWEEN_PERCENT) {
            @Override
            public void onCdpFileDownloaded(String location, CdpFile file) {
                label.setDownloadingFile(file.name, 100);
            }

            @Override
            public void onCdpFileDownloadException(Exception e, CdpFile file) {

            }

            @Override
            public void onCdpFileDownloadProgress(CdpFile file, float progress) {
                label.setDownloadingFile(file.name, (int) progress);
            }

            @Override
            public void onCdpFileFetched(String location, CdpFile file) {
                label.setParsingFile(file.name);
            }

            @Override
            public void onCdpFileFetchException(Exception e, String url) {

            }

            @Override
            public void onCdpTreeCreated(CdpTree result) {
                label.parsingFinished();
            }

            @Override
            public void onMainPageParsed(List<CdpTree> directories) {

            }

            @Override
            public void onMainPageParsedException(Exception e) {

            }

            @Override
            public void onCookieRetrieved(String cookie) {
                label.setLoginState(true, "");
            }

            @Override
            public void onCookieRetrievedException(Exception e) {

                if(e instanceof CdpCredentialException ){
                    label.setLoginState(false, ((CdpCredentialException) e).getCdpMessage());
                } else {
                    label.setLoginState(false, e.getClass().toString());
                }
            }

            @Override
            public void onCdpDownloadFinish(String directory) {
                label.downloadingFinished(directory);
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
    public abstract void onCdpDownloadFinish(String directory);
}
