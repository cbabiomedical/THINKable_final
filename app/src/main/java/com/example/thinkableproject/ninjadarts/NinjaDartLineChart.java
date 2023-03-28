package com.example.thinkableproject.ninjadarts;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thinkableproject.BroadcastReceiver_BTLE_GATT;
import com.example.thinkableproject.GameActivity;
import com.example.thinkableproject.R;
import com.example.thinkableproject.User;
import com.example.thinkableproject.sample.JsonPlaceHolder;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NinjaDartLineChart extends AppCompatActivity {

    TextView score, totalScore;
    FirebaseUser mUser;
    int a;
    int i;

    int g;
    User user;
    int updatedCoins;
    Double sum = 0.0;
    Double average = 0.0;
    FirebaseFirestore database;
    RelativeLayout relativeLayout;
    Dialog dialog;
    JsonPlaceHolder jsonPlaceHolder;
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;
    String s1;
    int c;
    ImageView close;
    ArrayList<Float> xVal = new ArrayList();
    ArrayList<Float> yVal = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ninja_dart_line_chart);
        score = findViewById(R.id.score);
        totalScore = findViewById(R.id.highscore);
        dialog = new Dialog(this);
        relativeLayout = findViewById(R.id.mainLayout);
        close = findViewById(R.id.close);
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GameActivity.class));
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NinjaDartsMainActivity.class));
            }
        });
        SharedPreferences shs = getSharedPreferences("prefsTimeConWH", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

        c = shs.getInt("firstStartTimeConWH", 0);
        Log.d("CVAL", String.valueOf(c));
        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
        Log.d("MONTH", String.valueOf(now.get(Calendar.MONTH)));
        Log.d("YEAR", String.valueOf(now.get(Calendar.YEAR)));
        Log.d("DAY", String.valueOf(now.get(Calendar.DAY_OF_MONTH)));

        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH) + 1;
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(new Date());
        Log.d("NinjaDartIndex", String.valueOf(BroadcastReceiver_BTLE_GATT.concentration_indexesND));

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120000, TimeUnit.SECONDS)
                .readTimeout(120000, TimeUnit.SECONDS).build();

        if (BroadcastReceiver_BTLE_GATT.concentration_indexesND.size() > 0) {

            Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.8.104:5000/").client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            jsonPlaceHolder = retrofit.create(JsonPlaceHolder.class);
            //Post Time to Concentrate and Time stayed Concentrated
            ArrayList data = new ArrayList((Arrays.asList(0.5, 0.6, 0.7, 0.3, 0.8, 0.55, 0.65, 0.75, 0.45, 0.7, 0.65, 0.55, 0.1, 0.45)));
            Call<List> callMusRel = jsonPlaceHolder.PostTimeToConcentrate(BroadcastReceiver_BTLE_GATT.concentration_indexesND);
            callMusRel.enqueue(new Callback<List>() {
                @Override
                public void onResponse(Call<List> call, Response<List> response) {
                    Toast.makeText(getApplicationContext(), "Post Space Successful", Toast.LENGTH_SHORT).show();
                    Log.d("SUMCONResponseTime DH", String.valueOf(response.code()));
                    Log.d("SUMCONResTime Message", response.message());
                    Log.d("SUMCONResTime Body", String.valueOf(response.body()));
                    List com = response.body();
                    LinkedTreeMap hashmap = new LinkedTreeMap();
                    hashmap = (LinkedTreeMap) com.get(0);
                    Log.d("SUMCONH", String.valueOf(hashmap.get("time_to_concentrate")));

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TimeTo").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
                    databaseReference.setValue(hashmap.get("time_to_concentrate"));
                    DatabaseReference databaseReferencet = FirebaseDatabase.getInstance().getReference("TimeStayed").child("Concentration").child(mUser.getUid()).child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
                    databaseReferencet.setValue(hashmap.get("time concentrated"));

                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("occupation");
                    LinkedTreeMap finalHashmap = hashmap;
                    reference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.d("GETOCCUPATION", String.valueOf(snapshot.getValue()));
                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("WhereAmI").child(String.valueOf(snapshot.getValue())).child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                            reference2.setValue(finalHashmap.get("time_to_concentrate"));
                            DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("WhereAmI").child(String.valueOf(snapshot.getValue())).child("Time Stayed Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(mUser.getUid()).child(String.valueOf(c));
                            reference3.setValue(finalHashmap.get("time concentrated"));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    DatabaseReference referenceAge = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("dob");
                    LinkedTreeMap finalHashmap1 = hashmap;
                    referenceAge.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.d("Ageof User", String.valueOf(snapshot.getValue()));
                            String date = snapshot.getValue().toString();
                            String[] splited = date.split(" ");
                            Log.d("DateHR", splited[2]);
                            int year = Integer.parseInt(splited[2]);
                            int currentYear = now.get(Calendar.YEAR);
                            int age = currentYear - year;
                            Log.d("Age", String.valueOf(age));

                            if (age >= 10 && age <= 20) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("10-20").child(mUser.getUid()).child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
                                reference.setValue(finalHashmap1.get("time_to_concentrate"));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("10-20").child(mUser.getUid()).child("Time Stayed Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
                                reference1.setValue(finalHashmap1.get("time concentrated"));

                            } else if (age > 20 && age <= 30) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("20-30").child(mUser.getUid()).child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
                                reference.setValue(finalHashmap1.get("time_to_concentrate"));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("20-30").child(mUser.getUid()).child("Time Stayed Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
                                reference1.setValue(finalHashmap1.get("time concentrated"));

                            } else if (age > 30 && age <= 40) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("30-40").child(mUser.getUid()).child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
                                reference.setValue(finalHashmap1.get("time_to_concentrate"));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("30-40").child(mUser.getUid()).child("Time Stayed Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
                                reference1.setValue(finalHashmap1.get("time concentrated"));

                            } else if (age > 40 && age <= 50) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("40-50").child(mUser.getUid()).child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
                                reference.setValue(finalHashmap1.get("time_to_concentrate"));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("40-50").child(mUser.getUid()).child("Time Stayed Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
                                reference1.setValue(finalHashmap1.get("time concentrated"));

                            } else if (age > 50 && age <= 60) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("50-60").child(mUser.getUid()).child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
                                reference.setValue(finalHashmap1.get("time_to_concentrate"));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("50-60").child(mUser.getUid()).child("Time Stayed Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
                                reference1.setValue(finalHashmap1.get("time concentrated"));

                            } else if (age > 60 && age <= 70) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("60-70").child(mUser.getUid()).child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
                                reference.setValue(finalHashmap1.get("time_to_concentrate"));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("60-70").child(mUser.getUid()).child("Time Stayed Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
                                reference1.setValue(finalHashmap1.get("time concentrated"));

                            } else if (age > 70 && age <= 80) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("70-80").child(mUser.getUid()).child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
                                reference.setValue(finalHashmap1.get("time_to_concentrate"));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("70-80").child(mUser.getUid()).child("Time Stayed Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
                                reference1.setValue(finalHashmap1.get("time concentrated"));

                            } else if (age > 80 && age <= 90) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WhereAmI").child("80-90").child(mUser.getUid()).child("Time to Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
                                reference.setValue(finalHashmap1.get("time_to_concentrate"));
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("WhereAmI").child("80-90").child(mUser.getUid()).child("Time Stayed Concentrate").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
                                reference1.setValue(finalHashmap1.get("time concentrated"));
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    Log.d("SUMCOGETI", String.valueOf(com.get(0)));

                }

                @Override
                public void onFailure(Call<List> call, Throwable t) {
//                                    Toast.makeText(context, "Failed Post", Toast.LENGTH_SHORT).show();
                    Log.d("ErrorValSpa", String.valueOf(t));
                }
            });
        }

        if (BroadcastReceiver_BTLE_GATT.concentration_indexesND.size() > 0) {
            for (int j = 0; j < BroadcastReceiver_BTLE_GATT.concentration_indexesND.size(); j++) {
                sum += (Double) BroadcastReceiver_BTLE_GATT.concentration_indexesND.get(j);
            }
            Log.d("SUMCONCS", String.valueOf(sum));
            average = sum / BroadcastReceiver_BTLE_GATT.concentration_indexesND.size();
            Log.d("SUMCONCA", String.valueOf(average));
            Double averageD = Double.valueOf(String.format("%.3g%n", average));

            DatabaseReference referenceIntervention = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("NinjaDartIntervention").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(c));
            referenceIntervention.setValue(averageD);
        }
        if (BroadcastReceiver_BTLE_GATT.concentration_indexesND.size() == 0) {
            average = 0.0;
        }

        SharedPreferences sha = getSharedPreferences("Sec", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show
        int sec = sha.getInt("sec", 0);
        Log.d("SecNinjaLineChart", String.valueOf(sec));

        SharedPreferences sh = getSharedPreferences("prefsTimeConWH", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

        g = sh.getInt("firstStartTimeConWH", 0);

// We can then use the data
        Log.d("A Count", String.valueOf(g));

        int y = g + 1;

        SharedPreferences prefsCount1 = getSharedPreferences("prefsTimeConWH", MODE_PRIVATE);
        SharedPreferences.Editor editor3 = prefsCount1.edit();
        editor3.putInt("firstStartTimeCon", y);
        editor3.apply();
        SharedPreferences sha5 = getSharedPreferences("prefsTimeConWH", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

        int x1 = sha5.getInt("firstStartTimeConWH", 0);


//prints day name
        System.out.println("Day Name: " + str);
        Log.d("Day Name", str);
        database = FirebaseFirestore.getInstance();


        SharedPreferences shh = getSharedPreferences("prefsCountNinja", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

        a = shh.getInt("firstStartCountNinja", 0);

// We can then use the data
        Log.d("A Count", String.valueOf(a));

        int b = a + 1;


        SharedPreferences prefsCount2 = getSharedPreferences("prefsCountNinja", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsCount2.edit();
        editor.putInt("firstStartCountNinja", b);
        editor.apply();
        SharedPreferences sha1 = getSharedPreferences("prefsCountNinja", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

        int a1 = sha1.getInt("firstStartCountNinja", 0);


        SharedPreferences sh2 = getSharedPreferences("Score", MODE_APPEND);
        mUser = FirebaseAuth.getInstance().getCurrentUser();

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show
        s1 = sh2.getString("name", "");
        Log.d("ScoreVal", s1);
        score.setText("Scores: " + s1);

        SharedPreferences sh1 = getSharedPreferences("HighScore", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show
        String s2 = sh1.getString("highscore", "");
        Log.d("HighScoreVal", s2);

        totalScore.setText("HighScore: " + s2);

//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Ninja Dart").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(a));
//        reference.setValue(Long.parseLong(s1));


        database.collection("users")
                .document(mUser.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
//                binding.currentCoins.setText(String.valueOf(user.getCoins()));
                Log.d("Current Coins", String.valueOf(user.getCoins()));
                Log.d("High Score Inside", String.valueOf(Long.parseLong(s1)));
                updatedCoins = (int) (user.getCoins() + Long.parseLong(s1));
                Log.d("Updated High Score", String.valueOf(updatedCoins));
//                binding.currentCoins.setText(user.getCoins() + "");
                database.collection("users").document(mUser.getUid())
                        .update("coins", updatedCoins).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(ColorPatternGame.this, "Successfully Updated Coins", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Error", String.valueOf(e));
//                        Toast.makeText(ColorPatternGame.this, "Failed to Update Coins", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
        DatabaseReference referenceTime = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(g));
        Log.d("ReferencePath", String.valueOf(referenceTime));
        referenceTime.setValue(sec);
        openPopUpIntervention();


    }

    private void openPopUpIntervention() {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
        Log.d("MONTH", String.valueOf(now.get(Calendar.MONTH)));
        Log.d("YEAR", String.valueOf(now.get(Calendar.YEAR)));
        Log.d("DAY", String.valueOf(now.get(Calendar.DAY_OF_MONTH)));

        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH) + 1;
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(new Date());
        Button ok;
        LineChart lineChart;
        TextView coinsGained, totalCoins;
        dialog.setContentView(R.layout.game_intervention_popup);
        ok = (Button) dialog.findViewById(R.id.ok);
        lineChart = (LineChart) dialog.findViewById(R.id.lineChartInterventionGame);
        coinsGained = (TextView) dialog.findViewById(R.id.points);
        totalCoins = (TextView) dialog.findViewById(R.id.total);

//        coinsGained.setText("Scores" + s1);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        lineEntries = new ArrayList();

        DatabaseReference referenceIntervention = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("NinjaDartIntervention").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str);
        referenceIntervention.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.d("XVAL", dataSnapshot.getKey());
                    float xxVal = (Float.parseFloat(dataSnapshot.getKey()));

                    Log.d("XArrayList", String.valueOf(xVal));
                    float yyVal = (Float.parseFloat(String.valueOf((Double) dataSnapshot.getValue())));
                    Log.d("YVAL", String.valueOf(yyVal));
                    Log.d("YArrayList", String.valueOf(yVal));
                    lineEntries.add(new Entry(xxVal, yyVal));

                    lineDataSet = new LineDataSet(lineEntries, "NinjaDart Progress on " + str);
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
                    lineChart.getXAxis().setTextColor(R.color.white);
                    lineChart.getAxisRight().setTextColor(getResources().getColor(R.color.white));
                    lineChart.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                    lineChart.getLegend().setTextColor(getResources().getColor(R.color.white));
                    lineChart.getDescription().setTextColor(R.color.white);
                    lineChart.invalidate();
                    lineChart.refreshDrawableState();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void getEntries() {
        lineEntries = new ArrayList();
        lineEntries.add(new Entry(2f, 34f));
        lineEntries.add(new Entry(4f, 56f));
        lineEntries.add(new Entry(6f, 65));
        lineEntries.add(new Entry(8f, 23f));

    }
}