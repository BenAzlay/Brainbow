package com.efrei.brainbow.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.VoiceInteractor;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.efrei.brainbow.Model.Quiz;
import com.efrei.brainbow.Model.User;
import com.efrei.brainbow.R;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView questionText;
    private Button answer1;
    private Button answer2;
    private Button answer3;
    private Button answer4;
    private Quiz quiz;
    private Gson json = new Gson();

    private int questionIndex;
    private int numberOfQuestion;
    private int score = 0;
    private String currentQuestion;
    private List<String> currentAnswers  = new ArrayList<>();
    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";

    private User currentUser;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        questionText = (TextView) findViewById(R.id.activity_game_question_text);
        answer1 = (Button) findViewById(R.id.activity_game_answer1_btn);
        answer2 = (Button) findViewById(R.id.activity_game_answer2_btn);
        answer3 = (Button) findViewById(R.id.activity_game_answer3_btn);
        answer4 = (Button) findViewById(R.id.activity_game_answer4_btn);
        // Use the same listener for the four buttons.
        // The tag value will be used to distinguish the button triggered
        answer1.setOnClickListener(this);
        answer2.setOnClickListener(this);
        answer3.setOnClickListener(this);
        answer4.setOnClickListener(this);
        // Use the tag property to 'name' the buttons
        answer1.setTag(0);
        answer2.setTag(1);
        answer3.setTag(2);
        answer4.setTag(3);

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

        numberOfQuestion = 10;
        questionIndex = 0;

        //API CONNECTION
        String url = "https://opentdb.com/api.php?amount=" + numberOfQuestion + "&category=" + currentUser.getQuizCategory() + "&type=multiple";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Rest response",response.toString());

                        quiz = json.fromJson(response.toString(), Quiz.class); //Loads Quiz object from JSON
                        Log.e("First question:", quiz.getResults().get(questionIndex).getCorrect_answer());
                        displayQuestion();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest response",error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);


    }

    @Override
    public void onClick(View v) {
        //int responseIndex = (int) v.getTag();
        Button button = (Button)v;
        //Log.e("Answer selected:", Integer.toString(responseIndex));
        if(quiz.getResults().get(questionIndex).getCorrect_answer().equals(button.getText().toString())){
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            score++;
        }
        else Toast.makeText(this, "Wrong! it was: " + quiz.getResults().get(questionIndex).getCorrect_answer(), Toast.LENGTH_SHORT).show();
        currentAnswers.clear();
        questionIndex++;
        displayQuestion();
    }

    public void displayQuestion(){
        if(questionIndex >= numberOfQuestion){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("You have " + score + "/10 good answers!")
                    .setMessage("Retry another time to finish your goal")
                    .setPositiveButton("Go back to main page", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.putExtra(BUNDLE_EXTRA_SCORE, score); //Affecting to score the key BUNDLE_EXTRA_SCORE
                            setResult(RESULT_OK, intent); //Confirm to Android our activity is terminated and indicating our intent
                            finish();
                        }
                    })
                    .create()
                    .show();
        }
        else if(currentUser.getQuizCurrentScore() + score >= currentUser.getQuizGoal()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Goal accomplished!")
                    .setMessage("You can now set a higher goal")
                    .setPositiveButton("Go back to main page", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.putExtra(BUNDLE_EXTRA_SCORE, score); //Affecting to score the key BUNDLE_EXTRA_SCORE
                            setResult(RESULT_OK, intent); //Confirm to Android our activity is terminated and indicating our intent
                            finish();
                        }
                    })
                    .create()
                    .show();
        }
        else{ //Set next question
            currentQuestion = quiz.getResults().get(questionIndex).getQuestion();
            questionText.setText(currentQuestion);

            currentAnswers.add(quiz.getResults().get(questionIndex).getCorrect_answer());
            currentAnswers.addAll(quiz.getResults().get(questionIndex).getIncorrect_answers());
            Collections.shuffle(currentAnswers);
            answer1.setText(currentAnswers.get(0));
            answer2.setText(currentAnswers.get(1));
            answer3.setText(currentAnswers.get(2));
            answer4.setText(currentAnswers.get(3));
        }

    }
}
