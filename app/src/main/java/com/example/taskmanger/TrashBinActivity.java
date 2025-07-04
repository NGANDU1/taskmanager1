package com.example.taskmanger;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TrashBinActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTrash;
    private TrashAdapter trashAdapter;
    private List<Task> deletedTasks;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash_bin);

        recyclerViewTrash = findViewById(R.id.recyclerViewTrash);
        recyclerViewTrash.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        deletedTasks = dbHelper.getDeletedTasks();

        if (deletedTasks.isEmpty()) {
            Toast.makeText(this, "Trash is empty", Toast.LENGTH_SHORT).show();
        }

        trashAdapter = new TrashAdapter(this, deletedTasks, dbHelper);
        recyclerViewTrash.setAdapter(trashAdapter);
    }
}
