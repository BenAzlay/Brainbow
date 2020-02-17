package com.efrei.brainbow.Controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.efrei.brainbow.Model.User;
import com.efrei.brainbow.R;

import java.text.ParseException;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private static final int QUIZ_ACTIVITY_REQUEST_CODE = 10;
    public static final String PREF_KEY_QUIZ_SCORE = "PREF_KEY_QUIZ_SCORE";
    private TextView greetingText;
    private TextView quizScoreLabel;
    private Button playButton;
    private User currentUser;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        greetingText = (TextView) findViewById(R.id.activity_main_greeting_txt);
        playButton = (Button) findViewById(R.id.activity_main_play_btn);
        quizScoreLabel = (TextView) findViewById(R.id.quizScoreLabel);

        playButton.setEnabled(true);

        //Get current user
        db = new DatabaseHandler(this);
        Bundle b = getIntent().getExtras();
        int id = 1; // or other values
        if(b != null)
            id = b.getInt("userID");
        try {
            currentUser = db.getUser(id);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setLabels();

        preferences = getPreferences(MODE_PRIVATE);

        //Sets button's action
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent quizActivity = new Intent(MainActivity.this, QuizActivity.class);

                startActivityForResult(quizActivity, QUIZ_ACTIVITY_REQUEST_CODE);
            }
        });
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

            db.updateUser(currentUser);

            setLabels();
        }
    }

    private void setLabels() {
        quizScoreLabel.setText("Intellect score: " + currentUser.getQuizCurrentScore() + "/" + currentUser.getQuizGoal());
        greetingText.setText("Welcome " + currentUser.getUsername());

        playButton.setEnabled(true);
    }
}
