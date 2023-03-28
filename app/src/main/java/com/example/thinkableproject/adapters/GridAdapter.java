package com.example.thinkableproject.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinkableproject.R;
import com.example.thinkableproject.repositories.FavDB;
import com.example.thinkableproject.sample.GameModelClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    private ArrayList<GameModelClass> coffeeItems;
    private Context context;
    private OnNoteListner onNoteListner;
    private FavDB favDB;

    //Constructor
    public GridAdapter(ArrayList<GameModelClass> coffeeItems, Context context, OnNoteListner onNoteListner) {
        this.coffeeItems = coffeeItems;
        this.context = context;
        this.onNoteListner = onNoteListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        favDB = new FavDB(context);
        //create table on first
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            createTableOnFirstStart();
        }
        // Setting View
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,
                parent, false);
        return new ViewHolder(view, onNoteListner);
    }

    @Override
    public void onBindViewHolder(@NonNull GridAdapter.ViewHolder holder, int position) {
        //Setting values to variables in recyclerview
        final GameModelClass coffeeItem = coffeeItems.get(position);

        readCursorData(coffeeItem, holder);
        Picasso.get().load(coffeeItems.get(position).getGameImage()).into(holder.imageView);
        holder.titleTextView.setText(coffeeItem.getGameName());
    }


    @Override
    public int getItemCount() {
        return coffeeItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView titleTextView, likeCountTextView;
        Button favBtn;
        OnNoteListner onNoteListner;

        public ViewHolder(@NonNull View itemView, OnNoteListner onNoteListner) {
            super(itemView);

            imageView = itemView.findViewById(R.id.gridImage);
            titleTextView = itemView.findViewById(R.id.item_name);
            favBtn = itemView.findViewById(R.id.favIcon);
            this.onNoteListner = onNoteListner;
            itemView.setOnClickListener(this);


            //add to fav btn
            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    GameModelClass gameModelClass = coffeeItems.get(position);
                    if (gameModelClass.getFav().equals("0")) {
                        gameModelClass.setFav("1");
                        //inserting item into favourites database
                        favDB.insertIntoTheDatabase(gameModelClass.getGameName(), gameModelClass.getGameImage(), gameModelClass.getGameId(), gameModelClass.getFav());
                        favBtn.setBackgroundResource(R.drawable.ic_favorite_filled);
                    } else {
                        gameModelClass.setFav("0");
                        //removing items from favourites database
                        favDB.remove_fav(gameModelClass.getGameId());
                        favBtn.setBackgroundResource(R.drawable.ic_favorite);
                    }
                }

            });
        }

        //OnClick for Items in RecyclerView
        @Override
        public void onClick(View v) {
            onNoteListner.onNoteClickGame(getAdapterPosition());
        }
    }

    private void createTableOnFirstStart() {
        favDB.insertEmpty();

        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    //Reading all data from db
    private void readCursorData(GameModelClass coffeeItem, ViewHolder viewHolder) {
        Cursor cursor = favDB.read_all_data(coffeeItem.getGameId());
        SQLiteDatabase db = favDB.getReadableDatabase();
        try {
            while (cursor.moveToNext()) {
                String item_fav_status = cursor.getString(cursor.getColumnIndex(FavDB.FAVORITE_STATUS));
                coffeeItem.setFav(item_fav_status);

                //check fav status
                //if fav status=1 added to favourites
                if (item_fav_status != null && item_fav_status.equals("1")) {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.ic_favorite_filled);
                    //if fav status=0 removed from fav
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

    // like click
    private void likeClick(GameModelClass coffeeItem, Button favBtn, final TextView textLike) {
        DatabaseReference refLike = FirebaseDatabase.getInstance().getReference().child("likes");
        final DatabaseReference upvotesRefLike = refLike.child(coffeeItem.getGameId());

        if (coffeeItem.getFav().equals("0")) {

            coffeeItem.setFav("1");
            favDB.insertIntoTheDatabase(coffeeItem.getGameName(), coffeeItem.getGameImage(),
                    coffeeItem.getGameId(), coffeeItem.getGameName());
            favBtn.setBackgroundResource(R.drawable.ic_favorite_filled);
            favBtn.setSelected(true);

            upvotesRefLike.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull final MutableData mutableData) {
                    try {
                        Integer currentValue = mutableData.getValue(Integer.class);
                        if (currentValue == null) {
                            mutableData.setValue(1);
                        } else {
                            mutableData.setValue(currentValue + 1);
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    textLike.setText(mutableData.getValue().toString());
                                }
                            });
                        }
                    } catch (Exception e) {
                        throw e;
                    }
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                    System.out.println("Transaction completed");
                }
            });


        } else if (coffeeItem.getFav().equals("1")) {
            coffeeItem.setFav("0");
            favDB.remove_fav(coffeeItem.getGameId());
            favBtn.setBackgroundResource(R.drawable.ic_favorite_filled);
            favBtn.setSelected(false);

            upvotesRefLike.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull final MutableData mutableData) {
                    try {
                        Integer currentValue = mutableData.getValue(Integer.class);
                        if (currentValue == null) {
                            mutableData.setValue(1);
                        } else {
                            mutableData.setValue(currentValue - 1);
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    textLike.setText(mutableData.getValue().toString());
                                }
                            });
                        }
                    } catch (Exception e) {
                        throw e;
                    }
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                    System.out.println("Transaction completed");
                }
            });
        }


    }

    public interface OnNoteListner {
        void onNoteClickGame(int position);
    }
}
