package com.example.thinkableproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
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
import com.example.thinkableproject.databinding.ActivityMainBinding;
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

public class Relaxation_Weekly extends AppCompatActivity {

    Dialog dialogrw;

    BarChart barChart, barChart1, barChart2;
    private Context context;
    AppCompatButton daily, yearly, monthly, improveConcentration;
    LottieAnimationView realTime;
    ImageView concentration, memoryBtn;
    ImageView music, meditation, video;
    FirebaseUser mUser;
    View c1, c2;
    GifImageView c1gif, c2gif;
    String text;
    File localFile;
    Animation scaleUp, scaleDown;

    File fileName;
    HorizontalScrollView scrollView;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<Float> floatList = new ArrayList<>();
    int color;
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;
    AppCompatButton progressTime, improvement;

    ArrayList<Float> xVal = new ArrayList<>(Arrays.asList(2f, 4f, 6f, 8f, 10f, 12f, 14f));
    ArrayList<Float> yVal = new ArrayList(Arrays.asList(45f, 36f, 75f, 36f, 73f, 45f, 83f));
    Long average1, average2, average3, average4, average5;
    int sum1, sum2, sum3, sum4, sum5;
    Double averageIn1 = 0.0;
    Double averageIn2 = 0.0;
    Double averageIn3 = 0.0;
    Double averageIn4 = 0.0;
    Double averageIn5 = 0.0;
    Double sumIn1 = 0.0;
    Double sumIn2 = 0.0;
    Double sumIn3 = 0.0;
    Double sumIn4 = 0.0;
    Double sumIn5 = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relaxation_weekly);

        //Initialize List entries
        List<BarEntry> entries = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("chartTable");
        //Initialize bar chart
        barChart1 = (BarChart) findViewById(R.id.barChartWeekly);
        //Initialize buttons
        realTime = findViewById(R.id.animation);
        daily = findViewById(R.id.daily);
        improveConcentration = findViewById(R.id.improveConcentration);

        yearly = findViewById(R.id.yearly);
        monthly = findViewById(R.id.monthly);
        c1gif = findViewById(R.id.landingfwall);
        c2gif = findViewById(R.id.landingfwall1);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.sacale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        lineChart = findViewById(R.id.lineChartWeekly);
        scrollView = findViewById(R.id.scroll);
        improvement = findViewById(R.id.improvement);
        progressTime = findViewById(R.id.progressTime);
        video = findViewById(R.id.game);

        //Initialize pop up window
        dialogrw = new Dialog(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        //go to concentration weekly landing page
        concentration = findViewById(R.id.concentration);
        memoryBtn = findViewById(R.id.memory);

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

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), VideoInterventionActivity.class));
            }
        });

        getEntries();


        concentration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Concentration_Weekly.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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

        concentration.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    concentration.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    concentration.startAnimation(scaleDown);
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

        music = findViewById(R.id.music);
        meditation = findViewById(R.id.meditations);

        //go to misic-excercise page
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MusicRelaxation.class);
                startActivity(intent);
            }
        });
        //go to meditation-exercise page
        meditation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MeditationExercise.class);
                startActivity(intent);
            }
        });
        realTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Calibration.class));
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


        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getUid();

        //initialize file handler
        final Handler handler = new Handler();
        final int delay = 5000;
        String[] weeks = new String[]{"Week1", "Week2", "Week3", "Week4", "Week5"};
        ArrayList<Float> creditsMain = new ArrayList<>(Arrays.asList(90f, 30f, 70f, 50f, 80f));

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


                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Relaxation Intervention").child(String.valueOf(now.get(Calendar.YEAR)))
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
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Relaxation Intervention").child(String.valueOf(now.get(Calendar.YEAR)))
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

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Relaxation Intervention").child(String.valueOf(now.get(Calendar.YEAR)))
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

                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Relaxation Intervention").child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Relaxation Intervention").child(String.valueOf(now.get(Calendar.YEAR)))
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
                                                        Log.d("Weekly5 Array", String.valueOf(sumElement));
                                                        Log.d("Weekly5 SUM", String.valueOf(sum5));
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
                                                        entries.add(new BarEntry(5, sum5min));


                                                        //Initializing object of MyBarDataset class and passing th arraylist to y axis of chart
                                                        MyBarDataset dataSet = new MyBarDataset(entries, "data", creditsMain);
                                                        dataSet.setColors(ContextCompat.getColor(getApplicationContext(), R.color.Bwhite),
                                                                ContextCompat.getColor(getApplicationContext(), R.color.Lblue),
                                                                ContextCompat.getColor(getApplicationContext(), R.color.blue),
                                                                ContextCompat.getColor(getApplicationContext(), R.color.bluebar),
                                                                ContextCompat.getColor(getApplicationContext(), R.color.dark));
                                                        BarData data = new BarData(dataSet);
                                                        data.setDrawValues(false);
                                                        data.setBarWidth(0.8f);
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
                                                        desc.setText("Time Spent Weekly on Relaxation");
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

                                    {

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
            fileName = new File(getCacheDir() + "/relWeeklyX.txt");  //Writing data to file
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
            StorageReference mountainsRef = storageXAxis.child("relWeeklyX.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(Relaxation_Weekly.this, "File Uploaded X data", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(Relaxation_Weekly.this, "File Uploading Failed X", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//
        try {
            fileName = new File(getCacheDir() + "/relWeeklyY.txt");  //Writing data to file
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
            StorageReference mountainsRef = storageYAxis.child("relWeeklyY.txt");
            InputStream stream = new FileInputStream(new File(fileName.getAbsolutePath()));
            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(Relaxation_Weekly.this, "File Uploaded Y Axis", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(Relaxation_Weekly.this, "File Uploading Failed Y Data", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


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


        //go to relaxation daily page
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Relaxation_Daily.class);
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
        //go to relaxation monthly page
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Relaxation_Monthly.class);
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
        //go to relaxation yearly page
        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Relaxation_Yearly.class);
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
        memoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Memory_Weekly.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        memoryBtn.setOnTouchListener(new View.OnTouchListener() {


            //
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    memoryBtn.startAnimation(scaleUp);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    memoryBtn.startAnimation(scaleDown);
                }

                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

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
                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RelaxationIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                        .child(String.valueOf(month)).child(String.valueOf(1));
                reference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList sumElement = new ArrayList();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Log.d("DataSnapshotWeek1", String.valueOf(dataSnapshot1.getValue()));
                                Double av1 = (Double) dataSnapshot1.getValue();
                                sumIn1 += av1;
                                sumElement.add(av1);
                            }
                        }
                        if (sumIn1 != 0.0) {
                            Log.d("SUMWEEK1", String.valueOf(sumIn1));
                            averageIn1 = sumIn1 / sumElement.size();
                            Log.d("AVERAGEWEEK1", String.valueOf(averageIn1));
                        } else {
                            sumIn1 = 0.0;
                            averageIn1 = 0.0;
                        }

                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RelaxationIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                .child(String.valueOf(month)).child(String.valueOf(2));
                        reference2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList sumElement = new ArrayList();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        Log.d("DataSnapshotWeek2", String.valueOf(dataSnapshot1.getValue()));
                                        Double av1 = (Double) dataSnapshot1.getValue();
                                        sumIn2 += av1;
                                        sumElement.add(av1);
                                    }
                                }
                                if (sumIn2 != 0.0) {
                                    Log.d("SUMWEEK2", String.valueOf(sumIn2));
                                    averageIn2 = sumIn2 / sumElement.size();
                                    Log.d("AVERAGEWEEK2", String.valueOf(averageIn2));
                                } else {
                                    sumIn2 = 0.0;
                                    averageIn2 = 0.0;
                                }
                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RelaxationIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                        .child(String.valueOf(month)).child(String.valueOf(3));
                                reference2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList sumElement = new ArrayList();
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                Log.d("DataSnapshotWeek3", String.valueOf(dataSnapshot1.getValue()));
                                                Double av1 = (Double) dataSnapshot1.getValue();
                                                sumIn3 += av1;
                                                sumElement.add(av1);
                                            }
                                        }
                                        if (sumIn3 != 0.0) {
                                            Log.d("SUMWEEK3", String.valueOf(sumIn3));
                                            averageIn3 = sumIn3 / sumElement.size();
                                            Log.d("AVERAGEWEEK3", String.valueOf(averageIn3));
                                        } else {
                                            sumIn3 = 0.0;
                                            averageIn3 = 0.0;
                                        }
                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RelaxationIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                .child(String.valueOf(month)).child(String.valueOf(4));
                                        reference2.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ArrayList sumElement = new ArrayList();
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                        Log.d("DataSnapshotWeek4", String.valueOf(dataSnapshot1.getValue()));
                                                        Double av1 = (Double) dataSnapshot1.getValue();
                                                        sumIn4 += av1;
                                                        sumElement.add(av1);
                                                    }
                                                }
                                                if (sumIn4 != 0.0) {
                                                    Log.d("SUMWEEK4", String.valueOf(sumIn4));
                                                    averageIn4 = sumIn4 / sumElement.size();
                                                    Log.d("AVERAGEWEEK4", String.valueOf(averageIn4));
                                                } else {
                                                    sumIn4 = 0.0;
                                                    averageIn4 = 0.0;
                                                }
                                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RelaxationIndex").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR)))
                                                        .child(String.valueOf(month)).child(String.valueOf(5));
                                                reference2.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        ArrayList sumElement = new ArrayList();
                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                Log.d("DataSnapshotWeek5", String.valueOf(dataSnapshot1.getValue()));
                                                                Double av1 = (Double) dataSnapshot1.getValue();
                                                                sumIn5 += av1;
                                                                sumElement.add(av1);
                                                            }
                                                        }
                                                        if (sumIn5 != 0.0) {
                                                            Log.d("SUMWEEK5", String.valueOf(sumIn5));
                                                            averageIn5 = sumIn5 / sumElement.size();
                                                            Log.d("AVERAGEWEEK5", String.valueOf(averageIn5));
                                                        } else {
                                                            sumIn5 = 0.0;
                                                            averageIn5 = 0.0;
                                                        }
                                                        lineEntries.add(new Entry(1, Float.parseFloat(String.valueOf(averageIn1))));
                                                        lineEntries.add(new Entry(2, Float.parseFloat(String.valueOf(averageIn2))));
                                                        lineEntries.add(new Entry(3, Float.parseFloat(String.valueOf(averageIn3))));
                                                        lineEntries.add(new Entry(4, Float.parseFloat(String.valueOf(averageIn4))));
                                                        lineEntries.add(new Entry(5, Float.parseFloat(String.valueOf(averageIn5))));
                                                        String xAxisValues[] = {"", "WK1", "WK2", "WK3", "WK4", "WK5", "", ""};

                                                        lineDataSet = new LineDataSet(lineEntries, "Relaxation Weekly Progress");
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
    }

    public void caliweekly1(View view) {

        Intent intentrw = new Intent(Relaxation_Weekly.this, Calibration.class);

        startActivity(intentrw);

    }

    //improve relaxation pop up window
    public void gotoPopup7(View view) {
        startActivity(new Intent(getApplicationContext(), Connection.class));
//        ImageButton imageViewcancle, imageViewmed, imageViewsong, imageViewvdo, imageViewbw, imageViewit;
//        View c1, c2;
//        FirebaseUser mUser;
//
//        dialogrw.setContentView(R.layout.activity_relaxation_popup);
//
//        mUser = FirebaseAuth.getInstance().getCurrentUser();
//        c1 = (View) dialogrw.findViewById(R.id.c1);
//        c2 = (View) dialogrw.findViewById(R.id.c2);
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
//        imageViewmed = (ImageButton) dialogrw.findViewById(R.id.medipop1);
//        imageViewmed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MeditationExercise.class);
//                startActivity(intent);
//            }
//        });
//
//        imageViewsong = (ImageButton) dialogrw.findViewById(R.id.songspop1);
//        imageViewsong.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), Music.class);
//                startActivity(intent);
//            }
//        });
//
//        imageViewvdo = (ImageButton) dialogrw.findViewById(R.id.vdospop1);
//        imageViewvdo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), Video.class);
//                startActivity(intent);
//            }
//        });
//
//        imageViewbw = (ImageButton) dialogrw.findViewById(R.id.bipop1);
//        imageViewbw.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), BineuralAcivity.class);
//                startActivity(intent);
//            }
//        });
//
//        imageViewit = (ImageButton) dialogrw.findViewById(R.id.canpop1);
//        imageViewit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), Relaxation_Weekly.class);
//                startActivity(intent);
//            }
//        });
//
//        imageViewcancle = (ImageButton) dialogrw.findViewById(R.id.canpop1);
//        imageViewcancle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogrw.dismiss();
//            }
//        });
//
//
//        dialogrw.show();
    }

    public void gotoPopup6(View view) {
    }

//    public void yearly(View v) {
//        Intent intent2 = new Intent(this, Relaxation_Yearly.class);
//        startActivity(intent2);
//
//    }
//
//    public void daily(View view) {
//        Intent intent2 = new Intent(this, Relaxation_Daily.class);
//        startActivity(intent2);
//    }
//
//    public void monthly(View view) {
//        Intent intent2 = new Intent(this, Relaxation_Monthly.class);
//        startActivity(intent2);
//    }

    //set up x and y axis data
    public class MyBarDataset extends BarDataSet {

        private List<Float> credits;

        MyBarDataset(List<BarEntry> yVals, String label, List<Float> credits) {
            super(yVals, label);
            this.credits = credits;
        }

        //set up color of bars on chart
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

    //delete temporary file
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