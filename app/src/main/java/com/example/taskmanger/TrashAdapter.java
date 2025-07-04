package com.example.taskmanger;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TrashAdapter extends RecyclerView.Adapter<TrashAdapter.TrashViewHolder> {

    private Context context;
    private List<Task> deletedTasks;
    private DatabaseHelper dbHelper;

    public TrashAdapter(Context context, List<Task> deletedTasks, DatabaseHelper dbHelper) {
        this.context = context;
        this.deletedTasks = deletedTasks;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public TrashViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trash_task, parent, false);
        return new TrashViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrashViewHolder holder, int position) {
        Task task = deletedTasks.get(position);

        holder.textTitle.setText(task.getTitle());
        holder.textDescription.setText(task.getDescription());
        holder.textDate.setText(task.getDate());
        holder.textTime.setText(task.getTime());

        // Restore button
        holder.btnRestore.setOnClickListener(v -> {
            boolean restored = dbHelper.restoreTask(task.getId());
            if (restored) {
                deletedTasks.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Task restored", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to restore task", Toast.LENGTH_SHORT).show();
            }
        });

        // Permanently delete button with confirmation
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Permanently")
                    .setMessage("Are you sure you want to permanently delete this task?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        boolean deleted = dbHelper.permanentlyDeleteTask(task.getId());
                        if (deleted) {
                            deletedTasks.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Task permanently deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to delete task", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return deletedTasks.size();
    }

    public static class TrashViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textDescription, textDate, textTime;
        Button btnRestore, btnDelete;

        public TrashViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTrashTitle);
            textDescription = itemView.findViewById(R.id.textTrashDescription);
            textDate = itemView.findViewById(R.id.textTrashDate);
            textTime = itemView.findViewById(R.id.textTrashTime);
            btnRestore = itemView.findViewById(R.id.btnRestore);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
