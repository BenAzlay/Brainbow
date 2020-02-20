package com.efrei.brainbow.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.efrei.brainbow.Model.User;
import com.efrei.brainbow.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SettingsActivity extends AppCompatActivity {
    private EditText quizGoalField;
    private EditText runGoalField;
    private DatePicker picker;
    private Button resetButton;
    private Button validateButton;

    private DatabaseHandler db;
    private User currentUser;

    public static final String BUNDLE_EXTRA_RUN_GOAL = "BUNDLE_EXTRA_RUN_GOAL";
    public static final String BUNDLE_EXTRA_QUIZ_GOAL = "BUNDLE_EXTRA_QUIZ_GOAL";
    public static final String BUNDLE_EXTRA_RESET = "BUNDLE_EXTRA_RESET";
    public static final String BUNDLE_EXTRA_DEADLINE = "BUNDLE_EXTRA_DEADLINE";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        picker = (DatePicker)findViewById(R.id.datePicker);
        quizGoalField = (EditText)findViewById(R.id.quizGoalField);
        runGoalField = (EditText)findViewById(R.id.runGoalField);
        resetButton = (Button) findViewById(R.id.resetButton);
        validateButton = (Button) findViewById(R.id.validateButton);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);

                // Set a title for alert dialog
                builder.setTitle("Reset scores");

                // Ask the final question
                builder.setMessage("Are you sure to reset all your data?");

                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setGoals(true);
                    }
                });

                // Set the alert dialog no button click listener
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
                        Toast.makeText(getApplicationContext(),
                                "Data kept",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();
            }
        });

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGoals(false);
            }
        });
    }

    public void setGoals(Boolean reset){
        Log.e("quizGoalField.getText().toString()=", quizGoalField.getText().toString());
        Intent intent = new Intent();
        intent.putExtra(BUNDLE_EXTRA_RESET, reset);
        Calendar currentCalendar = Calendar.getInstance();
        int day = picker.getDayOfMonth();
        int month = picker.getMonth();
        int year =  picker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);


        if(quizGoalField.getText().toString().equals("") || runGoalField.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),
                    "Please enter goals",Toast.LENGTH_SHORT).show();
        }else if (currentCalendar.compareTo(calendar) >= 0){ //If deadline not in future
            Toast.makeText(getApplicationContext(),
                    "Please enter a deadline in the future",Toast.LENGTH_SHORT).show();
        } else{

            intent.putExtra(BUNDLE_EXTRA_QUIZ_GOAL, Integer.parseInt(quizGoalField.getText().toString()));
            intent.putExtra(BUNDLE_EXTRA_RUN_GOAL, Integer.parseInt(runGoalField.getText().toString()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            intent.putExtra(BUNDLE_EXTRA_DEADLINE, sdf.format(calendar.getTime()));
            setResult(RESULT_OK, intent);
            finish();
        }


    }
}
