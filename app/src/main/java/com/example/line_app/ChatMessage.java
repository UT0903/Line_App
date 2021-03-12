package com.example.line_app;

public class ChatMessage {
    private String message;
    private long time;
    private String userName;
    private int uid;

    public ChatMessage(String userName, String message,long time, int uid) {
        this.userName = userName;
        this.message = message;
        this.time = time;
    }
    public ChatMessage(String userName, long time, int uid) {
        this.userName = userName;
        this.message = message;
        this.time = time;
    }

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
    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }


}
