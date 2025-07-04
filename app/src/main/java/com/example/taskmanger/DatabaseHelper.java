package com.example.taskmanger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "tasks.db";
    public static final int DB_VERSION = 2; // updated version

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "description TEXT, " +
                "date TEXT, " +
                "time TEXT, " +
                "reminderTime INTEGER, " +
                "is_deleted INTEGER DEFAULT 0)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE tasks ADD COLUMN reminderTime INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE tasks ADD COLUMN is_deleted INTEGER DEFAULT 0");
        }
    }

    // Insert Task
    public long insertTask(String title, String description, String date, String time, long reminderTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("date", date);
        values.put("time", time);
        values.put("reminderTime", reminderTime);
        values.put("is_deleted", 0);
        return db.insert("tasks", null, values);
    }

    // Get Active Tasks (not deleted)
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tasks WHERE is_deleted = 0", null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                task.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                task.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                task.setTime(cursor.getString(cursor.getColumnIndexOrThrow("time")));
                task.setReminderTime(cursor.getLong(cursor.getColumnIndexOrThrow("reminderTime")));
                tasks.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return tasks;
    }

    // Get Deleted Tasks (Trash)
    public List<Task> getDeletedTasks() {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tasks WHERE is_deleted = 1", null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                task.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                task.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                task.setTime(cursor.getString(cursor.getColumnIndexOrThrow("time")));
                task.setReminderTime(cursor.getLong(cursor.getColumnIndexOrThrow("reminderTime")));
                tasks.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return tasks;
    }

    // Soft delete: move task to trash
    public boolean deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_deleted", 1);
        int result = db.update("tasks", values, "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // Restore task from trash
    public boolean restoreTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_deleted", 0);
        int result = db.update("tasks", values, "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // Permanently delete task
    public boolean permanentlyDeleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("tasks", "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // Get tasks by date (excluding deleted tasks)
    public List<Task> getTasksForDate(String date) {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tasks WHERE date = ? AND is_deleted = 0", new String[]{date});

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                task.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                task.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                task.setTime(cursor.getString(cursor.getColumnIndexOrThrow("time")));
                task.setReminderTime(cursor.getLong(cursor.getColumnIndexOrThrow("reminderTime")));
                tasks.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return tasks;
    }

    // Update Task
    public int updateTask(int id, String title, String description, String date, String time, long reminderTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("date", date);
        values.put("time", time);
        values.put("reminderTime", reminderTime);
        return db.update("tasks", values, "id=?", new String[]{String.valueOf(id)});
    }
}
