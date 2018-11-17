package com.uievent.exercise.chat.Model;

public class Chat {
    private String sender;
    private String receiver;
    private String mess;

    public Chat(String sender, String receiver, String imageUrl) {
        this.sender = sender;
        this.receiver = receiver;
        this.mess = imageUrl;
    }

    public Chat() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String imageUrl) {
        this.mess = imageUrl;
    }
}
