package com.example.who_nextdoor.BoardRecycler;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.who_nextdoor.ChatDataInfo;
import com.example.who_nextdoor.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.BoardViewHolder> {
    private List<ChatDataInfo> mDataset;
    private String myNickName;
    private String myId;

    public ChatAdapter(List<ChatDataInfo> myDataset, Context context, String myNickName, String myId) {
        mDataset = myDataset;
        this.myNickName = myNickName;
        this.myId = myId;
    }

    public ChatDataInfo getChatDataInfo(int pos) {return mDataset.get(pos);}
    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat,parent,false);
        BoardViewHolder holder = new BoardViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        ChatDataInfo chat = mDataset.get(position);
        holder.TextView_nickname.setText(chat.getNickname());
        holder.TextView_msg.setText(chat.getMsg());


        if(chat.getUid().equals(this.myId)) {
            holder.TextView_nickname.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.TextView_msg.setBackgroundResource(R.drawable.bubble_a);
            holder.TextView_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.LinearLayout_main.setGravity(Gravity.RIGHT);
        }
        else {
            holder.TextView_nickname.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.TextView_msg.setBackgroundResource(R.drawable.bubble_b);
            holder.TextView_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.LinearLayout_main.setGravity(Gravity.LEFT);
        }

    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }
    public ChatDataInfo getChat(int position) {
        return mDataset != null ? mDataset.get(position) : null;
    }

    public void addChat(ChatDataInfo chat) {
        mDataset.add(chat);
        notifyItemInserted(mDataset.size()-1);
    }

    public class BoardViewHolder extends RecyclerView.ViewHolder {
        TextView TextView_nickname;
        TextView TextView_msg;
        LinearLayout LinearLayout_main;

        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);
            this.TextView_nickname = itemView.findViewById(R.id.TextView_nickname);
            this.TextView_msg= itemView.findViewById(R.id.TextView_msg);
            this.LinearLayout_main = itemView.findViewById(R.id.linear_layout_main);
        }
    }
}