package com.example.myaudiorecorder.model;

import android.os.Environment;

import com.example.myaudiorecorder.MainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileRepository {
    private List<String> listOfFiles;
    private final String mName = Environment.getExternalStorageDirectory().toString() + "/Android/data/" + MainActivity.FOLDER_NAME;

    public FileRepository() {
        listOfFiles = new ArrayList<>();

    }

    public List<String> getListOfFiles() {
        return listOfFiles;
    }

    public void updateListOfFiles() {
        doReading(mName);
    }


    private void doReading(final String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileRepository.this.readFiles(name);
            }
        }).start();
//        readFiles(name);
    }

    private void readFiles(String name) {
        File externalAppDir = new File(name);
        if (!externalAppDir.exists()) {
            externalAppDir.mkdir();
        }
        File[] fileList = externalAppDir.listFiles();
        if (fileList != null) {
            for (File file : fileList)
                if (file.isFile())
                    listOfFiles.add(file.getName());
                else if (file.isDirectory())
                    readFiles(file.getAbsolutePath());
        } else
            listOfFiles.add("no files");
    }
}

