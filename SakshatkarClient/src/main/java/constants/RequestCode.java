package constants;

public enum RequestCode {

    LOGIN_REQUEST,
    SIGNUP_REQUEST,
    USERSEARCH_REQUEST,
    ONLINEUSER_REQUEST,
    FILEDOWNLOADCOMPLETE_REQUEST;

    RequestCode(){
        this.toString();
    }
}