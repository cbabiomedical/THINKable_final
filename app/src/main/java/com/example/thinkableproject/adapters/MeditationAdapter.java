package com.example.thinkableproject.adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinkableproject.R;
import com.example.thinkableproject.repositories.FavMeditationDB;
import com.example.thinkableproject.sample.DownloadMeditationClass;
import com.example.thinkableproject.sample.MeditationModelClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MeditationAdapter extends RecyclerView.Adapter<MeditationAdapter.ViewHolder> {
    private ArrayList<MeditationModelClass> downloadMeditation;
    private Context context;
    OnNoteListner onNoteListner;
    private FavMeditationDB favDB;
    FirebaseUser mUser;
    ArrayList<DownloadMeditationClass> downoadSong = new ArrayList<>();
    HashMap<String, Object> meditation = new HashMap<>();

    //Constructor
    public MeditationAdapter(ArrayList<MeditationModelClass> coffeeItems, Context context, OnNoteListner onNoteListner) {
        this.downloadMeditation = coffeeItems;
        this.context = context;
        this.onNoteListner = onNoteListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        favDB = new FavMeditationDB(context);
        //create table on first
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            createTableOnFirstStart();
        }
        //Setting View
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_meditation,
                parent, false);
        return new ViewHolder(view, onNoteListner);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //setting values into variables in recyclerview
        final MeditationModelClass coffeeItem = downloadMeditation.get(position);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        readCursorDataMed(coffeeItem, holder);
        Picasso.get().load(coffeeItem.getMeditateImage()).into(holder.imageView);
        holder.titleTextView.setText(coffeeItem.getMeditateName());
        //onClick listener for Downloads button in Meditation Exercise
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadMeditationClass musicModelClass = new DownloadMeditationClass(downloadMeditation.get(position).getMeditateName(), downloadMeditation.get(position).getMeditateImage());
                //Calling Download function method
                downloadFile(context, downloadMeditation.get(position).getMeditateName(), ".mp3", DIRECTORY_DOWNLOADS, downloadMeditation.get(position).getUrl());
                mUser = FirebaseAuth.getInstance().getCurrentUser();
                downoadSong.add(musicModelClass);
                meditation.put(downloadMeditation.get(position).getMeditateName(), musicModelClass);
                Log.d("Downloaded Music", String.valueOf(musicModelClass));
                Log.d("Download List", String.valueOf(meditation));


                holder.download.setEnabled(false);
                //Storing download music in firebase
                DatabaseReference database = FirebaseDatabase.getInstance().getReference("DownloadsMeditation").child(mUser.getUid());
                database.setValue(meditation);

            }
        });
    }


    @Override
    public int getItemCount() {
        //returning size of arraylist
        return downloadMeditation.size();
    }

    //ViewHolder Class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView titleTextView, likeCountTextView;
        Button favBtn;
        OnNoteListner onNoteListner;
        AppCompatButton download;

        public ViewHolder(@NonNull View itemView, OnNoteListner onNoteListner) {
            super(itemView);
            imageView = itemView.findViewById(R.id.gridImage);
            titleTextView = itemView.findViewById(R.id.item_name);
            favBtn = itemView.findViewById(R.id.favouritesIcon2);
            download = itemView.findViewById(R.id.download);
            this.onNoteListner = onNoteListner;
            itemView.setOnClickListener(this);

            //On Click Listener for Favourites
            //add to fav btn
            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    MeditationModelClass gameModelClass = downloadMeditation.get(position);
                    if (gameModelClass.getFav().equals("0")) {
                        gameModelClass.setFav("1");
                        //Inserting item into favourites database
                        favDB.insertIntoTheDatabaseMed(gameModelClass.getMeditateName(), gameModelClass.getMeditateImage(), gameModelClass.getMediateId(), gameModelClass.getFav());
                        favBtn.setBackgroundResource(R.drawable.ic_favorite_filled);
                    } else {
                        gameModelClass.setFav("0");
                        // removing items from favourites database
                        favDB.remove_fav_med(gameModelClass.getMediateId());
                        favBtn.setBackgroundResource(R.drawable.ic_favorite);
                    }
                }

            });
        }

        //OnClick Listener for items in arraylist
        @Override
        public void onClick(View v) {
            onNoteListner.onNoteClickMeditation(getAdapterPosition());
        }
    }

    private void createTableOnFirstStart() {
        favDB.insertEmptyMed();

        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    // Reading all data from database
    private void readCursorDataMed(MeditationModelClass coffeeItem, ViewHolder viewHolder) {
        Cursor cursor = favDB.read_all_data_med(coffeeItem.getMediateId());
        SQLiteDatabase db = favDB.getReadableDatabase();
        try {
            while (cursor.moveToNext()) {
                String item_fav_status = cursor.getString(cursor.getColumnIndex(FavMeditationDB.FAVORITE_STATUSMED));
                coffeeItem.setFav(item_fav_status);

                //check fav status
                if (item_fav_status != null && item_fav_status.equals("1")) {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.ic_favorite_filled);
                } else if (item_fav_status != null && item_fav_status.equals("0")) {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.ic_favorite);
                }
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }

    }

    //Download File method
    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {

        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);
        downloadmanager.enqueue(request);
    }

    //OnNoteListener Interface

    public interface OnNoteListner {
        void onNoteClickMeditation(int position);
    }

    // like click
}
