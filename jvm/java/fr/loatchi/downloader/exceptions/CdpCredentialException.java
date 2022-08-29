package fr.loatchi.downloader.exceptions;

public class CdpCredentialException extends Exception {
    private final String login;
    private final String password;
    private final String url;

    private final String message;

    public CdpCredentialException(String login, String password, String url, String message){
        this.login = login;
        this.password = password;
        this.url = url;
        this.message = message;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    public String getCdpMessage(){
        return message;
    }
}
