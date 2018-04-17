package com.example.csf14kij.attendancemonitoring.helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.example.csf14kij.attendancemonitoring.HomePageSelection;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by csf14kij on 11/03/2018.
 */

public class HomePageBackgroundTask extends AsyncTask<String, Object, JSONArray> {
    public HomePageSelection delegate = null;

    @Override
    protected JSONArray doInBackground(String... params)
    {
        String getallcourses_url = params[0];
        JSONArray courses = new JSONArray();
        try
        {
            URL url = new URL(getallcourses_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

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
            courses = new JSONArray(response);
            Log.d("myTag", courses.toString());


            return courses;

        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    protected void onPostExecute(JSONArray result)
    {
        delegate.processFinish(result);
    }
}



