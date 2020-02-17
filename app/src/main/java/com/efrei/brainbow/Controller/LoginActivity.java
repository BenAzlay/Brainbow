package com.efrei.brainbow.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.efrei.brainbow.Model.User;
import com.efrei.brainbow.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText pwdInput;
    private Button loginButton;
    private Button signUpButton;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = (EditText) findViewById(R.id.username);
        pwdInput = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login);
        signUpButton = (Button) findViewById(R.id.signUp);

        db = new DatabaseHandler(this);

        //Button enabled only when both credentials are entered
        usernameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pwdInput.setEnabled(s.toString().length() != 0); //Enable button if the user has entered something
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pwdInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginButton.setEnabled(s.toString().length() != 0); //Enable button if the user has entered something
                signUpButton.setEnabled(s.toString().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Sets Sign Up action
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);//Go to MainActivity
                Calendar c = Calendar.getInstance();
                c.setTime(new Date()); // Now use today date.
                c.add(Calendar.DATE, 7); // Adding one week
                Date date =  c.getTime();
                int id = db.addUser(new User(usernameInput.getText().toString(),
                        pwdInput.getText().toString(),
                        9, 0, 10, 0, 3, date));

                //Passing user id to MainActivity
                Bundle b = new Bundle();
                b.putInt("userID", id);
                mainActivity.putExtras(b);
                startActivity(mainActivity);
            }
        });
        //Sets Log In action
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);//Go to MainActivity
                try {
                    List<User> userList = db.getAllUsers();
                    for(User user : userList){
                        if(user.getUsername().equals(usernameInput.getText().toString())
                                && user.getPwd().equals(pwdInput.getText().toString())){

                            //Passing user id to MainActivity
                            Bundle b = new Bundle();
                            b.putInt("userID", user.getId());
                            mainActivity.putExtras(b);
                            startActivity(mainActivity);
                            finish();
                        }
                    }

                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT);
                    toast.show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
