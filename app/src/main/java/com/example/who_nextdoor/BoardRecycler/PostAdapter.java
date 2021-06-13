package com.example.who_nextdoor.BoardRecycler;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.who_nextdoor.Activity.PostActivity;
import com.example.who_nextdoor.ComentsInfo;
import com.example.who_nextdoor.R;
import com.example.who_nextdoor.informationInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.BoardViewHolder> {
    private ArrayList<ComentsInfo> arrayList;
    SubcomentAdapter subcomentAdapter;
    private ArrayList subarray;
    private Context context;
    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private PostAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(PostAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }

    public PostAdapter(ArrayList<ComentsInfo> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public ComentsInfo getpostinfo(int pos) {
        return arrayList.get(pos);
    }

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_comentitem, parent, false);
        BoardViewHolder holder = new BoardViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://nextdoor-97fe5.appspot.com");
        if (storageReference != null) {
            StorageReference profileimage = storageReference.child("anonymous" + ".png");
            profileimage.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Glide.with(holder.coment_profile).load(task.getResult()).into(holder.coment_profile);
                    }
                }
            });
        }
        holder.post_nickname.setText("익명");
        holder.post_coments.setText(arrayList.get(position).getComents());
        holder.coment_date.setText(arrayList.get(position).getDate());
        holder.coment_send.setImageResource(R.drawable.coment_icon2);



    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class BoardViewHolder extends RecyclerView.ViewHolder {
        ImageView coment_profile, coment_send, if_sub;
        TextView post_nickname, post_coments, coment_date;

        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);
            this.coment_profile = itemView.findViewById(R.id.coment_profile);
            this.post_coments = itemView.findViewById(R.id.coment_coments);
            this.post_nickname = itemView.findViewById(R.id.coment_nickname);
            this.coment_date = itemView.findViewById(R.id.coment_date);
            this.coment_send = itemView.findViewById(R.id.coment_sendicon);
            this.if_sub = itemView.findViewById(R.id.coment_ifsub);

        }

    }
}