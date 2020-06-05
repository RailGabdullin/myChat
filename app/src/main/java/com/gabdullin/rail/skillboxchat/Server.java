package com.gabdullin.rail.skillboxchat;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import androidx.core.util.Consumer;
import androidx.core.util.Pair;

public class Server {

    private WebSocketClient webSocketClient;
    private Map<Long, String> names = new ConcurrentHashMap<>();
    private Consumer<Pair<String, String>> onMessageReceived;

    public Server (Consumer<Pair<String, String>> onMessageReceived){
        this.onMessageReceived = onMessageReceived;
    }

    public void connect(){
        URI address = null;
        try {
            address = new URI("ws://35.214.1.221:8881");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        Log.i("SERVER_MAIN", "READY_TO_START");
        webSocketClient = new WebSocketClient(address) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.i("SERVER", "Connected to server");
            }

            @Override
            public void onMessage(String messageJson) {
                int type = Protocol.getType(messageJson);
                if (type == Protocol.MESSAGE) {
                    displayIncomingMessage(Protocol.userMessageUnpack(messageJson));
                }
                if (type == Protocol.USER_STATUS){
                    updateUserStatus(Protocol.unpackStatus(messageJson));
                }
                Log.i("SERVER", "Got message: " + messageJson);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.i("SERVER", "Connection closed");
            }

            @Override
            public void onError(Exception ex) {
                Log.e("SERVER", "onError", ex);
            }
        };
        webSocketClient.connect();
    }

    public void disconnect(){
        webSocketClient.close();
    }

    public void sendMessage(String text){
        if(webSocketClient != null && webSocketClient.isOpen()){
            Protocol.Message message = new Protocol.Message(text);
            webSocketClient.send(Protocol.packMessage(message));
        }
    }

    public void sendName(String name){
        if(webSocketClient != null && webSocketClient.isOpen()){
            Protocol.UserName userName = new Protocol.UserName(name);
            webSocketClient.send(Protocol.packName(userName));
        }
    }

    private void updateUserStatus(Protocol.UserStatus userStatus) {
        Protocol.User user = userStatus.getUser();
        if (userStatus.isConnected()){
            names.put(user.getID(), user.getName());
        } else {
            names.remove(user.getID());
        }
    }

    private void displayIncomingMessage(Protocol.Message incomingMessage) {
        String name = names.get(incomingMessage.getSender());
        if (name == null){
            name = "Unnamed";
        }
        onMessageReceived.accept(new Pair<String, String>(name, incomingMessage.getEncodedText()));
    }
}
