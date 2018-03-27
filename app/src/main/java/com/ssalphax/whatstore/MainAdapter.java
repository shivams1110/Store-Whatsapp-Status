package com.ssalphax.whatstore;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by ssalphax on 3/25/2018.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context context;
    private ArrayList<FileDetail> fileList;

    public MainAdapter(Context context, ArrayList<FileDetail> fileList) {
        this.context= context;
        this.fileList = fileList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final FileDetail f = fileList.get(position);

        if (!f.getType().equals("mp4")){
            holder.imgPlay.setVisibility(View.GONE);
        }else {
            holder.imgPlay.setVisibility(View.VISIBLE);
        }

        holder.imgMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (f.getType().equals("mp4")){
                    Intent intent = new Intent(context, VideoViewActivty.class);
                    intent.putExtra("videoUrl", f.getFileUri().getAbsolutePath());
                    context.startActivity(intent);
                }else {
                    Intent intent = new Intent(context, ImageViewActivity.class);
                    intent.putExtra("imageUrl", f.getFileUri().getAbsolutePath());
                    context.startActivity(intent);
                }
            }
        });

        Glide.with(context).load(f.getFileUri()).into(holder.imgMain);

        holder.linearShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (f.getType().equals("mp4")){
                    shareVideo(f.getFileUri());
                }else {
                    shareImage(f.getFileUri());
                }

            }
        });


        holder.linearSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveFile(f.getFileUri());

            }
        });

    }

    private void saveFile(File fileUri) {

        File file = new File(Environment.getExternalStorageDirectory(),"WhatStore");

        if (!file.exists()){
            file.mkdirs();
        }

        file = new File(file +"/"+fileUri.getName());


        try {
            copy(fileUri, file);
            Toast.makeText(context, "Saved to WhatStore", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    private void shareVideo(File fileUri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("video/mp4");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(fileUri.getAbsolutePath()));
        context.startActivity(Intent.createChooser(intent, "Share Video"));
    }

    private void shareImage(File fileUri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(fileUri.getAbsolutePath()));
        context.startActivity(Intent.createChooser(intent, "Share Image"));
//        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgMain;
        public ImageView imgPlay;
        public LinearLayout linearSave;
        public LinearLayout linearShare;

        public ViewHolder(View itemView) {
            super(itemView);

            imgMain = (ImageView) itemView.findViewById(R.id.imgMain);
            imgPlay = (ImageView) itemView.findViewById(R.id.img_play);
            linearSave = (LinearLayout) itemView.findViewById(R.id.linearSave);
            linearShare = (LinearLayout) itemView.findViewById(R.id.linearShare);

        }
    }
}
