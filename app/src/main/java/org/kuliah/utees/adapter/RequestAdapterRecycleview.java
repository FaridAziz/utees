package org.kuliah.utees.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.kuliah.utees.R;
import org.kuliah.utees.model.Request;

import java.util.List;

public class RequestAdapterRecycleview extends RecyclerView.Adapter<RequestAdapterRecycleview.MyViewHolder> {

    private List<Request> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout rl_layout;
        public TextView item_nama, item_email, item_telepon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rl_layout = itemView.findViewById(R.id.rl_layout);
            item_nama = itemView.findViewById(R.id.item_nama);
            item_email = itemView.findViewById(R.id.item_email);
            item_telepon = itemView.findViewById(R.id.item_telepon);
        }
    }

    public RequestAdapterRecycleview(List<Request> moviesList){
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
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

}
