package com.example.who_nextdoor.BoardRecycler;

import android.content.Context;
import android.graphics.Color;
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

import com.example.who_nextdoor.MessageDataInfo;
import com.example.who_nextdoor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.BoardViewHolder> {
    private ArrayList<MessageDataInfo> arrayList;

    private Context context;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userid = user.getUid();
    int position2;
    String receiveruid;

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


    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mes_items,parent,false);
        BoardViewHolder holder = new BoardViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        position2 = position;

        MessageDataInfo mdata = arrayList.get(position);

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://nextdoor-97fe5.appspot.com");
        StorageReference pathReference = storageReference.child("m_board");


        holder.tv_nick.setText("옆집 친구");
        holder.tv_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        holder.tv_nick.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        holder.tv_nick.setTextColor(Color.parseColor("#1E90FF"));
        holder.LinearLayout_msg.setGravity(Gravity.LEFT);
        holder.tv_msg.setText(arrayList.get(position).getContents());

        holder.tv_msg.setText(arrayList.get(position).getContents());
    }


    public class BoardViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nick, tv_msg;
        LinearLayout LinearLayout_msg;

        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_nick = itemView.findViewById(R.id.mes_name);
            this.tv_msg = itemView.findViewById(R.id.mes_content);
            this.LinearLayout_msg = itemView.findViewById(R.id.linear_mesitems_main);
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

    public MessageDataInfo getmsginfo(int pos){
        return arrayList.get(pos);
    }


    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }



}