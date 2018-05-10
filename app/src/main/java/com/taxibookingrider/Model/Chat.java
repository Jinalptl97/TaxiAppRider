package com.taxibookingrider.Model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Author: Kartik Sharma
 * Created on: 9/4/2016 , 12:43 PM
 * Project: FirebaseChat
 */

@IgnoreExtraProperties
public class Chat {
    public String sender;
    public String receiver;
    public String message;
    public long timestamp;

    public Chat() {
    }

    public Chat(String sender, String receiver, String message, long timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.timestamp = timestamp;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
