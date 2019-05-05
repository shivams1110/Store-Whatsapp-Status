package com.devlopertechie.whatstore;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by ShivamSharma on 4/5/19.
 */
public class Utils {

    private static String[] extensions = {"png", "jpg", "mp4", "jpeg"};

    public static List<FileDetail> loadmp3(String YourFolderPath) {

        List<FileDetail> fileList_ = new ArrayList<>();

        File file1 = new File(YourFolderPath);

        File[] filesList = file1.listFiles();

        if (filesList.length > 0) {

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
        }
        return fileList_;
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

    public static String readJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
