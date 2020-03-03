package com.efrei.brainbow.Controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.efrei.brainbow.Model.User;
import com.efrei.brainbow.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private static final int QUIZ_ACTIVITY_REQUEST_CODE = 10;
    private static final int RUN_ACTIVITY_REQUEST_CODE = 11;
    private static final int SETTINGS_ACTIVITY_REQUEST_CODE = 12;
    public static final String PREF_KEY_QUIZ_SCORE = "PREF_KEY_QUIZ_SCORE";
    public static final String PREF_KEY_QUIZ_GOAL = "PREF_KEY_QUIZ_GOAL";
    public static final String PREF_KEY_RUN_GOAL = "PREF_KEY_RUN_GOAL";
    public static final String PREF_KEY_RUN_SCORE = "PREF_KEY_RUN_SCORE";
    public static final String PREF_KEY_DEADLINE = "PREF_KEY_DEADLINE";
    private TextView greetingText;
    private TextView quizScoreLabel;
    private TextView runScoreLabel;
    private TextView deadlineLabel;
    private Button quizButton;
    private Button settingsButton;
    private Button runButton;
    private Button aboutButton;
    private User currentUser;
    private int id;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        greetingText = (TextView) findViewById(R.id.activity_main_greeting_txt);
        deadlineLabel = (TextView) findViewById(R.id.deadlineLabel);
        quizButton = (Button) findViewById(R.id.activity_main_play_btn);
        settingsButton = (Button) findViewById(R.id.settingsButton);
        runButton = (Button) findViewById(R.id.runButton);
        aboutButton = (Button) findViewById(R.id.aboutButton);
        quizScoreLabel = (TextView) findViewById(R.id.quizScoreLabel);
        runScoreLabel = (TextView) findViewById(R.id.runScoreLabel);



        //Get current user
        db = new DatabaseHandler(this);
        Bundle b = getIntent().getExtras();
        id = 1;
        if(b != null)
            id = b.getInt("userID");
        try {
            currentUser = db.getUser(id);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        checkDeadline();

        setLabels();

        preferences = getPreferences(MODE_PRIVATE);

        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent quizActivity = new Intent(MainActivity.this, QuizActivity.class);
                Bundle b = new Bundle();
                b.putInt("userID", currentUser.getId());
                quizActivity.putExtras(b);
                startActivityForResult(quizActivity, QUIZ_ACTIVITY_REQUEST_CODE);
            }
        });
        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent runActivity = new Intent(MainActivity.this, RunActivity.class);
                Bundle b = new Bundle();
                b.putInt("userID", currentUser.getId());
                runActivity.putExtras(b);
                startActivityForResult(runActivity, RUN_ACTIVITY_REQUEST_CODE);
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsActivity = new Intent(MainActivity.this, SettingsActivity.class);
                Bundle b = new Bundle();
                b.putInt("userID", currentUser.getId());
                settingsActivity.putExtras(b);
                startActivityForResult(settingsActivity, SETTINGS_ACTIVITY_REQUEST_CODE);
            }
        });
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutActivity = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(aboutActivity);
            }
        });
    }

    private void checkDeadline() {
        Date currentDate = new Date(System.currentTimeMillis());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(currentDate.compareTo(currentUser.getDeadline()) >= 0) {
            if(currentUser.getQuizCurrentScore() >= currentUser.getQuizGoal()
                    && currentUser.getRunCurrentDistance() >= currentUser.getRunGoal()){
                builder.setTitle("Congratulations!")
                        .setMessage("You have completed all your goals before the deadline")
                        .setPositiveButton("Set new goals and deadline", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent settingsActivity = new Intent(MainActivity.this, SettingsActivity.class);
                                Bundle b = new Bundle();
                                b.putInt("userID", currentUser.getId());
                                settingsActivity.putExtras(b);
                                startActivityForResult(settingsActivity, SETTINGS_ACTIVITY_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();
            } else {
                builder.setTitle("Too bad!")
                        .setMessage("You have NOT completed all your goals before the deadline")
                        .setPositiveButton("Set new goals and deadline", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent settingsActivity = new Intent(MainActivity.this, SettingsActivity.class);
                                Bundle b = new Bundle();
                                b.putInt("userID", currentUser.getId());
                                settingsActivity.putExtras(b);
                                startActivityForResult(settingsActivity, SETTINGS_ACTIVITY_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();
            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (QUIZ_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            // Fetch the score from the Intent
            int score = data.getIntExtra(QuizActivity.BUNDLE_EXTRA_SCORE, 0);

            preferences.edit().putInt(PREF_KEY_QUIZ_SCORE, score).apply();

            //Adding the score to those of previous sessions
            currentUser.setQuizCurrentScore(currentUser.getQuizCurrentScore() + preferences.getInt(PREF_KEY_QUIZ_SCORE, 0));
        }

        if (RUN_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            // Fetch the score from the Intent
            int score = data.getIntExtra(RunActivity.BUNDLE_EXTRA_DISTANCE, 0);

            preferences.edit().putInt(PREF_KEY_RUN_SCORE, score).apply();

            //Adding the score to those of previous sessions
            currentUser.setRunCurrentDistance(currentUser.getRunCurrentDistance() + preferences.getInt(PREF_KEY_RUN_SCORE, 0));
        }

        if (SETTINGS_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            // Fetch the score from the Intent
            Boolean reset = data.getBooleanExtra(SettingsActivity.BUNDLE_EXTRA_RESET, false);
            int quizGoal = data.getIntExtra(SettingsActivity.BUNDLE_EXTRA_QUIZ_GOAL, 12);
            int runGoal = data.getIntExtra(SettingsActivity.BUNDLE_EXTRA_RUN_GOAL, 3);
            int category = data.getIntExtra(SettingsActivity.BUNDLE_EXTRA_CATEGORY, 9);
            String sdeadline = data.getStringExtra(SettingsActivity.BUNDLE_EXTRA_DEADLINE);
            Log.e("Deadline:", sdeadline);
            try {
                Date deadline = new SimpleDateFormat("dd-MM-yyyy").parse(sdeadline);
                currentUser.setDeadline(deadline);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(reset){
                currentUser.setQuizCurrentScore(0);
                currentUser.setRunCurrentDistance(0);
            }

            currentUser.setQuizGoal(quizGoal);
            currentUser.setRunGoal(runGoal);
            currentUser.setQuizCategory(category);
        }
        db.updateUser(currentUser);
        setLabels();
    }

    private void setLabels() {
        quizScoreLabel.setText("Intellect score: " + currentUser.getQuizCurrentScore() + "/" + currentUser.getQuizGoal());
        runScoreLabel.setText("Physique score: " + currentUser.getRunCurrentDistance() + "/" + currentUser.getRunGoal() + " meters");
        deadlineLabel.setText("Deadline: " + sdf.format(currentUser.getDeadline()));
        greetingText.setText("Welcome " + currentUser.getUsername());
    }
}
