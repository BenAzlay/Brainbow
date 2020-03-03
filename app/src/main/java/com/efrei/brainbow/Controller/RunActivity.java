package com.efrei.brainbow.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.efrei.brainbow.R;

public class RunActivity extends AppCompatActivity implements SensorEventListener {
    public static final String BUNDLE_EXTRA_DISTANCE = "BUNDLE_EXTRA_DISTANCE";
    SensorManager sensorManager;
    Sensor stepSensor;

    private TextView stepsLabel;
    private TextView distanceLabel;
    private Button stopButton;

    private int steps = 0;
    private int distance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        stepsLabel = (TextView) findViewById(R.id.stepsLabel);
        distanceLabel = (TextView) findViewById(R.id.distanceLabel);
        stopButton = (Button) findViewById(R.id.stopButton);

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RunActivity.this);

                // Set a title for alert dialog
                builder.setTitle("Stop activity?");

                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopActivity();
                    }
                });

                // Set the alert dialog no button click listener
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
                        Toast.makeText(getApplicationContext(),
                                "Good luck",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();
            }
        });
    }

    private void stopActivity() {
        Intent intent = new Intent();
        intent.putExtra(BUNDLE_EXTRA_DISTANCE, distance);
        setResult(RESULT_OK, intent);
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this, stepSensor);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;

        if (values.length > 0) {
            value = (int) values[0];
        }


        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            steps++;
        }
        Log.e("Steps", Integer.toString(steps));
        getDistanceRun(steps);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //function to determine the distance run in kilometers using average step length for men and number of steps
    public void getDistanceRun(int steps){
        distance = (steps*78)/100; //78 cm = length of one step, converted to meters
        stepsLabel.setText(Integer.toString(steps));
        distanceLabel.setText(Integer.toString(distance));
    }
}
