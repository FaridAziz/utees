package org.kuliah.utees.adapter;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.kuliah.utees.DetailActivity;
import org.kuliah.utees.R;
import org.kuliah.utees.model.Request;

import java.util.ArrayList;

public class RequestAdapterRecycleview extends RecyclerView.Adapter<RequestAdapterRecycleview.MyViewHolder> {

    ArrayList<Request> moviesList;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef = db.getReference("Request");
    private Activity mActivity;
    Button delete;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout rl_layout;
        public TextView item_nama, item_email, item_telepon;
        public ImageView item_foto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rl_layout = itemView.findViewById(R.id.rl_layout);
            item_nama = itemView.findViewById(R.id.item_nama);
            item_email = itemView.findViewById(R.id.item_email);
            item_telepon = itemView.findViewById(R.id.item_telepon);
            item_foto = itemView.findViewById(R.id.item_foto);

            mDatabaseRef = FirebaseDatabase.getInstance().getReference();

            delete = itemView.findViewById(R.id.detail);

//            delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    mDatabaseRef.child("Request")
//                            .removeValue();
//                }
//            });


        }
    }

    public RequestAdapterRecycleview(ArrayList<Request> moviesList){
        this.moviesList = moviesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Request movie = moviesList.get(position);

        holder.item_nama.setText(movie.getNama());
        holder.item_email.setText(movie.getEmail());
        holder.item_telepon.setText(movie.getTelepon());
        Picasso.get().load(movie.getImageUrl()).into(holder.item_foto);


        holder.rl_layout.setOnClickListener((view) -> {
            Intent goDetail = new Intent(holder.rl_layout.getContext(), DetailActivity.class);

            goDetail.putExtra("id", movie.getKey());
            goDetail.putExtra("nama", movie.getNama());
            goDetail.putExtra("email", movie.getEmail());
            goDetail.putExtra("telepon", movie.getTelepon());
            goDetail.putExtra("imageUrl", movie.getImageUrl());

            holder.rl_layout.getContext().startActivity(goDetail);
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public void filterList(ArrayList<Request> filteredList){
        moviesList = filteredList;
        notifyDataSetChanged();
    }

}