package com.efrei.brainbow.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.efrei.brainbow.Model.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UsersManager";
    private static final String TABLE_USERS = "Users";
    private static final String KEY_ID = "Id";
    private static final String KEY_USERNAME = "Username";
    private static final String KEY_PWD = "Pwd";
    private static final String KEY_QUIZ_CATEGORY = "QuizCategory";
    private static final String KEY_QUIZ_CURRENT_SCORE = "QuizCurrentScore";
    private static final String KEY_QUIZ_GOAL = "QuizGoal";
    private static final String KEY_RUN_CURRENT_DISTANCE = "RunCurrentDistance";
    private static final String KEY_RUN_GOAL = "RunGoal";
    private static final String KEY_DEADLINE = "Deadline";

    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USERNAME + " TEXT, "
                + KEY_PWD + " TEXT, "
                + KEY_QUIZ_CATEGORY + " INTEGER, "
                + KEY_QUIZ_CURRENT_SCORE + " INTEGER, "
                + KEY_QUIZ_GOAL + " INTEGER, "
                + KEY_RUN_CURRENT_DISTANCE + " INTEGER, "
                + KEY_RUN_GOAL + " INTEGER, "
                + KEY_DEADLINE + " DATE" + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Create tables again
        onCreate(db);
    }

    // code to add the new user
    int addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_PWD, user.getPwd());
        values.put(KEY_QUIZ_CATEGORY, user.getQuizCategory());
        values.put(KEY_QUIZ_CURRENT_SCORE, user.getQuizCurrentScore());
        values.put(KEY_QUIZ_GOAL, user.getQuizGoal());
        values.put(KEY_RUN_CURRENT_DISTANCE, user.getRunCurrentDistance());
        values.put(KEY_RUN_GOAL, user.getRunGoal());
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        values.put(KEY_DEADLINE, dateFormat.format(user.getDeadline()));

        // Inserting Row
        return (int) db.insert(TABLE_USERS, null, values);
    }

    // code to get the single user
    User getUser(int id) throws ParseException {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_ID,
                        KEY_USERNAME,
                        KEY_PWD,
                        KEY_QUIZ_CATEGORY,
                        KEY_QUIZ_CURRENT_SCORE,
                        KEY_QUIZ_GOAL,
                        KEY_RUN_CURRENT_DISTANCE,
                        KEY_RUN_GOAL,
                        KEY_DEADLINE
                }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();


        User user = new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                Integer.parseInt(cursor.getString(3)),
                Integer.parseInt(cursor.getString(4)),
                Integer.parseInt(cursor.getString(5)),
                Integer.parseInt(cursor.getString(6)),
                Integer.parseInt(cursor.getString(7)),
                formatter.parse(cursor.getString(8)));
        // return user
        return user;
    }

    // code to get all users in a list view
    public List<User> getAllUsers() throws ParseException {
        List<User> userList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setUsername(cursor.getString(1));
                user.setPwd(cursor.getString(2));
                user.setQuizCategory(Integer.parseInt(cursor.getString(3)));
                user.setQuizCurrentScore(Integer.parseInt(cursor.getString(4)));
                user.setQuizGoal(Integer.parseInt(cursor.getString(5)));
                user.setRunCurrentDistance(Integer.parseInt(cursor.getString(6)));
                user.setRunGoal(Integer.parseInt(cursor.getString(7)));
                user.setDeadline(formatter.parse(cursor.getString(8)));
                // Adding user to list
                userList.add(user);
            } while (cursor.moveToNext());
        }

        // return user list
        return userList;
    }

    // code to update the single user
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_PWD, user.getPwd());
        values.put(KEY_QUIZ_CATEGORY, user.getQuizCategory());
        values.put(KEY_QUIZ_CURRENT_SCORE, user.getQuizCurrentScore());
        values.put(KEY_QUIZ_GOAL, user.getQuizGoal());
        values.put(KEY_RUN_CURRENT_DISTANCE, user.getRunCurrentDistance());
        values.put(KEY_RUN_GOAL, user.getRunGoal());
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        values.put(KEY_DEADLINE, dateFormat.format(user.getDeadline()));
        // updating row
        db.update(TABLE_USERS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getId()) });
    }

    // Deleting single user
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getId()) });
        db.close();
    }
}