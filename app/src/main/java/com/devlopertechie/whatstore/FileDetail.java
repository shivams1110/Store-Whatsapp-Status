package com.devlopertechie.whatstore;

import java.io.File;

/**
 * Created by ssalphax on 3/25/2018.
 */

public class FileDetail {

    private String fileName;
    private String type;
    private File fileUri;

    public File getFileUri() {
        return fileUri;
    }

    public void setFileUri(File fileUri) {
        this.fileUri = fileUri;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
