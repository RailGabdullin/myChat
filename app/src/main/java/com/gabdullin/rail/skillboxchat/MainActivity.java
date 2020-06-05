package com.gabdullin.rail.skillboxchat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private Button sendButton;
    private EditText messageText;
    private RecyclerView recyclerView;
    private ChatListController chatListController;
    private Server server;

    private String myName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        recyclerView.findViewById(R.id.RecyclerView);

        chatListController = new ChatListController();
        chatListController.appendTo((RecyclerView) findViewById(R.id.RecyclerView), this);
        chatListController
                .setUsernameID(R.id.user_name)
                .setDatetimeID(R.id.messageTime)
                .setMessageTextID(R.id.message_text)
                .setIncomingLayoutID(R.layout.incoming_message_layout)
                .setOutgoingLayoutID(R.layout.outgoing_message_layout);

        messageText = findViewById(R.id.messageText);

        final EditText inputName = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Введите ваше имя")
                .setView(inputName)
                .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myName = inputName.getText().toString();
                        server.sendName(myName);
                    }
                })
                .show();

        findViewById(R.id.send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatListController.addMessage(new ChatListController.Message(messageText.getText().toString(), myName, new Date(), true));
                server.sendMessage(messageText.getText().toString());
                messageText.setText("");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        server = new Server(new Consumer<Pair<String, String>>() {
            @Override
            public void accept(final Pair<String, String> stringStringPair) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chatListController.addMessage(new ChatListController.Message(stringStringPair.second, stringStringPair.first, new Date(), false));
                    }
                });
            }
        });
        Log.i("SERVER_MAIN", "START");
        server.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        server.disconnect();
    }
}
