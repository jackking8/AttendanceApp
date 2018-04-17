package com.example.csf14kij.attendancemonitoring.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.csf14kij.attendancemonitoring.helpers.LecturerInputBackgroundTask;
import com.example.csf14kij.attendancemonitoring.helpers.LecturerInputBackgroundTaskMS;
import com.example.csf14kij.attendancemonitoring.R;
import com.example.csf14kij.attendancemonitoring.helpers.MotionSensor;

public class LecturerInput extends AppCompatActivity {


    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private MotionSensor mMotionSensor;

    EditText studentid;
    String student_id;
    String classid;
    String weekid;
    String lecturerid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_input);

        Intent intent = this.getIntent();
        classid = intent.getStringExtra("classid");
        weekid = intent.getStringExtra("weekid");
        lecturerid = intent.getStringExtra("lecturerid");
        studentid = (EditText)findViewById(R.id.studentid);
    }

    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
            mSensorManager.registerListener(mMotionSensor, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mMotionSensor);
        super.onPause();
    }

    private void setupShake() {
        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMotionSensor = new MotionSensor();
        mMotionSensor.setOnShakeListener(new MotionSensor.OnShakeListener() {

            @Override
            public void onShake(int count) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */
                handleShakeEvent(count);
            }
        });
    }

    private void handleShakeEvent(int count) {
        if (count == 2) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
            dialog.setTitle("Refresh");
            dialog.setMessage("The Page has been Refreshed");
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.create();
            dialog.show();
        }
    }

    public void confirmAttendance(View view)
    {
        student_id = studentid.getText().toString();
        if(!student_id.substring(0, 1).matches("B")||student_id.length()<9)
        {
            Toast.makeText(LecturerInput.this, "Student ID is not valid. Please Try Again",
                    Toast.LENGTH_LONG).show();
        }
        else
        {
            String updateattendance_url = "http://51.141.51.42/cloud/updateattendance.php";
            new LecturerInputBackgroundTask(this).execute(updateattendance_url, student_id, classid, weekid);
        }
    }

    public void exitScanning(View view)
    {
        String updateMS_url = "http://51.141.51.42/cloud/updateMS.php";
        new LecturerInputBackgroundTaskMS(this).execute(updateMS_url, classid);

        Intent intent = new Intent(getApplicationContext(), ClassSessions.class);
        intent.putExtra("weekid",weekid);
        intent.putExtra("lecturerid",lecturerid);
        startActivity(intent);
    }
}
