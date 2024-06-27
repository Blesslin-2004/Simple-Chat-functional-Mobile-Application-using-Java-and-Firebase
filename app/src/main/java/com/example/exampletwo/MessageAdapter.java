package com.example.exampletwo;


import android.content.Context;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exampletwo.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private Context context;
    private List<com.example.exampletwo.Message> messageList;
    private String currentUserId; // The ID of the current user

    public MessageAdapter(Context context, List<com.example.exampletwo.Message> messageList, String currentUserId) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        com.example.exampletwo.Message message = messageList.get(position);

        Toast.makeText(context, ""+message.getSenderId(), Toast.LENGTH_SHORT).show();

        if (message != null && message.getSenderId() != null && message.getSenderId().equalsIgnoreCase("sender")){
              holder.senderTextView.setVisibility(View.VISIBLE);
            holder.receiverTextview.setVisibility(View.GONE);
            holder.senderTextView.setText(message.getText());

        } else {
            holder.senderTextView.setVisibility(View.GONE);
            holder.receiverTextview.setVisibility(View.VISIBLE);
            holder.receiverTextview.setText(message.getText());
        }




    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView senderTextView, receiverTextview;
        RelativeLayout messageRt;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            receiverTextview = itemView.findViewById(R.id.recieverTextView);


            messageRt = itemView.findViewById(R.id.messageLayout);
        }
    }
}
