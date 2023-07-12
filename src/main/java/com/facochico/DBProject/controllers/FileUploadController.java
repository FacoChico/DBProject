package com.facochico.DBProject.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;



@Controller
public class FileUploadController {
    @PostMapping("/uploadClientPhoto")
    public ResponseEntity<?> handleClientPhotoUpload(@RequestParam("file") MultipartFile file ) {
        String resourcesDir = Paths.get("target" + File.separator + "classes" + File.separator
                 + "static" + File.separator + "uploads").toAbsolutePath() + File.separator;
        try {
            file.transferTo( new File(resourcesDir + "newImage"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok("File uploaded successfully.");
    }

    @PostMapping("/uploadOrderPhoto")
    public ResponseEntity<?> handleOrderPhotoUpload(@RequestParam("file") MultipartFile file ) {
        OrderController.isReady = false;
        String resourcesDir = Paths.get("target" + File.separator + "classes" + File.separator
                + "static" + File.separator + "uploads").toAbsolutePath() + File.separator;
        try {
            file.transferTo( new File(resourcesDir + "newImage"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        OrderController.isReady = true;

        return ResponseEntity.ok("File uploaded successfully.");
    }
}