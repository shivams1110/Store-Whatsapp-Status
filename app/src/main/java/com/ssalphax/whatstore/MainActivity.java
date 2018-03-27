package com.ssalphax.whatstore;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAPTURE_AUDIO_OUTPUT, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_SMS};
    private File rootPath;
    private ArrayList<FileDetail> fileList_ = new ArrayList<FileDetail>();

    private MainAdapter adapter;
    private RecyclerView recyclerView;
    private String[] extensions = {"png", "jpg", "mp4", "jpeg"};
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        if (!hasPermissions(this, PERMISSIONS)) {
//            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
//        }

        swipeRefreshLayout = findViewById(R.id.swipe);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_main);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new MainAdapter(this, fileList_);

        recyclerView.setAdapter(adapter);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // only for gingerbread and newer versions
            if (checkWriteExternalPermission()) {
                getFileFromWhat();
            }else {
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
        } else {
            getFileFromWhat();
        }



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFileFromWhat();
                Toast.makeText(MainActivity.this, "Refresh Success", Toast.LENGTH_SHORT).show();
            }
        });

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
            boolean somePermissionsForeverDenied = false;
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
                        somePermissionsForeverDenied = true;
                    }
                }
            }
//            if(somePermissionsForeverDenied){
//                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//                alertDialogBuilder.setTitle("Permissions Required")
//                        .setMessage("You have forcefully denied some of the required permissions " +
//                                "for this action. Please open settings, go to permissions and allow them.")
//                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                                        Uri.fromParts("package", getPackageName(), null));
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                            }
//                        })
//                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        })
//                        .setCancelable(false)
//                        .create()
//                        .show();
//            }
        } else {
            switch (requestCode) {
                //act according to the request code used while requesting the permission(s).
            }
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void getFileFromWhat() {

        fileList_.clear();

        rootPath = new File(Environment.getExternalStorageDirectory(), "WhatsApp/Media/.Statuses");

        fileList_ = loadmp3(rootPath.getAbsolutePath());

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

        return super.onOptionsItemSelected(item);
    }

    private void shareApp() {
        final String appPackageName = getPackageName();
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");

        String link = "https://play.google.com/store/apps/details?id=" + appPackageName;
        i.putExtra(Intent.EXTRA_TEXT,   link);
        startActivity(Intent.createChooser(i, "Share link:"));

    }

    private ArrayList<FileDetail> loadmp3(String YourFolderPath) {

        File file1 = new File(YourFolderPath);

        File[] filesList = file1.listFiles();

        if (filesList.length > 0) {

//            progressBar.setVisibility(View.GONE);

//            textView.setVisibility(View.GONE);

            //get file list last modified

            Arrays.sort(filesList, new Comparator() {
                public int compare(Object o1, Object o2) {

                    if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                        return -1;
                    } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                        return +1;
                    } else {
                        return 0;
                    }
                }

            });

            for (File f : filesList) {

                Date date = new Date();
                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_YEAR, -2);
                Date newDate = calendar.getTime();

                if (f.lastModified()> newDate.getTime()) {

                    for (int i = 0; i < extensions.length; i++) {

                        if (f.getAbsolutePath().endsWith(extensions[i])) {

                            FileDetail fDetail = new FileDetail();

                            String name = f.getName();

                            String extension = extensions[i];
                            Uri uri = Uri.parse(f.getAbsolutePath());


                            fDetail.setType(extension);
                            fDetail.setFileName(name);
                            fDetail.setFileUri(new File(String.valueOf(uri)));

                            fileList_.add(fDetail);


                        }

                    }
                }
            }


//            Arrays.sort(filesList, new Comparator<File>() {
//                @Override
//                public int compare(File object1, File object2) {
//
//
//                    Date date = null, date1 = null;
//                    String dateString = object1.getName();
//                    String dateString1 = object2.getName();
//
//                    dateString = dateString.replace("-", "/");
//
//                    dateString1 = dateString1.replace("-", "/");
//
//
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//                    try {
//                        date = sdf.parse(dateString);
//                        date1 = sdf.parse(dateString1);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//                    Long dat = date.getTime();
//                    Long dat2 = date1.getTime();
//
//
//                    if (dat > dat2) {
//                        return -1;
//                    } else if (dat < dat2) {
//                        return +1;
//                    } else {
//                        return 0;
//                    }
//
//                }
//            });

//            for (File file : filesList) {
//
//
//                if (file.isDirectory()) {
//                    File[] files = file.listFiles();
//
////            Arrays.sort(files, new Comparator<File>() {
////                public int compare(File f1, File f2) {
////                    return Long.compare(f1.lastModified(), f2.lastModified());
////                }
////            });
//
//
//                    // loadmp3(file.getAbsolutePath());
//
//
//                    Arrays.sort(files, new Comparator() {
//                        public int compare(Object o1, Object o2) {
//
//                            if (((File) o1).lastModified() > ((File) o2).lastModified()) {
//                                return -1;
//                            } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
//                                return +1;
//                            } else {
//                                return 0;
//                            }
//                        }
//
//                    });
//
//
//                    if (files != null && files.length > 0) {
//
//
////
//
//
//                        for (File f : files) {
//
//                            for (int i = 0; i < extensions.length; i++) {
//
//                                if (f.getAbsolutePath().endsWith(extensions[i])) {
//
//                                    String name = f.getName();
//
//                                    Uri uri = Uri.parse(f.getAbsolutePath());
//
//                                    fileList_.add(new File(String.valueOf(uri)));
//
////                                dataAddress.add(f.getAbsolutePath());
////                                dataList.add(userName); //array list of store data
//
//
//                                }
//
//                            }
//                        }
//
//
////                        }
//
//                    }
//
////                        progressBar.setVisibility(View.GONE);
////                        adapter.notifyDataSetChanged();
//
//
//                }
//
////
//            }


//            progressBar.setVisibility(View.GONE);


        } else {
//            progressBar.setVisibility(View.GONE);

        }

        return fileList_;
    }

    @Override
    protected void onResume() {
        super.onResume();

//        getFileFromWhat();
    }



}
