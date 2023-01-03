package com.example.pennpaper.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pennpaper.HomeActivity;
import com.example.pennpaper.MainActivity;
import com.example.pennpaper.R;
import com.example.pennpaper.entity.DataDetails;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

    Context context;
    ArrayList<DataDetails> list= new ArrayList<>();
    HomeActivity homeActivity;

    public RecycleAdapter(Context context, ArrayList<DataDetails> list,HomeActivity homeActivity) {
        this.context = context;
        this.list = list;
        this.homeActivity = homeActivity;
    }

    @NonNull
    @Override
    public RecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_recycle_layout,parent,false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapter.ViewHolder holder, int position) {

        DataDetails dataDetails = list.get(position);

        holder.title.setText(dataDetails.getTitle());
        holder.date.setText(dataDetails.getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    homeActivity.addAndEditData(true,dataDetails,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.titleRV);
            date = itemView.findViewById(R.id.dateRV);
        }
    }
}
