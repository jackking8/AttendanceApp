package com.example.csf14kij.attendancemonitoring.helpers;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.csf14kij.attendancemonitoring.activities.LecturerInput;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by csf14kij on 11/03/2018.
 */

public class LecturerInputBackgroundTask extends AsyncTask<String, Void, String> {

    Context mctx;
    String classid;
    String weekid;

    public LecturerInputBackgroundTask(Context ctx)
    {
        this.mctx = ctx;
    }

    @Override
    protected String doInBackground(String... params) {

        String updateattendance_url = params[0];
        String studentid = params[1];
        classid = params[2];
        weekid = params[3];

        try
        {
            URL url = new URL(updateattendance_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream OS = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
            String data = URLEncoder.encode("studentid","UTF-8") +"="+URLEncoder.encode(studentid,"UTF-8")+"&"+
                    URLEncoder.encode("classid","UTF-8") +"="+URLEncoder.encode(classid,"UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();
            InputStream IS = httpURLConnection.getInputStream();
            IS.close();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return "Attendance Confirmed";
    }

    @Override
    protected void onPostExecute(String result)
    {
        if(result.equals("Attendance Confirmed"))
        {
            Log.d("myTag", "HERE");
            Toast.makeText(mctx, result, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(mctx.getApplicationContext(), LecturerInput.class);
            intent.putExtra("classid",classid);
            intent.putExtra("weekid",weekid);
            mctx.startActivity(intent);
        }
    }

}
