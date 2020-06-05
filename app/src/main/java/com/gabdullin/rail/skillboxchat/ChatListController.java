package com.gabdullin.rail.skillboxchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatListController extends RecyclerView.Adapter {

    private List<Message> messageList;
    private RecyclerView recyclerView;

    private static final int TYPE_INCOMING = 0;
    private static final int TYPE_OUTGOING = 1;

    private int messageTextID;

    private int usernameID;
    private int datetimeID;
    private int incomingLayoutID;
    private int outgoingLayoutID;

    ChatListController(){
        messageList = new ArrayList<>();
    }

    //Класс сообщения
    public static class Message{

        String messageText;
        String username;
        Date date;
        Boolean isOutgoing;

        public Message(String messageText, String username, Date date, Boolean isOutgoing) {
            this.messageText = messageText;
            this.username = username;
            this.date = date;
            this.isOutgoing = isOutgoing;
        }
    }

    //Вью сообщения
    public class MessageView extends RecyclerView.ViewHolder{

        TextView username;
        TextView messageText;
        TextView datetime;

        public MessageView(@NonNull View itemView, int usernameID, int messageTextID, int datetimeID) {
            super(itemView);
            this.username = itemView.findViewById(usernameID);
            this.messageText = itemView.findViewById(messageTextID);
            this.datetime = itemView.findViewById(datetimeID);
        }

        void bind(Message message){
            DateFormat fmt = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
            messageText.setText(message.messageText);
            username.setText(message.username);
            datetime.setText(fmt.format(message.date));
        }
    }

    //Прикрепляем контроллер к ресайклер вью
    public void appendTo(RecyclerView recyclerView, Context context){
        this.recyclerView = recyclerView;
        this.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        this.recyclerView.setAdapter(this);
    }

    //Добавить сообщение
    public void addMessage(Message message){
        messageList.add(message);
        this.notifyDataSetChanged();
        this.recyclerView.scrollToPosition(messageList.size() - 1);
    }

    //Определение типа сообщения
    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        return message.isOutgoing ? TYPE_OUTGOING : TYPE_INCOMING;
    }

    //Создание холдера
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_OUTGOING) {
            view = LayoutInflater.from(parent.getContext()).inflate(outgoingLayoutID, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(incomingLayoutID, parent, false);
        }
        return new MessageView(view, usernameID, messageTextID, datetimeID);
    }

    //Биндим сообщение в вью сообщения
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        ((MessageView) holder).bind(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    //Сеттеры для IDшников вью сообщения
    public ChatListController setMessageTextID(int messageTextID) {
        this.messageTextID = messageTextID;
        return this;
    }

    public ChatListController setUsernameID(int usernameID) {
        this.usernameID = usernameID;
        return this;
    }

    public ChatListController setDatetimeID(int datetimeID) {
        this.datetimeID = datetimeID;
        return this;
    }

    public ChatListController setIncomingLayoutID(int incomingLayoutID) {
        this.incomingLayoutID = incomingLayoutID;
        return this;
    }

    public ChatListController setOutgoingLayoutID(int outgoingLayoutID) {
        this.outgoingLayoutID = outgoingLayoutID;
        return this;
    }
}
