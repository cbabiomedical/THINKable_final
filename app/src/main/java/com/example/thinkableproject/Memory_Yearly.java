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

public class Memory_Yearly extends AppCompatActivity {

    Dialog dialogcy;
    BarChart barChart2;
    AppCompatButton daily, weekly, monthly,improveConcentration;
    LottieAnimationView realTime;
    ImageView relaxationBtn, concentrationBtn, game, meditation, music;
    FirebaseUser mUser;
    File localFile, fileName;
    View c1, c2;
    GifImageView c1gif, c2gif;
    HorizontalScrollView scrollView;
    String text;
    Animation scaleUp, scaleDown;

    ArrayList<String> list = new ArrayList<>();
    ArrayList<Float> floatList = new ArrayList<>();
    int color;
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;
    AppCompatButton improvement, progressTime;
    Long average1, average2, average3, average;
    int sum1, sum2, sum3, sum4;

    ArrayList<Float> xVal = new ArrayList<>(Arrays.asList(2f, 4f, 6f, 8f, 10f, 12f, 14f));
    ArrayList<Float> yVal = new ArrayList(Arrays.asList(45f, 36f, 75f, 36f, 73f, 45f, 83f));
    ArrayList<String> xnewVal = new ArrayList<>();
    ArrayList<String> ynewVal = new ArrayList<>();
    ArrayList<Float> floatxVal = new ArrayList<>();
    ArrayList<Float> floatyVal = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory__yearly);

        barChart2 = (BarChart) findViewById(R.id.barChartYearly);
        daily = findViewById(R.id.daily);
        weekly = findViewById(R.id.weekly);
        monthly = findViewById(R.id.monthly);
        realTime = findViewById(R.id.animation);
        music = findViewById(R.id.music);
        relaxationBtn = findViewById(R.id.relaxation);
        concentrationBtn = findViewById(R.id.concentration);
        game = findViewById(R.id.game);
        meditation = findViewById(R.id.meditations);
        List<BarEntry> entries = new ArrayList<>();
        c1gif = findViewById(R.id.landingfwall);
        improveConcentration=findViewById(R.id.improveConcentration);

        c2gif = findViewById(R.id.landingfwall1);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        lineChart = findViewById(R.id.lineChartYearly);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.sacale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scrollView = findViewById(R.id.scroll);
        improvement = findViewById(R.id.improvement);
        progressTime = findViewById(R.id.progressTime);
        //Initialize bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        dialogcy = new Dialog(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

        improvement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);

                scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);

            }
        });
        improvement.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    improvement.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    improvement.startAnimation(scaleDown);
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

        getEntries();


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

        meditation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MeditationMemory.class));
            }
        });

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MusicMemory.class));
            }
        });
        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GamesMemory.class));
            }
        });

        Log.d("Outside", String.valueOf(floatList));
        //onClick listener for daily button
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Memory_Daily.class);
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

        //onClick listener for monthly button
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MemoryMonthly.class);
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
        //onClick listener for weekly button
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Memory_Weekly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        weekly.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    weekly.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    weekly.startAnimation(scaleDown);
                }

                return false;
            }
        });

        //onClick listener for relaxation toggle button
        relaxationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Relaxation_Yearly.class);
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
        concentrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Concentration_Yearly.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        concentrationBtn.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    concentrationBtn.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    concentrationBtn.startAnimation(scaleDown);
                }

                return false;
            }
        });
        //onClick listener for real time indication button
        realTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Calibration.class);
                startActivity(intent);
            }
        });

        realTime.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    realTime.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    realTime.startAnimation(scaleDown);
                }

                return false;
            }
        });

        //getting current user id from
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();
        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
        Log.d("MONTH", String.valueOf(now.get(Calendar.MONTH)));
        Log.d("YEAR", String.valueOf(now.get(Calendar.YEAR)));
        Log.d("DAY", String.valueOf(now.get(Calendar.DAY_OF_WEEK)));
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(new Date());
        int year1 = now.get(Calendar.YEAR) - 3;
        int year2 = now.get(Calendar.YEAR) - 2;
        int year3 = now.get(Calendar.YEAR) - 1;
        //prints day name
        System.out.println("Day Name: " + str);
        Log.d("Day Name", str);

        Handler handler = new Handler();
        final int delay = 5000;
        String[] years = new String[]{"",String.valueOf(year1), String.valueOf(year2), String.valueOf(year3), String.valueOf(now.get(Calendar.YEAR))};
        ArrayList<Float> creditsMain = new ArrayList<>(Arrays.asList(90f, 30f, 70f, 50f));

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Memory Games").child(String.valueOf(year1));
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList sumElement = new ArrayList();
                        int sum = (0);
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                    for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                        Log.d("Yearly 1", String.valueOf(snapshot3.getValue()));
                                        Long av1 = (Long) snapshot3.getValue();
                                        sumElement.add(snapshot1.getValue());
                                        sum1 += av1;
                                    }
                                }
                            }
                        }
                        Log.d("SUM", String.valueOf(sum1));
                        if (sum1 != 0) {
                            average1 = sum1 / Long.parseLong(String.valueOf(sumElement.size()));
                        } else {
                            average1 = 0L;
                        }

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Memory Games").child(String.valueOf(year2));
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList sumElement = new ArrayList();
                                int sum = (0);
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                            for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                                Log.d("Yearly 2", String.valueOf(snapshot3.getValue()));
                                                Long av1 = (Long) snapshot3.getValue();
                                                sumElement.add(snapshot1.getValue());
                                                sum2 += av1;
                                            }
                                        }
                                    }
                                }
                                Log.d("SUM", String.valueOf(sum2));
                                if (sum2 != 0) {
                                    average2 = sum2 / Long.parseLong(String.valueOf(sumElement.size()));
                                } else {
                                    average2 = 0L;
                                }

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Memory Games").child(String.valueOf(year3));
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList sumElement = new ArrayList();
                                        int sum = (0);
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                    for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                                        Log.d("Yearly 3", String.valueOf(snapshot3.getValue()));
                                                        Long av1 = (Long) snapshot3.getValue();
                                                        sumElement.add(snapshot1.getValue());
                                                        sum3 += av1;
                                                    }
                                                }
                                            }
                                        }
                                        Log.d("SUM", String.valueOf(sum3));
                                        if (sum3 != 0) {
                                            average3 = sum3 / Long.parseLong(String.valueOf(sumElement.size()));
                                        } else {
                                            average3 = 0L;
                                        }
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Memory Games").child(String.valueOf(now.get(Calendar.YEAR)));
                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ArrayList sumElement = new ArrayList();
                                                int sum = (0);
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                            for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                                                Log.d("Yearly 4", String.valueOf(snapshot3.getValue()));
                                                                Long av1 = (Long) snapshot3.getValue();
                                                                sumElement.add(snapshot1.getValue());
                                                                sum4 += av1;
                                                            }
                                                        }
                                                    }
                                                }
                                                Log.d("SUM", String.valueOf(sum4));
                                                if (sum4 != 0) {
                                                    average = sum4 / Long.parseLong(String.valueOf(sumElement.size()));
                                                } else {
                                                    average = 0L;
                                                }
                                                Log.d("Average", String.valueOf(average));
                                                Log.d("Average1", String.valueOf(average1));
                                                Log.d("Average2", String.valueOf(average2));
                                                Log.d("Average4", String.valueOf(average3));

                                                float textSize = 10f;
                                                float sum1min = TimeUnit.SECONDS.toMinutes(sum1);
                                                float sum2min = TimeUnit.SECONDS.toMinutes(sum2);
                                                float sum3min = TimeUnit.SECONDS.toMinutes(sum3);
                                                float sum4min = TimeUnit.SECONDS.toMinutes(sum4);

                                                entries.add(new BarEntry(1, sum1min));
                                                entries.add(new BarEntry(2, sum2min));
                                                entries.add(new BarEntry(3, sum3min));
                                                entries.add(new BarEntry(4, sum4min));
                                                //Initializing object of MyBarDataset class
                                                MyBarDataset dataSet = new MyBarDataset(entries, "data", creditsMain);
                                                dataSet.setColors(ContextCompat.getColor(getApplicationContext(), R.color.Bwhite),
                                                        ContextCompat.getColor(getApplicationContext(), R.color.Lblue),
                                                        ContextCompat.getColor(getApplicationContext(), R.color.blue),
                                                        ContextCompat.getColor(getApplicationContext(), R.color.bluebar),
                                                        ContextCompat.getColor(getApplicationContext(), R.color.dark));
                                                BarData data = new BarData(dataSet);
                                                data.setDrawValues(false);
                                                data.setBarWidth(0.8f);

                                                barChart2.setData(data);
                                                barChart2.setFitBars(true);
                                                barChart2.getXAxis().setValueFormatter(new IndexAxisValueFormatter(years));
                                                barChart2.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                                barChart2.getXAxis().setTextSize(textSize);
                                                barChart2.getAxisLeft().setTextSize(textSize);
                                                barChart2.setExtraBottomOffset(5f);
                                                barChart2.getXAxis().setAvoidFirstLastClipping(true);
                                                barChart2.getXAxis().setLabelCount(9, true);

                                                barChart2.getAxisRight().setEnabled(false);
                                                Description desc = new Description();
                                                desc.setText("Time Spent Yearly on Memory");
                                                desc.setTextColor(getResources().getColor(R.color.white));
                                                barChart2.setDescription(desc);
                                                barChart2.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                                barChart2.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                                barChart2.getLegend().setEnabled(false);
                                                barChart2.getXAxis().setDrawGridLines(false);
                                                barChart2.getAxisLeft().setDrawGridLines(false);
                                                barChart2.setNoDataText("Data Loading Please Wait....");
                                                barChart2.animateXY(1500, 1500);


                                                barChart2.invalidate();


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
        try {
            fileName = new File(getCacheDir() + "/memYearlyX.txt");  //Writing data to file
            FileWriter fw;
            fw = new FileWriter(fileName);
            BufferedWriter output = new BufferedWriter(fw);
            int size = xVal.size();
            for (int i = 0; i < size; i++) {
                output.write(xVal.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing X Data", Toast.LENGTH_SHORT).show();
            }
            output.close();
        } catch (IOException exception) {
//            Toast.makeText(this, "Failed Writing X Data", Toast.LENGTH_SHORT).show();
            exception.printStackTrace();
        }

//
        mUser = FirebaseAuth.getInstance().getCurrentUser(); // get current user
        mUser.getUid();
//
//        // Uploading saved data containing file to firebase storage
        StorageReference storageXAxis = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageXAxis.child("memYearlyX.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(Memory_Yearly.this, "File Uploaded X data", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(Memory_Yearly.this, "File Uploading Failed X", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//
        try {
            fileName = new File(getCacheDir() + "/memYearlyY.txt");  //Writing data to file
            FileWriter fw;
            fw = new FileWriter(fileName);
            BufferedWriter output = new BufferedWriter(fw);
            int size = yVal.size();
            for (int i = 0; i < size; i++) {
                output.write(yVal.get(i).toString() + "\n");
//                Toast.makeText(this, "Success Writing Y data", Toast.LENGTH_SHORT).show();
            }
            output.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        StorageReference storageYAxis = FirebaseStorage.getInstance().getReference(mUser.getUid());
        try {
            StorageReference mountainsRef = storageYAxis.child("memYearlyY.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(Memory_Yearly.this, "File Uploaded Y Axis", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(Memory_Yearly.this, "File Uploading Failed Y Data", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    private void getEntries() {
        Handler handler = new Handler();
        final int delay = 5000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                lineEntries = new ArrayList();
//        Handler handler1=new Handler();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/memYearlyX.txt");
                //Downloading file from firebase and storing data into a tempFile in cache memory
                try {
                    localFile = File.createTempFile("tempFileX", ".txt");
                    text = localFile.getAbsolutePath();
                    Log.d("Bitmap", text);
                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(Memory_Yearly.this, "Success", Toast.LENGTH_SHORT).show();

                            // reading data from the tempFile and storing in array list

                            try {
                                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(localFile.getAbsolutePath()));

                                Log.d("FileName", localFile.getAbsolutePath());

                                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                                String line = "";

                                Log.d("First", line);
                                if ((line = bufferedReader.readLine()) != null) {
                                    xnewVal.add(line);
                                }
                                while ((line = bufferedReader.readLine()) != null) {

                                    xnewVal.add(line);
                                    Log.d("Line", line);
                                }

                                Log.d("X New Val", String.valueOf(xnewVal));
                                //Converting string arraylist to float array list
                                for (int i = 0; i < xnewVal.size(); i++) {
                                    floatxVal.add(Float.parseFloat(xnewVal.get(i)));
                                    Log.d("FloatXVal", String.valueOf(floatxVal));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            StorageReference storageReference1 = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/memYearlyY.txt");
//        //Downloading file from firebase and storing data into a tempFile in cache memory
                            try {
                                localFile = File.createTempFile("tempFileY", ".txt");
                                text = localFile.getAbsolutePath();
                                Log.d("Bitmap", text);
                                storageReference1.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(Concentration_Daily.this, "Success", Toast.LENGTH_SHORT).show();

                                        // reading data from the tempFile and storing in array list

                                        try {
                                            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(localFile.getAbsolutePath()));

                                            Log.d("FileName", localFile.getAbsolutePath());

                                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                                            String line = "";

                                            Log.d("First", line);
                                            if ((line = bufferedReader.readLine()) != null) {
                                                ynewVal.add(line);
                                            }
                                            while ((line = bufferedReader.readLine()) != null) {

                                                ynewVal.add(line);
                                                Log.d("Line", line);
                                            }

                                            Log.d("YVal", String.valueOf(ynewVal));
                                            //Converting string arraylist to float array list
                                            for (int i = 0; i < ynewVal.size(); i++) {
                                                floatyVal.add(Float.parseFloat(ynewVal.get(i)));
                                                Log.d("OutX", String.valueOf(floatxVal));
                                                Log.d("FloatYArray", String.valueOf(floatyVal));
                                                Log.d("OutY", String.valueOf(floatyVal));


                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        Log.d("floatYVal", String.valueOf(floatyVal));

                                        for (int x = 0; x < floatxVal.size(); x++) {

                                            lineEntries.add(new Entry(floatxVal.get(x), floatyVal.get(x)));

                                        }
                                        Log.d("Line Entry", String.valueOf(lineEntries));
                                        lineDataSet = new LineDataSet(lineEntries, "Yearly Memory Index");
                                        lineData = new LineData(lineDataSet);
                                        lineChart.setData(lineData);

                                        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                                        lineDataSet.setValueTextColor(Color.WHITE);
                                        lineDataSet.setValueTextSize(10f);

                                        lineChart.setGridBackgroundColor(Color.TRANSPARENT);
                                        lineChart.setBorderColor(Color.TRANSPARENT);
                                        lineChart.setGridBackgroundColor(Color.TRANSPARENT);
                                        lineChart.getAxisLeft().setDrawGridLines(false);
                                        lineChart.getXAxis().setDrawGridLines(false);
                                        lineChart.getAxisRight().setDrawGridLines(false);
                                        lineChart.getAxisRight().setTextColor(getResources().getColor(R.color.white));
                                        lineChart.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                        lineChart.getLegend().setTextColor(getResources().getColor(R.color.white));
                                        lineChart.getDescription().setTextColor(R.color.white);
                                        lineChart.invalidate();
                                        lineChart.refreshDrawableState();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(Concentration_Daily.this, "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            } catch (IOException exception) {
                                exception.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Memory_Yearly.this, "Failed X", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (IOException exception) {
                    exception.printStackTrace();
                }

            }
        }, 10000);


    }

    //popup window method to provide suggesstions to improve concentration
    public void gotoPopup4m(View view) {

        startActivity(new Intent(getApplicationContext(), Connection.class));
//        ImageButton imageViewcancle, imageViewmed, imageViewsong, imageViewgames;
//        View c1, c2;
//        FirebaseUser mUser;
//
//        dialogcy.setContentView(R.layout.memory_popup);
//
//        c1 = (View) dialogcy.findViewById(R.id.c1);
//        c2 = (View) dialogcy.findViewById(R.id.c2);
//
//        mUser = FirebaseAuth.getInstance().getCurrentUser();
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
//        imageViewmed = (ImageButton) dialogcy.findViewById(R.id.meditationpop1);
//        imageViewmed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MeditationExercise.class);
//                startActivity(intent);
//            }
//        });
//
//        imageViewsong = (ImageButton) dialogcy.findViewById(R.id.musicpop1);
//        imageViewsong.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), Music.class);
//                startActivity(intent);
//            }
//        });
//        imageViewgames = (ImageButton) dialogcy.findViewById(R.id.gamespop1);
//        imageViewgames.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), GameActivity.class));
//            }
//        });
//
//        imageViewcancle = (ImageButton) dialogcy.findViewById(R.id.canclepop1);
//        imageViewcancle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogcy.dismiss();
//            }
//        });
//
//        dialogcy.show();
    }

    public void caliyearly(View view) {
        Intent intentcy = new Intent(Memory_Yearly.this, Calibration.class);

        startActivity(intentcy);
    }


    public class MyBarDataset extends BarDataSet {

        private List<Float> creditsWeek;

        MyBarDataset(List<BarEntry> yVals, String label, List<Float> creditsWeek) {
            super(yVals, label);
            this.creditsWeek = creditsWeek;
        }

        @Override
        public int getColor(int index) {
            float c = creditsWeek.get(index);

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
//    private ArrayList readWrite(){
//
//
//    }


}