package com.example.csf14kij.attendancemonitoring.helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.example.csf14kij.attendancemonitoring.HomePageSelection;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by csf14kij on 11/03/2018.
 */

public class ClassSessionsBackgroundTask extends AsyncTask<String, Object, JSONArray> {

    public HomePageSelection delegate = null;

    @Override
    protected JSONArray doInBackground(String... params) {

        String getallsessions_url = params[0];
        String weekid = params[1];
        JSONArray sessions = new JSONArray();

        try
        {
            URL url = new URL(getallsessions_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream OS = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));

            String data = URLEncoder.encode("weekid","UTF-8") +"="+URLEncoder.encode(weekid,"UTF-8");

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
            String response = "";
            String line = "";
            while ((line = bufferedReader.readLine())!=null)
            {
                response +=line;
            }

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

            Log.d("myTag", response);
            sessions = new JSONArray(response);
            Log.d("myTag", sessions.toString());

            return sessions;
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return sessions;
    }

    @Override
    protected void onPostExecute(JSONArray result)
    {
        delegate.processFinish(result);
    }
}