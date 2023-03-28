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

import com.example.thinkableproject.Music;
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

public class GameActivity24 extends AppCompatActivity {


    private final int N = 5;
    Cards cards;
    int a, points;
    FirebaseUser mUser;
    int updatedCoins;
    User user;
    int x;
    LineData lineData;
    LineDataSet lineDataSet;
    Long startTime, endTime;
    ArrayList lineEntries;
    FirebaseFirestore database;
    private ImageButton[][] button;
    private final int[][] BUT_ID = {{R.id.b2400, R.id.b2401, R.id.b2402, R.id.b2403, R.id.b2404},
            {R.id.b2410, R.id.b2411, R.id.b2412, R.id.b2413, R.id.b2414},
            {R.id.b2420, R.id.b2421, R.id.b2422, R.id.b2423, R.id.b2424},
            {R.id.b2430, R.id.b2431, R.id.b2432, R.id.b2433, R.id.b2434},
            {R.id.b2440, R.id.b2441, R.id.b2442, R.id.b2443, R.id.b2444}};
    private final int[] CARDS_ID = {R.drawable.card2400, R.drawable.card2401, R.drawable.card2402, R.drawable.card2403, R.drawable.card2404,
            R.drawable.card2405, R.drawable.card2406, R.drawable.card2407, R.drawable.card2408, R.drawable.card2409,
            R.drawable.card2410, R.drawable.card2411, R.drawable.card2412, R.drawable.card2413, R.drawable.card2414,
            R.drawable.card2415, R.drawable.card2416, R.drawable.card2417, R.drawable.card2418, R.drawable.card2419,
            R.drawable.card2420, R.drawable.card2421, R.drawable.card2422, R.drawable.card2423, R.drawable.card2424};

    private TextView scoreTV;
    private int numOfSteps;
    private TextView recordTV;
    private int recordSteps;
    private ImageButton soundBtn;
    private boolean check;
    private String whatToShow;
    public static boolean isStarted = false;

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
        dataBase.setPrefRef("PRESNAME24", "PRESSCORE24");
        setContentView(R.layout.activity_game24);
        whatToShow = getIntent().getStringExtra("whatToShow");
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseFirestore.getInstance();
        isStarted = true;
        startTime = System.currentTimeMillis();


        button = new ImageButton[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                button[i][j] = (ImageButton) this.findViewById(BUT_ID[i][j]);
                button[i][j].setOnClickListener(onClickListener);
            }
//        Typeface digitalFont = Typeface.createFromAsset(this.getAssets(), "font.ttf");

        ImageButton newGameBtn = findViewById(R.id.bNewGame24);
        ImageButton backBtn = findViewById(R.id.bBackMenu24);
        soundBtn = findViewById(R.id.bSoundOffOn24);


        TextView titleTV = findViewById(R.id.gameTitle24);
        TextView textScoreTV = findViewById(R.id.tSScore24);
        scoreTV = findViewById(R.id.tScore24);
        TextView textRecordTV = findViewById(R.id.tBestSScore24);
        recordTV = findViewById(R.id.tBestScore24);

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
        backBtn.setOnClickListener(navigateBtnsClickListener);
        newGameBtn.setOnClickListener(navigateBtnsClickListener);
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
                case R.id.bNewGame24:
                    Sound.menuClickSound.start();
                    newGame();
                    break;
                case R.id.bBackMenu24:
                    Sound.activitySwitchFlag = true;
                    Sound.menuClickSound.start();
                    backMenu();
                    break;
                case R.id.bSoundOffOn24:
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
                        if (v.getId() == BUT_ID[i][j])
                            butFunc(i, j);
            } else
                Toast.makeText(GameActivity24.this, R.string.you_won_24, Toast.LENGTH_SHORT).show();

        }
    };

    public void butFunc(int row, int columb) {
        cards.moveCards(row, columb);
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
        recordSteps = dataBase.getMaxScore(3, "PRESSCORE24");
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
        Long second = (endTime - startTime) / 1000;
        Log.d("Seconds", String.valueOf(second));
        isStarted = false;
        SharedPreferences sh1 = getSharedPreferences("prefsTimeMemWH", MODE_APPEND);
        x = sh1.getInt("firstStartTimeMemWH", 0);
        Log.d("A Count", String.valueOf(x));

        int y = x + 1;

        SharedPreferences prefsCount = getSharedPreferences("prefsTimeMemWH", MODE_PRIVATE);
        SharedPreferences.Editor editor1 = prefsCount.edit();
        editor1.putInt("firstStartTimeMemWH", y);
        editor1.apply();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Memory Games").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(x));
        reference.setValue(second);
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

        final Dialog dialog = new Dialog(GameActivity24.this);
        dialog.setContentView(R.layout.game_intervention_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button ok;
        LineChart lineChart;
        ok = (Button) dialog.findViewById(R.id.ok);
        lineChart = (LineChart) dialog.findViewById(R.id.lineChartInterventionGame);
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


//        Button finishButton = dialog.findViewById(R.id.finishButton);
//        final EditText finishName = dialog.findViewById(R.id.finishName);
//        TextView finishSteps = dialog.findViewById(R.id.finishSteps);
//        finishSteps.setText(numOfSteps + " " + getString(R.string.finished_steps));
        if (numOfSteps <= 75) {
            points = 50;
        } else if (numOfSteps <= 90) {
            points = 30;
        } else if (points <= 110) {
            points = 20;
        } else {
            points = 5;
        }
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
//                dataBase.setPrefRef("PRESNAME24", "PRESSCORE24");
//                numOfScores = dataBase.preferencesCounter.getInt("game24counter", 0);
//                if (numOfScores >= 10) {
//                    checkPlace = dataBase.checkIfScoreIsBest("PRESSCORE24", numOfSteps);
//                    if (checkPlace != (-1)) {
//                        dataBase.changeValues(finishName.getText().toString(), numOfSteps, checkPlace);
//                    }
//                } else
//                    dataBase.setValues(finishName.getText().toString(), numOfSteps, 3);
//
//                dialog.dismiss();
//
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
        final Dialog dialog = new Dialog(GameActivity24.this);
        dialog.setContentView(R.layout.dialog_hint);
        ImageButton hintImageButton = dialog.findViewById(R.id.hintImage);
        hintImageButton.setImageBitmap(SlicingImage.hint);
        hintImageButton.setOnClickListener(new View.OnClickListener() {
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
