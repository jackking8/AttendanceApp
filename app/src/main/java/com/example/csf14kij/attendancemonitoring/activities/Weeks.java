package com.example.csf14kij.attendancemonitoring.activities;

import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.csf14kij.attendancemonitoring.HomePageSelection;
import com.example.csf14kij.attendancemonitoring.R;
import com.example.csf14kij.attendancemonitoring.helpers.WeeksBackgroundTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Weeks extends AppCompatActivity implements HomePageSelection {

    //{

    WeeksBackgroundTask weeksBackgroundTask = new WeeksBackgroundTask();

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle sToggle;
    String lecturerid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weeks);

        Intent intent = this.getIntent();
        String moduleid = intent.getStringExtra("moduleid");
        lecturerid = intent.getStringExtra("lecturerid");

        weeksBackgroundTask.delegate = this;

        String getallweeks = "http://51.141.51.42/cloud/getallweeksapp.php";
        weeksBackgroundTask.execute(getallweeks,moduleid);




        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        sToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open,R.string.close);

        drawerLayout.addDrawerListener(sToggle);
        sToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView mNavigationView = (NavigationView)findViewById(R.id.navigation_menu);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case (R.id.logout):
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        break;
                    case (R.id.home):
                        Intent intent1 = new Intent(getApplicationContext(), HomePage.class);
                        intent1.putExtra("lecturerid",lecturerid);
                        startActivity(intent1);
                        break;
                }
                return true;
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(sToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void processFinish(JSONArray output) {

        Log.d("myTag", "in processFinish" + output);

        LinearLayout layout = (LinearLayout)findViewById(R.id.button_list);

        int id = 0;
        String name = "";

        int arrSize = output.length();
        List<Integer> ids = new ArrayList<Integer>(arrSize);
        List<String> names = new ArrayList<String>(arrSize);

        for(int i=0;i<output.length();i++) {
            JSONObject json_obj = new JSONObject();
            try {
                json_obj = output.getJSONObject(i);
                String i_d = json_obj.getString("id");
                name = json_obj.getString("name");
                Log.d("myTag", i_d);
                Log.d("myTag", name);

                try
                {
                    id = Integer.parseInt(i_d);
                }
                catch (NumberFormatException nfe)
                {
                    nfe.printStackTrace();
                }
                ids.add(id);
                names.add(name);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Button tv[] = new Button[output.length()];

            tv[i] = new Button(this);
            tv[i].getBackground().setColorFilter(new LightingColorFilter(3162015, 3162015));

            int week_id = ids.get(i);
            tv[i].setId(week_id);

            String s = names.get(i);
            tv[i].setText(s);

            tv[i].setGravity(Gravity.CENTER_HORIZONTAL);

            tv[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ClassSessions.class);
                    int week_id = v.getId();
                    String weekid = String.valueOf(week_id);
                    intent.putExtra("weekid",weekid);
                    intent.putExtra("lecturerid",lecturerid);
                    startActivity(intent);
                }
            });
            layout.addView(tv[i]);
        }
    }
}