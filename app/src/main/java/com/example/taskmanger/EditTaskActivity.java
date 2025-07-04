package com.example.taskmanger;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditTaskActivity extends AppCompatActivity {

    EditText editTitle, editDescription, editDate, editTime;
    Button updateButton;

    int taskId = -1; // Default if ID not found

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        editDate = findViewById(R.id.editDate);
        editTime = findViewById(R.id.editTime);
        updateButton = findViewById(R.id.updateButton);

        // Get task data from intent
        Intent intent = getIntent();
        taskId = intent.getIntExtra("id", -1);
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");

        // Set current values
        editTitle.setText(title);
        editDescription.setText(description);
        editDate.setText(date);
        editTime.setText(time);

        updateButton.setOnClickListener(v -> {
            String newTitle = editTitle.getText().toString();
            String newDescription = editDescription.getText().toString();
            String newDate = editDate.getText().toString();
            String newTime = editTime.getText().toString();

            // Update task in DB
            DatabaseHelper dbHelper = new DatabaseHelper(EditTaskActivity.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("title", newTitle);
            values.put("description", newDescription);
            values.put("date", newDate);
            values.put("time", newTime);

            db.update("tasks", values, "id=?", new String[]{String.valueOf(taskId)});
            db.close();

            // Go back
            finish();
        });
    }
}
