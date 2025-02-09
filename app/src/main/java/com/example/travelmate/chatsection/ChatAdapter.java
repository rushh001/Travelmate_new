package com.example.travelmate.chatsection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelmate.R;
import com.example.travelmate.data.MessageObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_SENT = 0;
        private static final int TYPE_RECEIVED = 1;

        private Context context;
        private List<MessageObject> messageList;
        private String currentUserId;

        public ChatAdapter(Context context, List<MessageObject> messageList, String currentUserId) {
            this.context = context;
            this.messageList = messageList;
            this.currentUserId = currentUserId;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == TYPE_SENT) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_message_sent, parent, false);
                return new SentMessageViewHolder(view);
            } else {
                View view = LayoutInflater.from(context).inflate(R.layout.item_message_received, parent, false);
                return new ReceivedMessageViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            MessageObject message = messageList.get(position);

            if (holder instanceof SentMessageViewHolder) {
                ((SentMessageViewHolder) holder).bind(message);
            } else if (holder instanceof ReceivedMessageViewHolder) {
                ((ReceivedMessageViewHolder) holder).bind(message);
            }
        }

        @Override
        public int getItemCount() {
            return messageList.size();
        }

        @Override
        public int getItemViewType(int position) {
            // If the message sender is the current user, it's a sent message, otherwise it's a received message.
            if (messageList.get(position).getSenderId().equals(currentUserId)) {
                return TYPE_SENT;
            } else {
                return TYPE_RECEIVED;
            }
        }

        // Update the message list when new data is received
        public void updateMessages(List<MessageObject> updatedMessages) {
            this.messageList = updatedMessages;
            notifyDataSetChanged();
        }

        // ViewHolder for sent messages
        public class SentMessageViewHolder extends RecyclerView.ViewHolder {

            private TextView messageText, timestampText;

            public SentMessageViewHolder(View itemView) {
                super(itemView);
                messageText = itemView.findViewById(R.id.text_message_sent); // Corrected ID for sent messages
                timestampText = itemView.findViewById(R.id.text_timestamp_sent); // Corrected ID for sent messages
            }

            public void bind(MessageObject message) {
                messageText.setText(message.getMessage());
                timestampText.setText(String.valueOf(formatTimestamp(message.getTimestamp()))); // Display timestamp as String
            }
        }

        // ViewHolder for received messages
        public class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

            private TextView messageText, timestampText;

            public ReceivedMessageViewHolder(View itemView) {
                super(itemView);
                messageText = itemView.findViewById(R.id.text_message_received); // Corrected ID for received messages
                timestampText = itemView.findViewById(R.id.text_timestamp_received); // Corrected ID for received messages
            }

            public void bind(MessageObject message) {
                messageText.setText(message.getMessage());
                timestampText.setText(String.valueOf(formatTimestamp(message.getTimestamp()))); // Display timestamp as String
            }
        }

        private String formatTimestamp(long timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            Date date = new Date(timestamp);
            return sdf.format(date);
        }
    }
