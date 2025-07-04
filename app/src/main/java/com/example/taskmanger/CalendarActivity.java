package com.example.taskmanger;

import android.os.Bundle;
import android.widget.CalendarView;
import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar); // Set layout with CalendarView

        CalendarView calendarView = findViewById(R.id.calendarView);
        // Optional: You can add listeners for date changes if needed
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Handle date change if necessary
        });
    }
}
