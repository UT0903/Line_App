package com.example.line_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.gson.Gson;
import com.gregacucnik.EditTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private EditTextView inputBox;
    private DatabaseReference dbRef;
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private int my_uid;
    private Toast toast;
    private Query mQuery;
    private List<ChatMessage> msg_list;
    private LinearLayoutManager linearLayoutManager;
    ArrayList<String> keyList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputBox = findViewById(R.id.inputBox);
        dbRef = FirebaseDatabase.getInstance().getReference();
        mQuery = dbRef;
        msg_list = new ArrayList<>();
        adapter = new MessageAdapter(this,msg_list);
        my_uid = 1;
        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        dbRef.addChildEventListener(new ChildEventListener() {
            @Override//收到新訊息時自動往下捲
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (adapter != null) {
                    Log.d("Add", "onChildAdded:" + dataSnapshot.getKey());
                    ChatMessage msg = dataSnapshot.getValue(ChatMessage.class);
                    Log.e("AddMessage", msg.getMessage());
                    msg_list.add(msg);
                    //
                    adapter.notifyDataSetChanged();
                    //adapter.addItem(msg);
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void sendMsg(View view) {
        //String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        String key = dbRef.push().getKey();
        dbRef.child(key).setValue(new ChatMessage("Anthony", this.inputBox.getText().toString(), new Date().getTime(), my_uid));
        this.inputBox.setText("");
        //Log.e("Main", msg);
    }
    private void saveLocalMessage(){
        Gson gson = new Gson();
        String json = gson.toJson(msg_list);
        sharedPreferences.edit().putString("message", json).commit();
    }
    private void retrieveLocalMessage(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString("MyObject", "");
        msg_list = gson.fromJson(json, (Type) ChatMessage.class);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.e("adapter", "onStart");
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
}