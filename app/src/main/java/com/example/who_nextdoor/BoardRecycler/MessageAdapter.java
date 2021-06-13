package com.example.who_nextdoor.BoardRecycler;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.who_nextdoor.MessageDataInfo;
import com.example.who_nextdoor.R;
import com.example.who_nextdoor.informationInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.BoardViewHolder> {
    private ArrayList<MessageDataInfo> arrayList;
    private Context context;

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }
    private MessageAdapter.OnItemClickListener mListener = null;
    public void setOnItemClickListener(MessageAdapter.OnItemClickListener listener){
        this.mListener = listener;
    }
    public MessageAdapter(ArrayList<MessageDataInfo> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public MessageDataInfo getmsginfo(int pos){
        return arrayList.get(pos);
    }

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mes_items,parent,false);
        BoardViewHolder holder = new BoardViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        holder.tv_content.setText(arrayList.get(position).getContents());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class BoardViewHolder extends RecyclerView.ViewHolder {
        //ImageView imageView;
        TextView tv_content;
        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);
            //this.imageView = itemView.findViewById(R.id.mes_image);
            this.tv_content = itemView.findViewById(R.id.mes_content);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        mListener.onItemClick(v,position);
                    }
                }
            });
        }
    }
}