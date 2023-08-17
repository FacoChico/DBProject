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
    @PostMapping("/clientVisit")
    public ResponseEntity<?> clientVisit(@RequestParam("isVisited") boolean isVisited) {
        ClientController.isVisited = isVisited;
        System.out.println("Upload. Изменение состояния isVisited на " + isVisited);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/orderVisit")
    public ResponseEntity<?> orderVisit(@RequestParam("isVisited") boolean isVisited) {
        OrderController.isVisited = isVisited;
        System.out.println("Upload. Изменение состояния isVisited на " + isVisited);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/uploadClientPhoto")
    public ResponseEntity<?> handleClientPhotoUpload(@RequestParam("file") MultipartFile file) {
        String resourcesDir = Paths.get("target" + File.separator + "classes" + File.separator
                 + "static" + File.separator + "uploads").toAbsolutePath() + File.separator;
        try {
            file.transferTo( new File(resourcesDir + "newImage"));
            System.out.println("Client. Изменение состояния на TRUE в UploadController");
            ClientController.isReady = true;
        } catch (Exception e) {
            System.out.println("Client. Произошла ошибка при создании newImage!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok("File uploaded successfully.");
    }

    @PostMapping("/uploadOrderPhoto")
    public ResponseEntity<?> handleOrderPhotoUpload(@RequestParam("file") MultipartFile file) {
        String resourcesDir = Paths.get("target" + File.separator + "classes" + File.separator
                + "static" + File.separator + "uploads").toAbsolutePath() + File.separator;
        try {
            file.transferTo( new File(resourcesDir + "newImage"));
            System.out.println("Order. Изменение состояния на TRUE в UploadController");
            OrderController.isReady = true;
        } catch (Exception e) {
            System.out.println("Order. Произошла ошибка при создании newImage!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok("File uploaded successfully.");
    }
}