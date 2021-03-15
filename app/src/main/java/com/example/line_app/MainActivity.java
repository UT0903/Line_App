package com.example.line_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.line_app.adapter.MessageAdapter;
import com.example.line_app.model.ChatMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.gregacucnik.EditTextView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditTextView inputBox;
    private DatabaseReference dbRef;
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private Toast toast;
    private List<ChatMessage> msg_list;
    private LinearLayoutManager linearLayoutManager;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLogin();
        initInstance();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 設置要用哪個menu檔做為選單
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        // 取得點選項目的id
        int id = item.getItemId();

        // 依照id判斷點了哪個項目並做相應事件
        if (id == R.id.action_settings) {
            // 按下「設定」要做的事
            Toast.makeText(this, "設定", Toast.LENGTH_SHORT).show();
            return true;
        }*/
        FirebaseAuth.getInstance().signOut();
        finish();
        return super.onOptionsItemSelected(item);
    }
    private void checkLogin(){
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Log.e("NotLogin", "NotLogin");
            startActivityForResult(new Intent(this, AuthActivity.class), 1);

        }
        else {
            Log.e("IsLogin", "islogin");
        }
    }
    private void initInstance(){
        inputBox = findViewById(R.id.inputBox);
        dbRef = FirebaseDatabase.getInstance().getReference();
        msg_list = new ArrayList<>();
        adapter = new MessageAdapter(this,msg_list);
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
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            return;
        }
        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        String key = dbRef.push().getKey();
        dbRef.child(key).setValue(new ChatMessage(userName, this.inputBox.getText().toString(), new Date().getTime(), FirebaseAuth.getInstance().getCurrentUser().getUid()));
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