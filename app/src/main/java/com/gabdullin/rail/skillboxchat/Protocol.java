package com.gabdullin.rail.skillboxchat;

import com.google.gson.Gson;

public class Protocol {

    public static final int USER_STATUS = 1;
    public static final int MESSAGE = 2;
    public static final int USER_NAME = 3;

    static class Message{
        private static final int GROUP_CHAT = 1;
        private long sender;
        private long receiver = GROUP_CHAT;
        private String encodedText;

        public  Message (String encodedText){
            this.encodedText = encodedText;
        }

        public Message setSender(long sender) {
            this.sender = sender;
            return this;
        }

        public long getSender() {
            return sender;
        }

        public Message setReceiver(long receiver) {
            this.receiver = receiver;
            return this;
        }

        public long getReceiver() {
            return receiver;
        }

        public Message setEncodedText(String encodedText) {
            this.encodedText = encodedText;
            return this;
        }

        public String getEncodedText() {
            return encodedText;
        }
    }

    static class User{
        private long ID;
        private String name;

        public long getID() {
            return ID;
        }

        public String getName() {
            return name;
        }

        public User setID(long ID) {
            this.ID = ID;
            return this;
        }

        public User setName(String name) {
            this.name = name;
            return this;
        }
    }

    static class UserStatus{
        private User user;
        private boolean isConnected;

        public User getUser() {
            return user;
        }

        public UserStatus setName(User user) {
            this.user = user;
            return this;
        }

        public boolean isConnected() {
            return isConnected;
        }

        public UserStatus setConnected(boolean connected) {
            isConnected = connected;
            return this;
        }
    }

    static class UserName{
        private String name;

        public UserName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public UserName setName(String name) {
            this.name = name;
            return this;
        }
    }

    public static int getType (String json){
        if(json == null || json.length() == 0){
            return -1;
        }
        return Integer.parseInt(json.substring(0, 1));
    }

    public static UserStatus unpackStatus(String json){
        Gson g = new Gson();
        return g.fromJson(json.substring(1), UserStatus.class);
    }

    public static Message userMessageUnpack(String json){
        Gson g = new Gson();
        return g.fromJson(json.substring(1), Message.class);
    }

    public static String packMessage(Message message){
        Gson g = new Gson();
        return MESSAGE + g.toJson(message);
    }

    public static String packName(UserName userName){
        Gson g = new Gson();
        return USER_NAME + g.toJson(userName);
    }
}
