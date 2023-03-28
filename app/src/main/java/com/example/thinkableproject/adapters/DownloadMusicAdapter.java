package com.example.thinkableproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinkableproject.R;
import com.example.thinkableproject.sample.DownloadMusicModelClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DownloadMusicAdapter extends RecyclerView.Adapter<DownloadMusicAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DownloadMusicModelClass> downloadMusic;

    // Constructor
    public DownloadMusicAdapter(Context context, ArrayList<DownloadMusicModelClass> downloadMusic) {
        this.context = context;
        this.downloadMusic = downloadMusic;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_download_music, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Setting values to variables in recyclerview
        Picasso.get().load(downloadMusic.get(position).getItem_image()).into(holder.imageView);
        holder.songTitle.setText(downloadMusic.get(position).getItem_title());

    }

    @Override
    public int getItemCount() {
        //returning size of arraylist
        return downloadMusic.size();
    }

    //ViewHolder Class

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView songTitle;

        //Constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.downloadImage);
            songTitle = itemView.findViewById(R.id.downloadTitle);

        }
    }

}
