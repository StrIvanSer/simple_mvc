package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.controllers.BookShelfController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Service
public class FileService {

    private final List<File> fileList = new ArrayList<>();
    private final List<String> nameFiles = new ArrayList<>();
    private final Logger logger = Logger.getLogger(FileService.class);

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

    public void uploadFile(MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();
        byte[] bytes = file.getBytes();

        //create dir
        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "external_uploads");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //create file
        File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
        stream.write(bytes);// записывает данные
        stream.close();// закрывает соединение

        logger.info("new file saved: " + serverFile.getAbsolutePath());
        setFileList();
        setNameFiles();
    }

    public void downloadFile(String fileName, HttpServletResponse response) {
        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "external_uploads");

        Path file = Paths.get(String.valueOf(dir), fileName);

        if (Files.exists(file)) {
            response.setHeader("Content-disposition", "attachment; filename = " + fileName);
            response.setContentType("application/octet-stream");
            try {
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
