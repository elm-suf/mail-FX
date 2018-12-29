package model;

import java.io.Serializable;

public class Request implements Serializable{
//    public enum Action{
//        DOWNLOAD_ALL,
//        LOGIN,
//        LOGOUT,
//        SEND,
//        FORWARD,
//        DELETE;
//    }

    private String action;
    private Object requestObject;


    public Request(String action, Object requestObject) {
        this.action = action;
        this.requestObject = requestObject;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Object getRequestObject() {
        return requestObject;
    }

    public void setRequestObject(Object requestObject) {
        this.requestObject = requestObject;
    }
}
