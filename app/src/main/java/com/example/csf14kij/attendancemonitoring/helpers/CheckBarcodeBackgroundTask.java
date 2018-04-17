package com.example.csf14kij.attendancemonitoring.helpers;

import android.os.AsyncTask;

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

public class CheckBarcodeBackgroundTask extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params) {

        String checkid_url = params[0];
        String id = params[1];
        String response = "";

        try
        {
            URL url = new URL(checkid_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream OS = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));

            String data = URLEncoder.encode("id","UTF-8") +"="+URLEncoder.encode(id,"UTF-8");

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            response = "";
            String line = "";
            while ((line = bufferedReader.readLine())!=null)
            {
                response +=line;
            }

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return response;
    }
}
