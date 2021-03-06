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
import com.example.who_nextdoor.R;
import com.example.who_nextdoor.TradeInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.BreakIterator;
import java.util.ArrayList;

public class TradeAdapter extends RecyclerView.Adapter<TradeAdapter.BoardViewHolder> {
    private ArrayList<TradeInfo> arrayList;
    private Context context;
    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }
    private TradeAdapter.OnItemClickListener mListener = null;
    public void setOnItemClickListener(TradeAdapter.OnItemClickListener listener){
        this.mListener = listener;
    }
    public TradeAdapter(ArrayList<TradeInfo> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }
    public TradeInfo gettradeinfo(int pos){
        return arrayList.get(pos);
    }
    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.t_boarditems,parent,false);
        BoardViewHolder holder = new BoardViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        if(arrayList.get(position).getboard_image().equals("F")){
            holder.imageView.setImageResource(R.drawable.basic);
        }
        else {
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://nextdoor-97fe5.appspot.com");
            StorageReference pathReference = storageReference.child("t_board");
            if(pathReference != null){
                StorageReference submitimage = storageReference.child("t_board/"+arrayList.get(position).getTitle()+".png");
                submitimage.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Glide.with(holder.itemView).load(task.getResult()).into(holder.imageView);
                        }
                    }
                });
            }
        }
        holder.tv_title.setText("????" + arrayList.get(position).getTitle());
        holder.tv_price.setText("????" + arrayList.get(position).getPrice() + "???");
        //holder.tv_content.setText(arrayList.get(position).getContents());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class BoardViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv_title, tv_price;

        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.tb_imageView);
            this.tv_title = itemView.findViewById(R.id.tb_title);
            this.tv_price = itemView.findViewById(R.id.tb_price);
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