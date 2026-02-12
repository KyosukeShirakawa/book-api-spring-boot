package com.book.api.controller;

import com.book.api.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/file")
public class FileController {
    private final FileService fileService;

    @Value("${project.images}")
    private String path;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestPart MultipartFile file) throws IOException {
        String  uploadedFileName =fileService.uploadFile(path, file);
        return ResponseEntity.ok("File uploaded is: " + uploadedFileName);
    }

}
