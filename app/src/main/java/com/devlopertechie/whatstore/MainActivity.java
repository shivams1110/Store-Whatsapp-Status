package com.devlopertechie.whatstore;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.devlopertechie.whatstore.Constants.STATUS_FOLDER;
import static com.devlopertechie.whatstore.Utils.hasPermissions;
import static com.devlopertechie.whatstore.Utils.loadmp3;

public class MainActivity extends AppCompatActivity {

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAPTURE_AUDIO_OUTPUT, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_SMS};
    private List<FileDetail> fileList_ = new ArrayList<>();

    private MainAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        swipeRefreshLayout = findViewById(R.id.swipe);
        adapter = new MainAdapter(this, fileList_);
        recyclerView.setAdapter(adapter);

        getStatusResultFromSD();
        swipeRefreshListener();

    }

    private void swipeRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFileFromWhat();
                Toast.makeText(MainActivity.this, "Refresh Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getStatusResultFromSD() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // only for gingerbread and newer versions
            if (checkWriteExternalPermission()) {
                getFileFromWhat();
            }else {
                showAlertForPermision();
            }
        } else {
            getFileFromWhat();
        }
    }

    private void showAlertForPermision() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("WhatStore needs storage permission to work properly");
        dialog.setTitle("Allow Access");
        dialog.setPositiveButton("Allow", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!hasPermissions(MainActivity.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
                }
                dialog.cancel();
            }
        });

        AlertDialog dialog1 = dialog.create();
        dialog1.show();
    }

    private boolean checkWriteExternalPermission()
    {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(permissions.length == 0){
            return;
        }
        boolean allPermissionsGranted = true;
        if(grantResults.length>0){
            for(int grantResult: grantResults){
                if(grantResult != PackageManager.PERMISSION_GRANTED){
                    allPermissionsGranted = false;
                    break;
                }
            }
        }
        if(!allPermissionsGranted){
            for(String permission: permissions){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
                    //denied
                    Log.e("denied", permission);
                    Toast.makeText(this, "Permission denied to read External Storage", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    if(ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED){
                        //allowed
                        Log.e("allowed", permission);
                        getFileFromWhat();
                    } else{
                        //set to never ask again
                        Log.e("set to never ask again", permission);
                    }
                }
            }
        }
    }

    private void getFileFromWhat() {

        fileList_.clear();

        File rootPath = new File(Environment.getExternalStorageDirectory(), STATUS_FOLDER);

        fileList_.addAll(loadmp3(rootPath.getAbsolutePath()));

        swipeRefreshLayout.setRefreshing(false);
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            shareApp();
            return true;
        }

        if (id == R.id.action_direct_msg) {
            openDirectMsgActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openDirectMsgActivity() {
        Intent intent = new Intent(MainActivity.this, DirectMessageActvity.class);
        startActivity(intent);
    }

    private void shareApp() {
        final String appPackageName = getPackageName();
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");

        String link = "https://play.google.com/store/apps/details?id=" + appPackageName;
        i.putExtra(Intent.EXTRA_TEXT,   link);
        startActivity(Intent.createChooser(i, "Share link:"));

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
