package com.example.ecofriendly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecofriendly.R;
import com.example.ecofriendly.data.models.Task;
import com.google.firestore.v1.Value;

import java.util.List;

public class TaskCardAdapters extends RecyclerView.Adapter<TaskCardAdapters.ViewHolder> {

    private List<Task> taskList;
    private Context context;
    private onTaskClickListener listener;

    public interface onTaskClickListener{
        void onTaskClick(Task task);
    }

    public TaskCardAdapters(List<Task> taskList, Context context, onTaskClickListener listener) {
        this.taskList = taskList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskCardAdapters.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ui_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskCardAdapters.ViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.title.setText(task.getTitle());
        holder.points.setText(String.valueOf(task.getPoints()));
        holder.shortDescription.setText(task.getShortDescription());

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void updateList(List<Task> newList) {
        taskList.clear();
        taskList.addAll(newList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, shortDescription, points;
        ImageButton details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            points = itemView.findViewById(R.id.points);
            shortDescription = itemView.findViewById(R.id.shortDescription);
            details = itemView.findViewById(R.id.details);

        }
    }
}
