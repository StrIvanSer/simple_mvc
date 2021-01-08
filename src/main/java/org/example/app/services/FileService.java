package org.example.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Service
public class FileService {

    private final List<File> fileList = new ArrayList<>();
    private final List<String> nameFiles = new ArrayList<>();

    @Autowired
    public FileService() {
        setFileList();
        setNameFiles();
    }

    public List<String> getNameFiles() {
        return nameFiles;
    }

    public void setNameFiles() {
        nameFiles.clear();
        for (File file : fileList) {
            nameFiles.add(file.getName());
        }
    }

    public void setFileList() {
        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "external_uploads");
        fileList.clear();

        for (File file : requireNonNull(dir.listFiles())) {
            if (file.isFile()) {
                fileList.add(file);
            }
        }
    }

}
