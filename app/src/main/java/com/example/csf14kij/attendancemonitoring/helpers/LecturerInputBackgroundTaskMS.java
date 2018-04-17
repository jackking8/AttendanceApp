package com.example.csf14kij.attendancemonitoring.helpers;

import android.content.Context;
import android.os.AsyncTask;

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

public class LecturerInputBackgroundTaskMS extends AsyncTask<String, Void, Void> {

    Context mctx;
    String classid;

    public LecturerInputBackgroundTaskMS(Context ctx)
    {
        this.mctx = ctx;
    }

    @Override
    protected Void doInBackground(String... params) {

        String updateattendance_url = params[0];
        classid = params[1];

        try
        {
            URL url = new URL(updateattendance_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream OS = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
            String data = URLEncoder.encode("classid","UTF-8") +"="+URLEncoder.encode(classid,"UTF-8");
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
        return null;
    }
}
