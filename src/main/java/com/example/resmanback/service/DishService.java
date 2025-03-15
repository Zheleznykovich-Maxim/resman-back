package com.example.resmanback.service;

import com.example.resmanback.model.Dish;
import com.example.resmanback.repository.DishRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class DishService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public Dish getDishById(Long id) {
        return dishRepository.findById(id).orElseThrow(() -> new RuntimeException("Dish not found"));
    }

    public Dish createDish(Dish dish, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            String imageFileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path imagePath = Path.of(uploadDir).resolve(imageFileName);
            Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            dish.setImageUrl("/uploads/images/" + imageFileName);
        }
        return dishRepository.save(dish);
    }

    public String generateImage(String dishName) {
        // TODO: Add logic for AI image generation (placeholder)
        return "/uploads/images/generated_" + dishName + ".png";
    }

    public Dish updateDish(Long id, Dish updatedDish) {
        Dish dish = getDishById(id);
        dish.setName(updatedDish.getName());
        dish.setPrice(updatedDish.getPrice());
        dish.setCategory(updatedDish.getCategory());
        return dishRepository.save(dish);
    }

    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }
}
