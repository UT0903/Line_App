package com.example.line_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ChatMessageHolder>{
    List<ChatMessage> mlistMessages;
    String mCurrentUser;
    Context mContext;

    public MessageAdapter(Context context,List<ChatMessage> mlistMessages) {
        this.mlistMessages = mlistMessages;
        mContext=context;
    }

    @NonNull
    @Override
    public ChatMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_msg, parent, false);
        ChatMessageHolder holder = new ChatMessageHolder(view);
        Log.e("onCreateViewHolder", "onCreateViewHolder");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageHolder holder, int position) {
        ChatMessage model = mlistMessages.get(position);
        holder.setValues(model);
    }

    @Override
    public int getItemCount() {
        return mlistMessages.size();
    }


    public class ChatMessageHolder extends RecyclerView.ViewHolder {
        private TextView my_msg, other_name, other_msg;
        private ImageView my_img, other_img;
        RelativeLayout my_layout, other_layout;
        private int my_uid = 0; //TODO
        public ChatMessageHolder(@NonNull View v) {
            super(v);
            Log.e("ChatMessageHolder", "ChatMessageHolder");
            my_msg = v.findViewById(R.id.my_msg);
            other_name = v.findViewById(R.id.other_name);
            other_msg = v.findViewById(R.id.other_msg);

            my_img = v.findViewById(R.id.my_img);
            other_img = v.findViewById(R.id.other_img);

            my_layout = v.findViewById(R.id.my_layout);
            other_layout = v.findViewById(R.id.other_layout);

        }

        public void setValues(final ChatMessage chatMessage) {
            Log.e("setvalue", "setValue");
            if (chatMessage != null) {
                String chatMsg = chatMessage.getMessage();
                if (chatMsg == null) {
                    other_layout.setVisibility(View.GONE);
                    my_layout.setVisibility(View.GONE);
                } else {
                    if (!chatMessage.getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
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
