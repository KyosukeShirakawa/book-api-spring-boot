package com.book.api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String filePath = path + File.separator + filename;

        File f = new File(path);
        if(!f.exists()) {
            f.mkdir();
        }

        // copy file to path
        Files.copy(file.getInputStream(), Paths.get(filePath));

        return filename;
    }

    @Override
    public InputStream getResourceFile(String path, String fileName) throws FileNotFoundException {
        String fullPath = path + File.separator + fileName;
        return new FileInputStream(fullPath);
    }
}
