package com.example.taskmanger;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {

    EditText editTextTitle, editTextDescription;
    Button buttonPickDate, buttonPickTime, buttonSave;
    Calendar selectedDateTime;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        dbHelper = new DatabaseHelper(this);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonPickDate = findViewById(R.id.buttonPickDate);
        buttonPickTime = findViewById(R.id.buttonPickTime);
        buttonSave = findViewById(R.id.buttonSave);

        selectedDateTime = Calendar.getInstance();

        buttonPickDate.setOnClickListener(v -> {
            int year = selectedDateTime.get(Calendar.YEAR);
            int month = selectedDateTime.get(Calendar.MONTH);
            int day = selectedDateTime.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(this, (view, y, m, d) -> {
                selectedDateTime.set(Calendar.YEAR, y);
                selectedDateTime.set(Calendar.MONTH, m);
                selectedDateTime.set(Calendar.DAY_OF_MONTH, d);
            }, year, month, day).show();
        });

        buttonPickTime.setOnClickListener(v -> {
            int hour = selectedDateTime.get(Calendar.HOUR_OF_DAY);
            int minute = selectedDateTime.get(Calendar.MINUTE);

            new TimePickerDialog(this, (view, h, m) -> {
                selectedDateTime.set(Calendar.HOUR_OF_DAY, h);
                selectedDateTime.set(Calendar.MINUTE, m);
                selectedDateTime.set(Calendar.SECOND, 0);
            }, hour, minute, true).show();
        });

        buttonSave.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString();
            String description = editTextDescription.getText().toString();
            String date = selectedDateTime.get(Calendar.YEAR) + "-" +
                    (selectedDateTime.get(Calendar.MONTH) + 1) + "-" +
                    selectedDateTime.get(Calendar.DAY_OF_MONTH);
            String time = selectedDateTime.get(Calendar.HOUR_OF_DAY) + ":" +
                    selectedDateTime.get(Calendar.MINUTE);

            // Get reminder time (the full timestamp in milliseconds)
            long reminderTime = selectedDateTime.getTimeInMillis();

            // Insert task with reminder time
            dbHelper.insertTask(title, description, date, time, reminderTime);

            // Schedule the alarm with reminder time
            scheduleAlarm(title, description, selectedDateTime);

            // Finish the activity
            finish();
        });
    }

    private void scheduleAlarm(String title, String message, Calendar calendar) {
        // Create an intent to be triggered when the alarm goes off
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("message", message);

        // Create a PendingIntent to send when the alarm goes off
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                (int) System.currentTimeMillis(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Get AlarmManager and schedule the alarm
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}
