package com.example.thinkableproject.puzzle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thinkableproject.R;
import com.example.thinkableproject.User;
import com.example.thinkableproject.puzzle.Class.Cards;
import com.example.thinkableproject.puzzle.Class.DataBase;
import com.example.thinkableproject.puzzle.Class.SlicingImage;
import com.example.thinkableproject.puzzle.Class.Sound;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GameActivity15 extends AppCompatActivity {

    private final int N = 4;
    Cards cards;
    int a, points;
    FirebaseUser mUser;
    User user;
    LineData lineData;
    int x;
    LineDataSet lineDataSet;
    ArrayList lineEntries;
    int updatedCoins;
    Long startTime, endTime;
    FirebaseFirestore database;
    public static boolean isStarted = false;
    private ImageButton[][] button;
    private final int[][] BUTTON_ID = {{R.id.b1500, R.id.b1501, R.id.b1502, R.id.b1503},
            {R.id.b1510, R.id.b1511, R.id.b1512, R.id.b1513},
            {R.id.b1520, R.id.b1521, R.id.b1522, R.id.b1523},
            {R.id.b1530, R.id.b1531, R.id.b1532, R.id.b1533}};
    private final int[] CARDS_ID = {R.drawable.card1500, R.drawable.card1501, R.drawable.card1502, R.drawable.card1503,
            R.drawable.card1504, R.drawable.card1505, R.drawable.card1506, R.drawable.card1507,
            R.drawable.card1508, R.drawable.card1509, R.drawable.card1510, R.drawable.card1511,
            R.drawable.card1512, R.drawable.card1513, R.drawable.card1514, R.drawable.card1515};

    private TextView scoreTV;
    private int numOfSteps;
    private TextView recordTV;
    private int recordSteps;
    private ImageButton soundBtn;
    private boolean check;
    private String whatToShow;

    DataBase dataBase = new DataBase(this);
    Sound sound = new Sound();

    @Override
    protected void onStart() {
        super.onStart();
        if (!Sound.gameMusic.isPlaying())
            sound.switchMusic(Sound.gameMusic, Sound.backgroundMusic);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!Sound.activitySwitchFlag)
            Sound.gameMusic.pause();
        else
            sound.switchMusic(Sound.backgroundMusic, Sound.gameMusic);
        Sound.activitySwitchFlag = false;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Sound.activitySwitchFlag = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBase.setPrefRef("PRESNAME15", "PRESSCORE15");
        setContentView(R.layout.activity_game15);
        whatToShow = getIntent().getStringExtra("whatToShow");
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseFirestore.getInstance();
        isStarted = true;
        startTime = System.currentTimeMillis();


        button = new ImageButton[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                button[i][j] = (ImageButton) this.findViewById(BUTTON_ID[i][j]);
                button[i][j].setOnClickListener(onClickListener);
            }
//        Typeface digitalFont = Typeface.createFromAsset(this.getAssets(), "font.ttf");


        ImageButton newGameBtn = findViewById(R.id.bNewGame15);
        ImageButton backBtn = findViewById(R.id.bBackMenu15);
        soundBtn = findViewById(R.id.bSoundOffOn15);


        TextView titleTV = findViewById(R.id.gameTitle15);
        TextView textScoreTV = findViewById(R.id.tSScore15);
        scoreTV = findViewById(R.id.tScore15);
        TextView textRecordTV = findViewById(R.id.textBestScore15);
        recordTV = findViewById(R.id.tBestScore15);
//
//        titleTV.setTypeface(digitalFont);
//        textScoreTV.setTypeface(digitalFont);
//        scoreTV.setTypeface(digitalFont);
//        textRecordTV.setTypeface(digitalFont);
//        recordTV.setTypeface(digitalFont);
        Button hintBtn = findViewById(R.id.hint);

        AnimationDrawable animationDrawable = (AnimationDrawable) hintBtn.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        hintBtn.setOnClickListener(navigateBtnsClickListener);
        newGameBtn.setOnClickListener(navigateBtnsClickListener);
        backBtn.setOnClickListener(navigateBtnsClickListener);
        soundBtn.setOnClickListener(navigateBtnsClickListener);

        if (whatToShow.equals("Zoo"))
            hintBtn.setVisibility(View.VISIBLE);
        else
            hintBtn.setVisibility(View.INVISIBLE);

        if (Sound.check)
            soundBtn.setImageResource(R.drawable.soundon);


        cards = new Cards(N, N);
        newGame();
    }

    View.OnClickListener navigateBtnsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bNewGame15:
                    Sound.menuClickSound.start();
                    newGame();
                    break;
                case R.id.bBackMenu15:
                    Sound.menuClickSound.start();
                    Sound.activitySwitchFlag = true;
                    backMenu();
                    break;
                case R.id.bSoundOffOn15:
                    soundOffOn();
                    Sound.menuClickSound.start();
                    break;
                case R.id.hint:
                    openHintDialog();
                    break;
                default:
                    break;
            }
        }
    };
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!check) {
                for (int i = 0; i < N; i++)
                    for (int j = 0; j < N; j++)
                        if (v.getId() == BUTTON_ID[i][j])
                            buttonFunction(i, j);
            } else
                Toast.makeText(GameActivity15.this, R.string.you_won_toast, Toast.LENGTH_SHORT).show();


        }
    };

    public void buttonFunction(int row, int column) {
        cards.moveCards(row, column);
        if (cards.resultMove()) {
            Sound.buttonGameSound.start();
            numOfSteps++;
            showGame();
            checkFinish();
        }
    }

    public void newGame() {
        cards.getNewCards();
        numOfSteps = 0;
        recordSteps = dataBase.getMaxScore(2, "PRESSCORE15");
        recordTV.setText(Integer.toString(recordSteps));
        showGame();
        check = false;
    }

    public void backMenu() {
        finish();
    }

    public void showGame() {
        scoreTV.setText(Integer.toString(numOfSteps));

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (whatToShow.equals("Zoo")) {
                    if (cards.getValueBoard(i, j) != 0)
                        button[i][j].setImageBitmap(SlicingImage.imageChunksStorageList.get(cards.getValueBoard(i, j)));
                    else
                        button[i][j].setImageResource(CARDS_ID[cards.getValueBoard(i, j)]);
                } else
                    button[i][j].setImageResource(CARDS_ID[cards.getValueBoard(i, j)]);
    }

    public void checkFinish() {
        if (cards.finished(N, N)) {
            showGame();
            Sound.winningSound.start();
            openDialog();
            if ((numOfSteps < recordSteps) || (recordSteps == 0)) {
                recordTV.setText(Integer.toString(numOfSteps));
            }
            check = true;
        }
    }

    private void openDialog() {
        isStarted = false;
        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
        Log.d("MONTH", String.valueOf(now.get(Calendar.MONTH)));
        Log.d("YEAR", String.valueOf(now.get(Calendar.YEAR)));
        Log.d("DAY", String.valueOf(now.get(Calendar.DAY_OF_MONTH)));
        Log.d("Month", String.valueOf(now.get(Calendar.MONTH)));

        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH) + 1;
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(new Date());
        endTime = System.currentTimeMillis();
        Long seconds = (endTime - startTime) / 1000;
        Log.d("Seconds ", String.valueOf(seconds));
        SharedPreferences sh1 = getSharedPreferences("prefsTimeMemWH", MODE_APPEND);
        x = sh1.getInt("firstStartTimeMemWH", 0);
        Log.d("A Count", String.valueOf(x));

        int y = x + 1;

        SharedPreferences prefsCount = getSharedPreferences("prefsTimeMemWH", MODE_PRIVATE);
        SharedPreferences.Editor editor1 = prefsCount.edit();
        editor1.putInt("firstStartTimeMemWH", y);
        editor1.apply();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Memory Games").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(x));
        reference.setValue(seconds);
        SharedPreferences sh = getSharedPreferences("prefsCountPuz", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

        a = sh.getInt("firstStartCountPuz", 0);

// We can then use the data
        Log.d("Count 1", String.valueOf(a));

        int b = a + 1;
        Log.d("B Val", String.valueOf(b));

        SharedPreferences prefsCount1 = getSharedPreferences("prefsCountPuz", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefsCount1.edit();
        editor.putInt("firstStartCountPuz", b);
        editor.apply();
        SharedPreferences sha = getSharedPreferences("prefsCountPuz", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

        int a1 = sha.getInt("firstStartCountPuz", 0);

        Log.d("Count 2", String.valueOf(a1));

        final Dialog dialog = new Dialog(GameActivity15.this);
        dialog.setContentView(R.layout.game_intervention_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button ok;
        LineChart lineChart;
        ok = (Button) dialog.findViewById(R.id.ok);
        lineChart = (LineChart) dialog.findViewById(R.id.lineChartInterventionGame);
//        Button finishButton = dialog.findViewById(R.id.finishButton);
//        final EditText finishName = dialog.findViewById(R.id.finishName);
//        TextView finishSteps = dialog.findViewById(R.id.finishSteps);
//        finishSteps.setText(numOfSteps + " " + getString(R.string.finished_steps));

        if (numOfSteps <= 50) {
            points = 50;
        } else if (points <= 75) {
            points = 30;
        } else if (points <= 90) {
            points = 20;
        } else {
            points = 5;
        }
        getEntries();
        lineDataSet = new LineDataSet(lineEntries, "Puzzles Progress");
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
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });
        database.collection("users")
                .document(mUser.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
//                binding.currentCoins.setText(String.valueOf(user.getCoins()));
                Log.d("Current Coins", String.valueOf(user.getCoins()));
                Log.d("High Score Inside", String.valueOf(points));
                updatedCoins = (int) (user.getCoins() + points);
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


//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Puzzles").child(String.valueOf(a));
//        reference.setValue(points);
        dialog.show();
//        finishButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int numOfScores, checkPlace;
//                dataBase.setPrefRef("PRESNAME15", "PRESSCORE15");
//                numOfScores = dataBase.preferencesCounter.getInt("game15counter", 0);
//                if (numOfScores >= 10) {
//                    checkPlace = dataBase.checkIfScoreIsBest("PRESSCORE15", numOfSteps);
//                    if (checkPlace != (-1)) {
//                        dataBase.changeValues(finishName.getText().toString(), numOfSteps, checkPlace);
//                    }
//                } else
//                    dataBase.setValues(finishName.getText().toString(), numOfSteps, 2);
//
//                dialog.dismiss();
//
//            }
//        });
    }

    private void getEntries() {
        lineEntries = new ArrayList();
        lineEntries.add(new Entry(2f, 34f));
        lineEntries.add(new Entry(4f, 56f));
        lineEntries.add(new Entry(6f, 65));
        lineEntries.add(new Entry(8f, 23f));
    }

    private void openHintDialog() {
        final Dialog dialog = new Dialog(GameActivity15.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_hint);
        ImageButton imageButton = dialog.findViewById(R.id.hintImage);
        imageButton.setImageBitmap(SlicingImage.hint);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    public void soundOffOn() {
        if (Sound.check) {
            Sound.check = false;
            soundBtn.setImageResource(R.drawable.soundoff);
        } else {
            Sound.check = true;
            soundBtn.setImageResource(R.drawable.soundon);
        }
        sound.setSounds();
        sound.setMusic();
    }

}
