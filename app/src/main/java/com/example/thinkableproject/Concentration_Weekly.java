package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifImageView;

public class Concentration_Weekly extends AppCompatActivity {
    Dialog dialogcw;
    BarChart barChart1;
    AppCompatButton monthly, yearly, daily, improveConcentration;
    Animation scaleUp, scaleDown;

    View dayMode, nightMode;
    TextView realTime;
    Double sum1In = 0.0;
    Double total1 = 0.0;
    Double sum2In = 0.0;
    Double total2 = 0.0;
    Double sum3In = 0.0;
    Double total3 = 0.0;
    Double sumIn = 0.0;
    Double total = 0.0;
    Double sum4In = 0.0;
    Double total4 = 0.0;
    Double sum5In = 0.0;
    Double total5 = 0.0;
    Double sum6In = 0.0;
    Double total6 = 0.0;
    ImageView games, relaxationBtn, memory;
    LottieAnimationView anim;
    FirebaseUser mUser;
    ImageView music;
    GifImageView c1gif, c2gif;
    HorizontalScrollView scrollView;
    View c1, c2;
    File localFile, fileName;
    ArrayList<String> list = new ArrayList<>();
    String text;
    ArrayList<Float> floatList = new ArrayList<>();
    int color;
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;
    Double avg1, avg2, avg3, avg4, avg5;
    Long average1, average2, average3, average4, average5;
    int sum1, sum2, sum3, sum4, sum5;
    AppCompatButton progressTime, improvementChart;
    ArrayList<Float> xVal = new ArrayList<>(Arrays.asList(2f, 4f, 6f, 8f, 10f, 12f, 14f));
    ArrayList<Float> yVal = new ArrayList(Arrays.asList(45f, 36f, 75f, 36f, 73f, 45f, 83f));
    ArrayList<String> xnewVal = new ArrayList<>();
    ArrayList<String> ynewVal = new ArrayList<>();
    ArrayList<Float> floatxVal = new ArrayList<>();
    ArrayList<Float> floatyVal = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentration__weekly);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        barChart1 = (BarChart) findViewById(R.id.barChartWeekly);
        monthly = findViewById(R.id.monthly);
        yearly = findViewById(R.id.yearly);
        //dayMode=findViewById(R.id.dayMode);
//        nightMode = findViewById(R.id.nightMode);
        daily = findViewById(R.id.daily);
        relaxationBtn = findViewById(R.id.relaxation);
        List<BarEntry> entries = new ArrayList<>();
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.sacale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        dialogcw = new Dialog(this);
        memory = findViewById(R.id.memory);
        improveConcentration = findViewById(R.id.improveConcentration);
        music = findViewById(R.id.music);
        games = findViewById(R.id.game);
        anim = findViewById(R.id.animation);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c1gif = findViewById(R.id.landingfwall);
        c2gif = findViewById(R.id.landingfwall1);
        lineChart = findViewById(R.id.lineChartWeekly);
        scrollView = findViewById(R.id.scroll);
        progressTime = findViewById(R.id.progressTime);
        improvementChart = findViewById(R.id.improvement);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        DatabaseReference colorreference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("theme");
        colorreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FirebaseColor", String.valueOf(snapshot.getValue()));
                color = (int) snapshot.getValue(Integer.class);
                Log.d("Color", String.valueOf(color));

                if (color == 2) {  //light theme
                    c1.setVisibility(View.INVISIBLE);  //c1 ---> dark blue , c2 ---> light blue
                    c2.setVisibility(View.VISIBLE);
                    c2gif.setVisibility(View.VISIBLE);
                    c1gif.setVisibility(View.GONE);


                } else if (color == 1) { //light theme

                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.INVISIBLE);
                    c1gif.setVisibility(View.VISIBLE);
                    c2gif.setVisibility(View.INVISIBLE);


                } else {
                    if (timeOfDay >= 0 && timeOfDay < 12) { //light theme

                        c1.setVisibility(View.INVISIBLE);
                        c2.setVisibility(View.VISIBLE);
                        c2gif.setVisibility(View.VISIBLE);
                        c1gif.setVisibility(View.GONE);

                    } else if (timeOfDay >= 12 && timeOfDay < 16) {//dark theme
                        c1.setVisibility(View.INVISIBLE);
                        c2.setVisibility(View.VISIBLE);
                        c2gif.setVisibility(View.VISIBLE);
                        c1gif.setVisibility(View.GONE);

                    } else if (timeOfDay >= 16 && timeOfDay < 24) {//dark theme
                        c1.setVisibility(View.VISIBLE);
                        c2.setVisibility(View.INVISIBLE);
                        c1gif.setVisibility(View.VISIBLE);
                        c2gif.setVisibility(View.INVISIBLE);


                    }
                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        improveConcentration.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    improveConcentration.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    improveConcentration.startAnimation(scaleDown);
                }

                return false;
            }
        });

        improveConcentration.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    improveConcentration.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    improveConcentration.startAnimation(scaleDown);
                }

                return false;
            }
        });

        improvementChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);

                scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        });

        improvementChart.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    improvementChart.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    improvementChart.startAnimation(scaleDown);
                }

                return false;
            }
        });
        progressTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT);

                scrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
            }
        });

        progressTime.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    progressTime.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    progressTime.startAnimation(scaleDown);
                }

                return false;
            }
        });


        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Music.class));
            }
        });

        games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });
        getEntries();


        //Initializing bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Relaxation_Daily.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.exercise:
                        startActivity(new Intent(getApplicationContext(), Exercise.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.reports:
                        startActivity(new Intent(getApplicationContext(), ConcentrationReportDaily.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.userprofiles:
                        startActivity(new Intent(getApplicationContext(), ResultActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), Setting.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        // On click listener of monthly button
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Concentration_Monthly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        monthly.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    monthly.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    monthly.startAnimation(scaleDown);
                }

                return false;
            }
        });

        // On click listener of yearly button
        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Concentration_Yearly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        yearly.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    yearly.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    yearly.startAnimation(scaleDown);
                }

                return false;
            }
        });

        // On click listener of daily button
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Concentration_Daily.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        daily.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    daily.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    daily.startAnimation(scaleDown);
                }

                return false;
            }
        });

        // On click listener of relaxation button
        relaxationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Relaxation_Weekly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        relaxationBtn.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    relaxationBtn.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    relaxationBtn.startAnimation(scaleDown);
                }

                return false;
            }
        });

        // On click listener of realTime indication button
        anim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Calibration.class);
                startActivity(intent);
            }
        });
        anim.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    anim.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    anim.startAnimation(scaleDown);
                }

                return false;
            }
        });

        anim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Calibration.class);
                startActivity(intent);
            }
        });
        memory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Memory_Weekly.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        memory.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    memory.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    memory.startAnimation(scaleDown);
                }

                return false;
            }
        });

        String[] weeks = new String[]{"Week1", "Week2", "Week3", "Week4", "Week5"};
        ArrayList<Float> creditsMain = new ArrayList<>(Arrays.asList(90f, 30f, 70f, 50f));

        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
        Log.d("MONTH", String.valueOf(now.get(Calendar.MONTH)));
        Log.d("YEAR", String.valueOf(now.get(Calendar.YEAR)));
        Log.d("DAY", String.valueOf(now.get(Calendar.DAY_OF_WEEK)));
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(new Date());
//prints day name
        System.out.println("Day Name: " + str);
        Log.d("Day Name", str);

        int month = now.get(Calendar.MONTH) + 1;
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR)))
                        .child(String.valueOf(month)).child(String.valueOf(1));

                reference.addValueEventListener(new ValueEventListener() {


                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList sumElement = new ArrayList();
                        int sum = (0);
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Log.d("Weekly1 Val", String.valueOf(dataSnapshot.getValue()));
                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                Log.d("Weekly1 Array", String.valueOf(snapshot1.getValue()));
                                Long av1 = (Long) snapshot1.getValue();
                                sumElement.add(snapshot1.getValue());
                                sum1 += av1;


                            }
                        }
                        Log.d("Weekly1 Array", String.valueOf(sumElement));
                        Log.d("Weekly1 SUM", String.valueOf(sum1));
                        if (sum1 != 0) {
                            average1 = sum1 / Long.parseLong(String.valueOf(sumElement.size()));
                        } else {
                            average1 = 0L;
                        }
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR)))
                                .child(String.valueOf(month)).child(String.valueOf(2));

                        reference.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList sumElement = new ArrayList();
                                int sum = (0);
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Log.d("Weekly2 Val", String.valueOf(dataSnapshot.getValue()));
                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                        Log.d("Weekly2 Array", String.valueOf(snapshot1.getValue()));
                                        Long av1 = (Long) snapshot1.getValue();
                                        sumElement.add(snapshot1.getValue());
                                        sum2 += av1;


                                    }
                                }
                                Log.d("Weekly2 Array", String.valueOf(sumElement));
                                Log.d("Weekly2 SUM", String.valueOf(sum2));
                                if (sum2 != 0) {
                                    average2 = sum2 / Long.parseLong(String.valueOf(sumElement.size()));
                                } else {
                                    average2 = 0L;
                                }

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR)))
                                        .child(String.valueOf(month)).child(String.valueOf(3));

                                reference.addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList sumElement = new ArrayList();
                                        int sum = (0);
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            Log.d("Weekly3 Val", String.valueOf(dataSnapshot.getValue()));
                                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                Log.d("Weekly3 Array", String.valueOf(snapshot1.getValue()));
                                                Long av1 = (Long) snapshot1.getValue();
                                                sumElement.add(snapshot1.getValue());
                                                sum3 += av1;


                                            }
                                        }
                                        Log.d("Weekly3 Array", String.valueOf(sumElement));
                                        Log.d("Weekly3 SUM", String.valueOf(sum3));
                                        if (sum3 != 0) {
                                            average3 = sum3 / Long.parseLong(String.valueOf(sumElement.size()));
                                        } else {
                                            average3 = 0L;
                                        }

                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR)))
                                                .child(String.valueOf(month)).child(String.valueOf(4));

                                        reference.addValueEventListener(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ArrayList sumElement = new ArrayList();
                                                int sum = (0);
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    Log.d("Weekly4 Val", String.valueOf(dataSnapshot.getValue()));
                                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                        Log.d("Weekly4 Array", String.valueOf(snapshot1.getValue()));
                                                        Long av1 = (Long) snapshot1.getValue();
                                                        sumElement.add(snapshot1.getValue());
                                                        sum4 += av1;


                                                    }
                                                }
                                                Log.d("Weekly4 Array", String.valueOf(sumElement));
                                                Log.d("Weekly4 SUM", String.valueOf(sum4));
                                                if (sum4 != 0) {
                                                    average4 = sum4 / Long.parseLong(String.valueOf(sumElement.size()));
                                                } else {
                                                    average4 = 0L;
                                                }
                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR)))
                                                        .child(String.valueOf(month)).child(String.valueOf(5));

                                                reference.addValueEventListener(new ValueEventListener() {

                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        ArrayList sumElement = new ArrayList();
                                                        int sum = (0);
                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                            Log.d("Weekly4 Val", String.valueOf(dataSnapshot.getValue()));
                                                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                                Log.d("Weekly4 Array", String.valueOf(snapshot1.getValue()));
                                                                Long av1 = (Long) snapshot1.getValue();
                                                                sumElement.add(snapshot1.getValue());
                                                                sum5 += av1;


                                                            }
                                                        }
                                                        Log.d("Weekly4 Array", String.valueOf(sumElement));
                                                        Log.d("Weekly4 SUM", String.valueOf(sum5));
                                                        if (sum5 != 0) {
                                                            average5 = sum5 / Long.parseLong(String.valueOf(sumElement.size()));
                                                        } else {
                                                            average5 = 0L;
                                                        }


                                                        Log.d("Average Week1", String.valueOf(average1));
                                                        Log.d("Average Week2", String.valueOf(average2));
                                                        float sum1min = TimeUnit.SECONDS.toMinutes(sum1);
                                                        float sum2min = TimeUnit.SECONDS.toMinutes(sum2);
                                                        float sum3min = TimeUnit.SECONDS.toMinutes(sum3);
                                                        float sum4min = TimeUnit.SECONDS.toMinutes(sum4);
                                                        float sum5min = TimeUnit.SECONDS.toMinutes(sum5);
                                                        ArrayList<BarEntry> entries = new ArrayList();
                                                        entries.add(new BarEntry(1, sum1min));
                                                        entries.add(new BarEntry(2, sum2min));
                                                        entries.add(new BarEntry(3, sum3min));
                                                        entries.add(new BarEntry(4, sum4min));


                                                        //Initializing object of MyBarDataset class and passing th arraylist to y axis of chart
                                                        MyBarDataset dataSet = new MyBarDataset(entries, "data", creditsMain);
                                                        dataSet.setColors(ContextCompat.getColor(getApplicationContext(), R.color.Bwhite),
                                                                ContextCompat.getColor(getApplicationContext(), R.color.Lblue),
                                                                ContextCompat.getColor(getApplicationContext(), R.color.blue),
                                                                ContextCompat.getColor(getApplicationContext(), R.color.bluebar),
                                                                ContextCompat.getColor(getApplicationContext(), R.color.dark));
                                                        BarData data = new BarData(dataSet);
                                                        data.setDrawValues(false);
                                                        data.setBarWidth(0.7f);
                                                        String labels[] = {"", "WK1", "WK2", "WK3", "WK4", "WK5", "", ""};
                                                        barChart1.setData(data);
                                                        barChart1.setFitBars(true);
                                                        barChart1.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                                                        barChart1.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                                        barChart1.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                                        barChart1.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                                        barChart1.getXAxis().setTextSize(8f);
                                                        barChart1.getAxisLeft().setTextSize(8f);
                                                        barChart1.getXAxis().setLabelCount(11, true);
                                                        barChart1.setExtraBottomOffset(7f);


                                                        barChart1.getAxisRight().setEnabled(false);
                                                        Description desc = new Description();
                                                        desc.setText("Time Spent Weekly on Concentration");
                                                        desc.setTextColor(getResources().getColor(R.color.white));
                                                        barChart1.setDescription(desc);
                                                        barChart1.getLegend().setEnabled(false);
                                                        barChart1.getXAxis().setDrawGridLines(false);
                                                        barChart1.getAxisLeft().setDrawGridLines(false);
                                                        barChart1.setNoDataText("Data Loading Please Wait...");
                                                        barChart1.animateXY(1500, 1500);
                                                        barChart1.invalidate();


                                                        Log.d("Average Week3", String.valueOf(average3));
                                                        Log.d("Average Week4", String.valueOf(average4));


                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }, 3000);
//        try {
//            fileName = new File(getCacheDir() + "/weeklyX.txt");  //Writing data to file
//            FileWriter fw;
//            fw = new FileWriter(fileName);
//            BufferedWriter output = new BufferedWriter(fw);
//            int size = xVal.size();
//            for (int i = 0; i < size; i++) {
//                output.write(xVal.get(i).toString() + "\n");
////                Toast.makeText(this, "Success Writing X Data", Toast.LENGTH_SHORT).show();
//            }
//            output.close();
//        } catch (IOException exception) {
//            Toast.makeText(this, "Failed Writing X Data", Toast.LENGTH_SHORT).show();
//            exception.printStackTrace();
//        }

//
        mUser = FirebaseAuth.getInstance().getCurrentUser(); // get current user
        mUser.getUid();
//
//        // Uploading saved data containing file to firebase storage
//        StorageReference storageXAxis = FirebaseStorage.getInstance().getReference(mUser.getUid());
//        try {
//            StorageReference mountainsRef = storageXAxis.child("weeklyX.txt");
//            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
//            UploadTask uploadTask = mountainsRef.putStream(stream);
//            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                    Toast.makeText(Concentration_Weekly.this, "File Uploaded X data", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
////                    Toast.makeText(Concentration_Weekly.this, "File Uploading Failed X", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            fileName = new File(getCacheDir() + "/weeklyY.txt");  //Writing data to file
//            FileWriter fw;
//            fw = new FileWriter(fileName);
//            BufferedWriter output = new BufferedWriter(fw);
//            int size = yVal.size();
//            for (int i = 0; i < size; i++) {
//                output.write(yVal.get(i).toString() + "\n");
////                Toast.makeText(this, "Success Writing Y data", Toast.LENGTH_SHORT).show();
//            }
//            output.close();
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }

//        StorageReference storageYAxis = FirebaseStorage.getInstance().getReference(mUser.getUid());
//        try {
//            StorageReference mountainsRef = storageYAxis.child("weeklyY.txt");
//            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
//            UploadTask uploadTask = mountainsRef.putStream(stream);
//            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                    Toast.makeText(Concentration_Weekly.this, "File Uploaded Y Axis", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
////                    Toast.makeText(Concentration_Weekly.this, "File Uploading Failed Y Data", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }


    }

    private void getEntries() {
        Handler handler = new Handler();
        final int delay = 5000;
        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
        Log.d("MONTH", String.valueOf(now.get(Calendar.MONTH)));
        Log.d("YEAR", String.valueOf(now.get(Calendar.YEAR)));
        Log.d("DAY", String.valueOf(now.get(Calendar.DAY_OF_WEEK)));
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(new Date());
//prints day name
        System.out.println("Day Name: " + str);
        Log.d("Day Name", str);

        int month = now.get(Calendar.MONTH) + 1;

        handler.postDelayed(new Runnable() {


            @Override
            public void run() {

                lineEntries = new ArrayList();

                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                        .child(String.valueOf(month)).child(String.valueOf(1));

                reference1.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList sumElement = new ArrayList();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            Log.d("ValuesWeek1", String.valueOf(dataSnapshot.getValue()));
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Log.d("ValuesWeek1Data", String.valueOf(dataSnapshot1.getValue()));
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                    Log.d("DataWeek1", String.valueOf(dataSnapshot2.getKey()));
                                    for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                        Log.d("DataFin", dataSnapshot3.getKey());
                                        if (dataSnapshot3.getKey().equals("index")) {
                                            dataSnapshot3.getValue();
                                            Log.d("DataF", String.valueOf(dataSnapshot3.getValue()));
                                            Double av1 = (Double) dataSnapshot3.getValue();
                                            sumElement.add(av1);
                                            sumIn += av1;
                                        }
                                    }
                                }
                            }
                        }
                        for (int i = 0; i < sumElement.size(); i++) {
                            total1 += (Double) sumElement.get(i);
                        }
                        Log.d("TotalWeek1", String.valueOf(total1));

                        if (total1 == 0.0) {
                            sumIn = 0.0;
                            avg1 = 0.0;
                        } else {
                            Log.d("SUMWeek1", String.valueOf(total1));
                            avg1 = total1 / sumElement.size();
                            Log.d("AverageWeek1", String.valueOf(avg1));
                        }
                        Log.d("AverageWeek1", String.valueOf(avg1));
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                .child(String.valueOf(month)).child(String.valueOf(2));

                        reference1.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList sumElement = new ArrayList();

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    Log.d("ValuesWeek1", String.valueOf(dataSnapshot.getValue()));
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        Log.d("ValuesWeek1Data", String.valueOf(dataSnapshot1.getValue()));
                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                            Log.d("DataWeek1", String.valueOf(dataSnapshot2.getKey()));
                                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                Log.d("DataFin", dataSnapshot3.getKey());
                                                if (dataSnapshot3.getKey().equals("index")) {
                                                    dataSnapshot3.getValue();
                                                    Log.d("DataF", String.valueOf(dataSnapshot3.getValue()));
                                                    Double av1 = (Double) dataSnapshot3.getValue();
                                                    sumElement.add(av1);
                                                    sum1In += av1;
                                                }
                                            }
                                        }
                                    }
                                }
                                for (int i = 0; i < sumElement.size(); i++) {
                                    total2 += (Double) sumElement.get(i);
                                }
                                Log.d("TotalWeek2", String.valueOf(total2));

                                if (total2 == 0.0) {
                                    sum1In = 0.0;
                                    avg2 = 0.0;
                                } else {
                                    Log.d("SUMWeek2", String.valueOf(total2));
                                    avg2 = total2 / sumElement.size();
                                    Log.d("AverageWeek2", String.valueOf(avg2));
                                }
                                Log.d("AverageWeek2", String.valueOf(avg1));

                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                        .child(String.valueOf(month)).child(String.valueOf(3));

                                reference1.addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList sumElement = new ArrayList();

                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                            Log.d("ValuesWeek3", String.valueOf(dataSnapshot.getValue()));
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                Log.d("ValuesWeek1Data", String.valueOf(dataSnapshot1.getValue()));
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                    Log.d("DataWeek3", String.valueOf(dataSnapshot2.getKey()));
                                                    for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                        Log.d("DataFin", dataSnapshot3.getKey());
                                                        if (dataSnapshot3.getKey().equals("index")) {
                                                            dataSnapshot3.getValue();
                                                            Log.d("DataF", String.valueOf(dataSnapshot3.getValue()));
                                                            Double av1 = (Double) dataSnapshot3.getValue();
                                                            sumElement.add(av1);
                                                            sum2In += av1;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        for (int i = 0; i < sumElement.size(); i++) {
                                            total3 += (Double) sumElement.get(i);
                                        }
                                        Log.d("TotalWeek3", String.valueOf(total3));

                                        if (total3 == 0.0) {
                                            sum2In = 0.0;
                                            avg3 = 0.0;
                                        } else {
                                            Log.d("SUMWeek3", String.valueOf(total3));
                                            avg3 = total3 / sumElement.size();
                                            Log.d("AverageWeek3", String.valueOf(avg3));
                                        }
                                        Log.d("AverageWeek3", String.valueOf(avg3));
                                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                                .child(String.valueOf(month)).child(String.valueOf(4));

                                        reference1.addValueEventListener(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ArrayList sumElement = new ArrayList();

                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                    Log.d("ValuesWeek3", String.valueOf(dataSnapshot.getValue()));
                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                        Log.d("ValuesWeek1Data", String.valueOf(dataSnapshot1.getValue()));
                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                            Log.d("DataWeek3", String.valueOf(dataSnapshot2.getKey()));
                                                            for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                                Log.d("DataFin", dataSnapshot3.getKey());
                                                                if (dataSnapshot3.getKey().equals("index")) {
                                                                    dataSnapshot3.getValue();
                                                                    Log.d("DataF", String.valueOf(dataSnapshot3.getValue()));
                                                                    Double av1 = (Double) dataSnapshot3.getValue();
                                                                    sumElement.add(av1);
                                                                    sum3In += av1;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                for (int i = 0; i < sumElement.size(); i++) {
                                                    total4 += (Double) sumElement.get(i);
                                                }
                                                Log.d("TotalWeek4", String.valueOf(total4));

                                                if (total4 == 0.0) {
                                                    sum3In = 0.0;
                                                    avg4 = 0.0;
                                                } else {
                                                    Log.d("SUMWeek4", String.valueOf(total4));
                                                    avg4 = total4 / sumElement.size();
                                                    Log.d("AverageWeek4", String.valueOf(avg4));
                                                }
                                                Log.d("AverageWeek4", String.valueOf(avg4));

                                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Concentration Post").child(String.valueOf(now.get(Calendar.YEAR)))
                                                        .child(String.valueOf(month)).child(String.valueOf(5));

                                                reference1.addValueEventListener(new ValueEventListener() {

                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        ArrayList sumElement = new ArrayList();

                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                            Log.d("ValuesWeek5", String.valueOf(dataSnapshot.getValue()));
                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                Log.d("ValuesWeek1Data", String.valueOf(dataSnapshot1.getValue()));
                                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                                                    Log.d("DataWeek5", String.valueOf(dataSnapshot2.getKey()));
                                                                    for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                                        Log.d("DataFin", dataSnapshot3.getKey());
                                                                        if (dataSnapshot3.getKey().equals("index")) {
                                                                            dataSnapshot3.getValue();
                                                                            Log.d("DataF", String.valueOf(dataSnapshot3.getValue()));
                                                                            Double av1 = (Double) dataSnapshot3.getValue();
                                                                            sumElement.add(av1);
                                                                            sum4In += av1;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        for (int i = 0; i < sumElement.size(); i++) {
                                                            total5 += (Double) sumElement.get(i);
                                                        }
                                                        Log.d("TotalWeek5", String.valueOf(total5));

                                                        if (total5 == 0.0) {
                                                            sum4In = 0.0;
                                                            avg5 = 0.0;
                                                        } else {
                                                            Log.d("SUMWeek5", String.valueOf(total5));
                                                            avg5 = total5 / sumElement.size();
                                                            Log.d("AverageWeek5", String.valueOf(avg5));
                                                        }
                                                        Log.d("AverageWeek5", String.valueOf(avg5));

                                                        Log.d("AverageData6", String.valueOf(avg1));
                                                        Log.d("Average Outside1 Con", String.valueOf(avg1));
                                                        Log.d("Average Outside2 Con", String.valueOf(avg2));
                                                        Log.d("Average Outside3 Con", String.valueOf(avg3));
                                                        Log.d("Average Outside4 Con", String.valueOf(avg4));
                                                        Log.d("Average Outside5 Con", String.valueOf(avg5));

                                                        final String[] weekdays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
////
////
                                                        lineEntries.add(new Entry(1, Float.parseFloat(String.valueOf(avg1))));
                                                        Log.d("FloatAverage", String.valueOf(Float.parseFloat(String.valueOf(avg1))));
                                                        lineEntries.add(new Entry(2, Float.parseFloat(String.valueOf(avg2))));
                                                        lineEntries.add(new Entry(3, Float.parseFloat(String.valueOf(avg3))));
                                                        lineEntries.add(new Entry(4, Float.parseFloat(String.valueOf(avg4))));
                                                        lineEntries.add(new Entry(5, Float.parseFloat(String.valueOf(avg5))));

                                                        String xAxisValues[] = {"", "WK1", "WK2", "WK3", "WK4", "WK5", "", ""};

                                                        lineDataSet = new LineDataSet(lineEntries, "Concentration Daily Progress");
                                                        lineChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));

                                                        lineData = new LineData(lineDataSet);
                                                        lineChart.setData(lineData);

                                                        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                                                        lineDataSet.setValueTextColor(Color.WHITE);
                                                        lineDataSet.setValueTextSize(6f);

                                                        lineChart.setGridBackgroundColor(Color.TRANSPARENT);
                                                        lineChart.getXAxis().setTextSize(8f);
                                                        lineChart.setBorderColor(Color.TRANSPARENT);
                                                        lineChart.setGridBackgroundColor(Color.TRANSPARENT);
                                                        lineChart.getAxisLeft().setDrawGridLines(false);
                                                        lineChart.getXAxis().setDrawGridLines(false);
                                                        lineChart.getAxisRight().setDrawGridLines(false);
//                                                                        lineChart.getXAxis().setLabelCount(7, true);
                                                        lineChart.getAxisRight().setTextColor(getResources().getColor(R.color.white));
                                                        lineChart.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                                        lineChart.getLegend().setTextColor(getResources().getColor(R.color.white));
                                                        lineChart.setTouchEnabled(true);
                                                        lineChart.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                                        lineChart.setDragEnabled(true);
                                                        lineChart.setScaleEnabled(false);
                                                        lineChart.setPinchZoom(false);
                                                        lineChart.setDrawGridBackground(false);
                                                        lineChart.setExtraBottomOffset(5f);
                                                        lineChart.getXAxis().setLabelCount(7, true);
                                                        lineChart.getXAxis().setAvoidFirstLastClipping(true);
                                                        lineChart.getDescription().setTextColor(R.color.white);
                                                        lineChart.invalidate();
                                                        lineChart.refreshDrawableState();
                                                        XAxis xAxis = lineChart.getXAxis();
                                                        xAxis.setGranularity(1f);
                                                        xAxis.setCenterAxisLabels(true);
                                                        xAxis.setEnabled(true);
                                                        xAxis.setDrawGridLines(false);
                                                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//
//                                                                    }

//                                                                    //
//                                                                    @Override
//                                                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                                                    }
//                                                                });
////
//
//                                                            }
//
//                                                            @Override
//                                                            public void onCancelled(@NonNull DatabaseError error) {
//
//                                                            }
//                                                        });
////
////
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
//
////
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
////
////
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
//
////
                            }

                            //
                            //
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }, 10000);

    }

    //popup window method to provide suggestions for improve concentration
    public void gotoPopup3m(View view) {
        startActivity(new Intent(getApplicationContext(), Connection.class));
//        ImageButton cancelcon, games, music1;
//        View c1, c2;
//
//        FirebaseUser mUser;
//
//
//        dialogcw.setContentView(R.layout.activity_concentration_popup);
//        c1 = (View) dialogcw.findViewById(R.id.c1);
//        c2 = (View) dialogcw.findViewById(R.id.c2);
//
//        mUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        Calendar c = Calendar.getInstance();
//        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
//
//        DatabaseReference colorreference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("theme");
//        colorreference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d("FirebaseColor PopUp", String.valueOf(snapshot.getValue()));
//                color = (int) snapshot.getValue(Integer.class);
//                Log.d("Color", String.valueOf(color));
//
//                if (color == 2) {  //light theme
//                    c1.setVisibility(View.INVISIBLE);  //c1 ---> dark blue , c2 ---> light blue
//                    c2.setVisibility(View.VISIBLE);
//                } else if (color == 1) { //light theme
//
//                    c1.setVisibility(View.VISIBLE);
//                    c2.setVisibility(View.INVISIBLE);
//
//
//                } else {
//                    if (timeOfDay >= 0 && timeOfDay < 12) { //light theme
//
//                        c1.setVisibility(View.INVISIBLE);
//                        c2.setVisibility(View.VISIBLE);
//
//
//                    } else if (timeOfDay >= 12 && timeOfDay < 16) {//dark theme
//                        c1.setVisibility(View.INVISIBLE);
//                        c2.setVisibility(View.VISIBLE);
//
//
//                    } else if (timeOfDay >= 16 && timeOfDay < 24) {//dark theme
//                        c1.setVisibility(View.VISIBLE);
//                        c2.setVisibility(View.INVISIBLE);
//
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        games = (ImageButton) dialogcw.findViewById(R.id.gamespop1);
//        music1 = (ImageButton) dialogcw.findViewById(R.id.musicpop1);
//
//
//        games.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        music1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), Music.class);
//                startActivity(intent);
//            }
//        });
//
//        cancelcon = (ImageButton) dialogcw.findViewById(R.id.canclepop1);
//        cancelcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogcw.dismiss();
//            }
//        });
//        dialogcw.show();
    }

    public void caliweekly(View view) {
        Intent intentcw = new Intent(Concentration_Weekly.this, Calibration.class);

        startActivity(intentcw);
    }

    public void calidaily(View view) {
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    public void gotoPopup1(View view) {
        ImageButton games, music1, cancelcon;


        dialogcw.setContentView(R.layout.activity_concentration_popup);

        games = (ImageButton) dialogcw.findViewById(R.id.gamespop1);
        music1 = (ImageButton) dialogcw.findViewById(R.id.musicpop1);

        games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });

        music1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Music.class);
                startActivity(intent);
            }
        });

        cancelcon = (ImageButton) dialogcw.findViewById(R.id.canclepop1);
        cancelcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogcw.dismiss();
            }
        });
        dialogcw.show();

    }


    public class MyBarDataset extends BarDataSet {

        private List<Float> credits;

        MyBarDataset(List<BarEntry> yVals, String label, List<Float> credits) {
            super(yVals, label);
            this.credits = credits;
        }

        @Override
        public int getColor(int index) {
            float c = credits.get(index);

            if (c > 80) {
                return mColors.get(0);
            } else if (c > 60) {
                return mColors.get(1);
            } else if (c > 40) {
                return mColors.get(2);
            } else if (c > 20) {
                return mColors.get(3);
            } else {
                return mColors.get(4);
            }

        }
    }

    private class MyXAxisValueFormatter extends ValueFormatter {
        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
            Log.d("Tag", "monthly clicked hi");
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int) value];
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isChangingConfigurations()) {
            deleteTempFiles(getCacheDir());
        }
    }

    private boolean deleteTempFiles(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        deleteTempFiles(f);
                    } else {
                        f.delete();
                    }
                }
            }
        }
        return file.delete();
    }


}