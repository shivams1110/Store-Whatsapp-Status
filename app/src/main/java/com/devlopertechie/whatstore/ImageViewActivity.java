package com.devlopertechie.whatstore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import static com.devlopertechie.whatstore.Constants.KEY_IMAGE_URL;

public class ImageViewActivity extends AppCompatActivity {

    private ImageView imgMain;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_image_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String imgUrl = this.getIntent().getStringExtra(KEY_IMAGE_URL);
        imgMain = findViewById(R.id.imageMain);
        imgBack = findViewById(R.id.img_back_image);

        Glide.with(this).load(imgUrl).into(imgMain);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}