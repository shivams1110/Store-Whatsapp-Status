package com.devlopertechie.whatstore;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import static com.devlopertechie.whatstore.Constants.KEY_VIDEO_URL;

public class VideoViewActivty extends AppCompatActivity {

    private VideoView videoView;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_video_view_activty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String videoUrl = this.getIntent().getStringExtra(KEY_VIDEO_URL);

        imgBack = (ImageView) findViewById(R.id.img_back);
        videoView = (VideoView) findViewById(R.id.videoView);
        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(this);
            mediacontroller.setAnchorView(videoView);
        // Get the URL from String VideoURL
            Uri video = Uri.parse(videoUrl);
            videoView.setMediaController(mediacontroller);
            videoView.setVideoPath(videoUrl);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                videoView.start();

            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

}
