package com.example.resmanback.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final String imageDir = "src/main/resources/uploads/images"; // Укажи путь к папке с изображениями

    // Эндпоинт для получения изображений
    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(imageDir).resolve(filename);
            File file = filePath.toFile();

            // Проверка на существование файла
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Если файл не найден
            }

            // Отдача изображения
            Resource resource = new UrlResource(file.toURI());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)  // Или другой тип, если нужно
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Ошибка при чтении файла
        }
    }
}
