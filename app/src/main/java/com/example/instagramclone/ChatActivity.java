package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView chatListView;
    private ArrayList<String> chatList;
    private ArrayAdapter adapter;
    private String receivedUsername;
    private ImageButton btnSend;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent receivedIntentObject = getIntent();
        receivedUsername = receivedIntentObject.getStringExtra("username");
        FancyToast.makeText(ChatActivity.this, receivedUsername, FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true)
                .show();

        setTitle(receivedUsername + "'s Chat");

        btnSend = findViewById(R.id.btnSend);

        chatListView = findViewById(R.id.chatListView);
        chatList = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, chatList);
        chatListView.setAdapter(adapter);

        try {
            ParseQuery<ParseObject> firstUserChatQuery = ParseQuery.getQuery("Chat");
            ParseQuery<ParseObject> secondUserChatQuery = ParseQuery.getQuery("Chat");

            firstUserChatQuery.whereEqualTo("waSender", ParseUser.getCurrentUser().getUsername());
            firstUserChatQuery.whereEqualTo("waReceiver", receivedUsername);

            secondUserChatQuery.whereEqualTo("waSender", receivedUsername);
            secondUserChatQuery.whereEqualTo("waReceiver", ParseUser.getCurrentUser().getUsername());

            ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
            allQueries.add(firstUserChatQuery);
            allQueries.add(secondUserChatQuery);

            ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);
            myQuery.orderByAscending("createdAt");

            myQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseObject chatObject : objects) {
                            String waMessage = chatObject.get("waMessage") + "";
                            if (chatObject.get("waSender").equals(ParseUser.getCurrentUser().getUsername())) {
                                waMessage = ParseUser.getCurrentUser().getUsername() + ": " + waMessage;
                            }
                            if (chatObject.get("waSender").equals(receivedUsername)) {
                                waMessage = receivedUsername + ": " + waMessage;
                            }
                            chatList.add(waMessage);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

        btnSend.setOnClickListener(ChatActivity.this);
    }

    @Override
    public void onClick(View view) {

        EditText edtChat = findViewById(R.id.edtChat);

        ParseObject chat = new ParseObject("Chat");
        chat.put("waSender", ParseUser.getCurrentUser().getUsername());
        chat.put("waReceiver", receivedUsername);
        chat.put("waMessage", edtChat.getText().toString());
        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    chatList.add(ParseUser.getCurrentUser().getUsername() + ": " + edtChat.getText().toString());
                    adapter.notifyDataSetChanged();
                    edtChat.setText("");
                }
            }
        });
    }

}