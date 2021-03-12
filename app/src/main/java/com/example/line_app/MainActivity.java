package com.example.line_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.gregacucnik.EditTextView;

import org.json.JSONException;
import org.json.JSONObject;

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
    private FirebaseRecyclerPagingAdapter<ChatMessage, ChatMessageHolder> adapter;
    private int my_uid;
    private Toast toast;
    private Query mQuery;
    private PagedList.Config PLConfig;
    private DatabasePagingOptions<ChatMessage> options;
    ArrayList<String> keyList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputBox = findViewById(R.id.inputBox);
        dbRef = FirebaseDatabase.getInstance().getReference();
        mQuery = dbRef.child("message");
        PLConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(30)
                .build();
        options = new DatabasePagingOptions.Builder<ChatMessage>()
                .setLifecycleOwner(this)
                .setQuery(mQuery, PLConfig, ChatMessage.class)
                .build();
    }

    public void sendMsg(View view) {
        String msg = this.inputBox.getText().toString();
        //String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        long time = new Date().getTime();

        String key = dbRef.push().getKey();
        keyList.add(key);
       dbRef.child(key).setValue(new ChatMessage(userName, this.inputBox.getText(), new Date().getTime(), my_uid));

        edtInput.setText("");
        Set<String> saveKeyList = new HashSet<>();
        for (int i = 0; i < keyList.size(); i++) {
            saveKeyList.add(keyList.get(i));
        }
        //sharedPreferences.edit().putStringSet("keyList", saveKeyList).commit();
        //Log.e("Main", msg);

    }
    /*@Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }*/
    private void displayChatMsg() {
        try {
            adapter = new FirebaseRecyclerPagingAdapter<ChatMessage, ChatMessageHolder>
                    (options) {

                @Override
                protected void onBindViewHolder(@NonNull ChatMessageHolder holder, int position, @NonNull ChatMessage model) {

                }

                @Override
                protected void onLoadingStateChanged(@NonNull LoadingState state) {

                }

                public ChatMessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_msg, parent, false);
                    ChatMessageHolder holder = new ChatMessageHolder(view);

                    return holder;
                }
            };
            //recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public class ChatMessageHolder extends RecyclerView.ViewHolder {
        private TextView my_msg, other_name, other_msg;
        private ImageView my_img, other_img;
        RelativeLayout my_layout, other_layout;

        public ChatMessageHolder(@NonNull View v) {
            super(v);
            my_msg = v.findViewById(R.id.my_msg);
            other_name = v.findViewById(R.id.other_name);
            other_msg = v.findViewById(R.id.other_msg);

            my_img = v.findViewById(R.id.my_img);
            other_img = v.findViewById(R.id.other_img);

            my_layout = v.findViewById(R.id.my_layout);
            other_layout = v.findViewById(R.id.other_layout);

        }

        public void setValues(final ChatMessage chatMessage) {
            if (chatMessage != null) {
                String chatMsg = chatMessage.getMessage();
                if (chatMsg == null) {
                    other_layout.setVisibility(View.GONE);
                    my_layout.setVisibility(View.GONE);
                } else {
                    if (chatMessage.getUid() == my_uid) {
                        other_layout.setVisibility(View.VISIBLE);
                        other_img.setVisibility(View.VISIBLE);
                        other_name.setVisibility(View.VISIBLE);
                        other_msg.setVisibility(View.VISIBLE);

                        my_layout.setVisibility(View.GONE);
                        my_img.setVisibility(View.GONE);
                        my_msg.setVisibility(View.GONE);

                        other_name.setText(chatMessage.getUserName());
                        other_msg.setText(chatMessage.getMessage());
                    } else {
                        other_layout.setVisibility(View.GONE);
                        other_img.setVisibility(View.GONE);
                        other_name.setVisibility(View.GONE);
                        other_msg.setVisibility(View.GONE);

                        my_layout.setVisibility(View.VISIBLE);
                        my_img.setVisibility(View.VISIBLE);
                        my_msg.setVisibility(View.VISIBLE);

                        my_msg.setText(chatMessage.getMessage());
                    }
                }
            }
        }
    }
}