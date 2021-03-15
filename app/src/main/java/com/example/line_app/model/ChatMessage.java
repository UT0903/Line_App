package com.example.line_app.model;

public class ChatMessage {
    private String message;
    private long time;
    private String userName;
    private String uid;

    public ChatMessage(String userName, String message,long time, String uid) {
        this.userName = userName;
        this.message = message;
        this.time = time;
        this.uid = uid;
    }
    public ChatMessage(String userName, long time, String uid) {
        this.userName = userName;
        this.message = message;
        this.time = time;
        this.uid = uid;
    }
    public ChatMessage() {}
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getTime() {
        return time;
    }

    public String getUserName() {
        return userName;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }


}
