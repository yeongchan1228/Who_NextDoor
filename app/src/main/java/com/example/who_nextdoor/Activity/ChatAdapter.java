package com.example.who_nextdoor.Activity;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.who_nextdoor.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private List<ChatData> mDataset;
    private String myNickName;
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView TextView_nickname;
        public TextView TextView_msg;
        public View rootView;
        public MyViewHolder(View v) {
            super(v);
            //TextView_nickname = v.findViewById(R.id.textView_nickname);
            TextView_msg = v.findViewById(R.id.textView_msg);
            rootView = v;
        }

    }

    public ChatAdapter(List<ChatData> mydataset, Context context, String myNickname) {
        mDataset = mydataset;
        this.myNickName = myNickname;
    }

    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ChatData chat = mDataset.get(position);
        holder.TextView_nickname.setText(chat.getNickname());
        holder.TextView_msg.setText(chat.getMsg());

        if(chat.getNickname().equals(this.myNickName)) {
            holder.TextView_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        }
        else {
            holder.TextView_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

    public ChatData getChat(int position) {
        return mDataset != null ? mDataset.get(position) : null;
    }

    public void addChat(ChatData chat) {
        mDataset.add(chat);
        notifyItemInserted(mDataset.size()-1);
    }
}
