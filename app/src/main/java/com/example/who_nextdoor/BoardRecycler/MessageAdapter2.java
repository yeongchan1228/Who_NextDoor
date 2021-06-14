package com.example.who_nextdoor.BoardRecycler;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.who_nextdoor.MessageDataInfo2;
import com.example.who_nextdoor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MessageAdapter2 extends RecyclerView.Adapter<MessageAdapter2.BoardViewHolder> {
    private ArrayList<MessageDataInfo2> arrayList;

    private Context context;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userid = user.getUid();
    int position2;
    String receiveruid;

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }
    private MessageAdapter2.OnItemClickListener mListener = null;

    public MessageAdapter2(ArrayList<MessageDataInfo2> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent,false);
        BoardViewHolder holder = new BoardViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        position2 = position;

        MessageDataInfo2 mdata = arrayList.get(position);

        if(arrayList.get(position).getboard_image().equals("F")) {
            holder.imageView.setImageBitmap(null);
            holder.imageView.setVisibility(View.GONE);
        }

        else {
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://nextdoor-97fe5.appspot.com");
            StorageReference pathReference = storageReference.child("m_board");

            if (pathReference != null) {
                StorageReference submitimage = storageReference.child("m_board/" + mdata.getUid() +"/"+ arrayList.get(position).getDate() + ".png");
                submitimage.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            if(arrayList.get(position2).getboard_image().equals("T")) {
                                Glide.with(holder.itemView).load(task.getResult()).into(holder.imageView);
                            }
                        }
                    }
                });
            }
        }


        if(mdata.getUid().equals(userid)) {
            holder.tv_nick.setText("나");
            holder.tv_nick.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.tv_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_END);
            holder.tv_nick.setTextColor(Color.parseColor("#FFD700"));
            holder.LinearLayout_msg.setGravity(Gravity.RIGHT);
        }
        else {
            holder.tv_nick.setText("옆집 친구");
            holder.tv_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.tv_nick.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_START);
            holder.tv_nick.setTextColor(Color.parseColor("#1E90FF"));
            holder.LinearLayout_msg.setGravity(Gravity.LEFT);
        }

        holder.tv_msg.setText(arrayList.get(position).getContents());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class BoardViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv_nick, tv_msg;
        LinearLayout LinearLayout_msg;

        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_nick = itemView.findViewById(R.id.message_nickname);
            this.tv_msg = itemView.findViewById(R.id.message_msg);
            this.imageView = itemView.findViewById(R.id.message_image);
            this.LinearLayout_msg = itemView.findViewById(R.id.linear_message_main);
        }
    }
}