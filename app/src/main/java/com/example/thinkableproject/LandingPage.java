package com.example.thinkableproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LandingPage extends AppCompatActivity {
    private static final int PICK_FILE_REQUEST = 234;
    Button changepassword, logout, relaxationLanding, concentrationLanding, reportConDaily, reportRelDaily;
    Button chooseFile, uploadFile, edit, ble;
    private Uri filePath;
    private StorageReference storageReference;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        concentrationLanding = findViewById(R.id.concentrationDaily);
        chooseFile = findViewById(R.id.choose);
        ble = findViewById(R.id.ble);
        uploadFile = findViewById(R.id.upload);
        reportConDaily = findViewById(R.id.reportConDaily);
        edit = findViewById(R.id.editProfile);
        reportRelDaily = findViewById(R.id.reportLanding);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        ble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Connection.class));
            }
        });

        Log.d("User", mUser.getUid());
        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference().child(mUser.getUid());
        concentrationLanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Concentration_Daily.class);
                startActivity(intent);
            }
        });
        reportConDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConcentrationReportDaily.class);
                startActivity(intent);
            }
        });
        reportRelDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RelaxationReportDaily.class);
                startActivity(intent);
            }
        });

//      /  BottomNavigationView navView = findViewById(R.id.nav_view);
        relaxationLanding = findViewById(R.id.relaxationLanding);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);

        changepassword = findViewById(R.id.changepassword);
        logout = findViewById(R.id.logout);
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
                startActivity(intent);
            }
        });
        relaxationLanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Relaxation_Daily.class);
                startActivity(intent);
            }
        });
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null) {
            Toast.makeText(this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(LandingPage.this, SignInActivity.class));
            }
        });
    }

    private void uploadFileD() {


        StorageReference riverRef = storageReference.child("daily.txt");
        Log.d("UserCheck", mUser.getUid());
        riverRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(LandingPage.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LandingPage.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("text/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select file"), PICK_FILE_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Log.d("path", String.valueOf(filePath));
        }
    }


    public void logoutfrmGgl(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intentSignotGgl = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intentSignotGgl);
    }

    public void cq(View view) {
        Intent intentcq = new Intent(getApplicationContext(), ConcentrationReportWhereamI.class);
        startActivity(intentcq);
    }

    public void notificationPage(View view) {
        Intent intentnp = new Intent(getApplicationContext(), Notification1.class);
        startActivity(intentnp);
    }
}