package com.example.ecofriendly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecofriendly.R;
import com.example.ecofriendly.data.models.UserBage;

import java.util.List;

public class BagesAdapter extends RecyclerView.Adapter<BagesAdapter.ViewHolder> {

    List<UserBage> userBageList;
    Context context;

    @NonNull
    @Override
    public BagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bage_ui, parent);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BagesAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
