package com.example.ecofriendly.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecofriendly.R;
import com.example.ecofriendly.data.models.Badge;
import com.example.ecofriendly.data.models.UserBadge;

import java.util.List;

public class BadgesAdapter extends RecyclerView.Adapter<BadgesAdapter.ViewHolder> {

    private List<Badge> userBageList;
    private Context context;

    public BadgesAdapter(List<Badge> userBageList, Context context) {
        this.userBageList = userBageList;
        this.context = context;
    }

    @NonNull
    @Override
    public BadgesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bage_ui, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BadgesAdapter.ViewHolder holder, int position) {
        Badge badge = userBageList.get(position);

        String nameBadge = badge.getImageName();
        @SuppressLint("DiscouragedApi") int resId = holder.itemView.getContext()
                .getResources()
                .getIdentifier(nameBadge, "drawable", holder.itemView.getContext().getPackageName());
        if (resId != 0) {
            holder.icon.setImageResource(resId);
        }

        holder.title.setText(badge.getTitle());
        holder.description.setText(badge.getDescription());

    }

    @Override
    public int getItemCount() {
        return userBageList.size();
    }

    public void updateList(List<Badge> newList) {
        userBageList.clear();
        userBageList.addAll(newList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView title, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
        }
    }
}
