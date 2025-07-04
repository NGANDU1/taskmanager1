package com.example.taskmanger;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> taskList;
    private boolean isTrashMode;

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
        this.isTrashMode = false; // can be set true in TrashBinActivity if needed
    }

    public void setTrashMode(boolean isTrash) {
        this.isTrashMode = isTrash;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        if (task != null) {
            holder.textTitle.setText(task.getTitle());
            holder.textDescription.setText(task.getDescription());

            // Combine date and time
            String dateTime = task.getDate() + " - " + task.getTime();
            holder.textDateTime.setText("Due: " + dateTime);

            // Edit task
            holder.editButton.setVisibility(isTrashMode ? View.GONE : View.VISIBLE); // Hide edit button in trash
            holder.editButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditTaskActivity.class);
                intent.putExtra("id", task.getId());
                intent.putExtra("title", task.getTitle());
                intent.putExtra("description", task.getDescription());
                intent.putExtra("date", task.getDate());
                intent.putExtra("time", task.getTime());
                context.startActivity(intent);
            });

            // View task when item is clicked
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ViewTaskActivity.class);
                intent.putExtra("id", task.getId());
                intent.putExtra("title", task.getTitle());
                intent.putExtra("description", task.getDescription());
                intent.putExtra("date", task.getDate());
                intent.putExtra("time", task.getTime());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    // Move task to trash (soft delete)
    public void deleteTask(int position) {
        Task taskToDelete = taskList.get(position);

        // Soft delete: update database
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.deleteTask(taskToDelete.getId());

        // Remove from current view
        taskList.remove(position);
        notifyItemRemoved(position);
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textDescription, textDateTime;
        ImageButton editButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textViewTitle);
            textDescription = itemView.findViewById(R.id.textViewDescription);
            textDateTime = itemView.findViewById(R.id.textViewDate);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}
