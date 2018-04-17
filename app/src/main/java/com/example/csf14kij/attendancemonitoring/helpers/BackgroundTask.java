package com.example.csf14kij.attendancemonitoring.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.csf14kij.attendancemonitoring.activities.HomePage;

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

public class BackgroundTask extends AsyncTask<String,Void,String> {

    AlertDialog alertDialog;
    Context mctx;

    BackgroundTask(Context ctx)
    {
        this.mctx = ctx;
    }

    @Override
    protected void onPreExecute()
    {
        alertDialog = new AlertDialog.Builder(mctx).create();
        alertDialog.setTitle("Login Information");
    }

    @Override
    protected String doInBackground(String... params) {

        String response = null;
        String reg_url = "http://51.141.51.42/cloud/Register.php";
        String login_url = "http://51.141.51.42/cloud/Login.php";
        String method = params[0];
        if(method.equals("register"))
        {
            String name = params[1];
            String emailaddress = params[2];
            String password = params[3];

            try
            {
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data = URLEncoder.encode("name","UTF-8") +"="+URLEncoder.encode(name,"UTF-8")+"&"+
                        URLEncoder.encode("emailaddress","UTF-8") +"="+URLEncoder.encode(emailaddress,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8") +"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                return "Registration Success...";
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        else if (method.equals("login"))
        {
            String emailaddress = params[1];
            String password = params[2];
            try
            {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));

                String data = URLEncoder.encode("emailaddress","UTF-8") +"="+URLEncoder.encode(emailaddress,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8") +"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                response = "";
                String line = "";
                while ((line = bufferedReader.readLine())!=null)
                {
                    response +=line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;

            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return response;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result)
    {

        if(result.equals("Registration Success..."))
        {
            Log.d("myTag", "HERE");
            Toast.makeText(mctx, result, Toast.LENGTH_LONG).show();
        }
        else if(result.equals("Login Failed........Try Again"))
        {
            Toast.makeText(mctx, result, Toast.LENGTH_LONG).show();
        }
        else
        {
            Log.d("myTag", "HERE1");
            Log.d("myTag", result);

            Intent intent = new Intent(mctx.getApplicationContext(), HomePage.class);
            intent.putExtra("lecturerid",result);
            mctx.startActivity(intent);
        }
    }
}