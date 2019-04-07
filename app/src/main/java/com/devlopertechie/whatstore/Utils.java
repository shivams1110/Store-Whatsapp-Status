package com.devlopertechie.whatstore;

import android.net.Uri;

import java.io.File;
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
}
