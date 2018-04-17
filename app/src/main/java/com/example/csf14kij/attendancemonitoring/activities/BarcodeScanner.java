package com.example.csf14kij.attendancemonitoring.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.csf14kij.attendancemonitoring.helpers.CheckBarcodeBackgroundTask;
import com.example.csf14kij.attendancemonitoring.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.concurrent.ExecutionException;

public class BarcodeScanner extends AppCompatActivity  {

    String classid;
    String weekid;
    String lecturerid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        configureScan();
    }

    public void configureScan ()
    {
        IntentIntegrator integrator = new IntentIntegrator(this);

        Intent intent = this.getIntent();
        this.classid = intent.getStringExtra("classid");
        this.weekid = intent.getStringExtra("weekid");
        this.lecturerid = intent.getStringExtra("lecturerid");

        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        final String lecturer = "lecturer";
        final String student = "student";

        if (result != null)
        {
            if (result.getContents() != null)
            {
                String id = String.valueOf(result.getContents());
                String checkid_url = "http://51.141.51.42/cloud/checkid.php";
                String checkID = "";
                try
                {
                    checkID = new CheckBarcodeBackgroundTask().execute(checkid_url, id).get();

                    if(checkID.equalsIgnoreCase(lecturer))
                    {
                        Toast.makeText(this, "Scan Successful", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LecturerInput.class);
                        intent.putExtra("classid",classid);
                        intent.putExtra("weekid",weekid);
                        intent.putExtra("lecturerid",lecturerid);
                        startActivity(intent);
                    }
                    else if (checkID.equalsIgnoreCase(student))
                    {
                        String updateattendance_url = "http://51.141.51.42/cloud/updateattendance.php";
                        new CheckBarcodeBackgroundTask().execute(updateattendance_url, id, classid);
                        Toast.makeText(this, "Attendance Confirmed", Toast.LENGTH_LONG).show();
                        configureScan();
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(BarcodeScanner.this, ClassSessions.class);
                startActivity(intent);
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
