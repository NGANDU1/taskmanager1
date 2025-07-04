package com.example.taskmanger;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ViewTaskActivity extends AppCompatActivity {

    private TextView textTitle, textDescription, textDateTime;
    private Button buttonEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        textTitle = findViewById(R.id.textTitle);
        textDescription = findViewById(R.id.textDescription);
        textDateTime = findViewById(R.id.textDateTime);
        buttonEdit = findViewById(R.id.buttonEdit);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");

        textTitle.setText(title);
        textDescription.setText(description);
        textDateTime.setText(getString(R.string.due_text, date, time));

        buttonEdit.setOnClickListener(v -> {
            Intent editIntent = new Intent(ViewTaskActivity.this, EditTaskActivity.class);
            editIntent.putExtra("id", id);
            editIntent.putExtra("title", title);
            editIntent.putExtra("description", description);
            editIntent.putExtra("date", date);
            editIntent.putExtra("time", time);
            startActivity(editIntent);
            finish();
        });
    }
}
