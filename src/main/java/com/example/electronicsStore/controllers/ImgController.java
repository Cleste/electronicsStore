package com.example.electronicsStore.controllers;

import com.example.electronicsStore.exceptions.InvalidFileException;
import com.example.electronicsStore.exceptions.ProductNotFoundException;
import com.example.electronicsStore.repositories.ProductRepository;
import com.example.electronicsStore.services.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/product")
public class ImgController {
    private ProductRepository productRepository;
    @Autowired
    private ImgService imgService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/img/{id}")
    public ResponseEntity<?> getImg(@PathVariable("id") String id) {
        return loadImg(id, uploadPath);
    }


    @PostMapping("/img/{id}")
    public ResponseEntity<?> addImg(@PathVariable("id") String id,
                                        @RequestParam("img") MultipartFile img
    ) {
        return saveImg(id, uploadPath, img);
    }


    private ResponseEntity<?> loadImg(String id, String path) {
        try {
            return imgService.loadImg(id, path);

        } catch (IOException e) {
            return new ResponseEntity<>("Error reading image!",
                    HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("invalid product id - " + id,
                    HttpStatus.BAD_REQUEST);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>("No book found for this ID - " + id,
                    HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<?> saveImg(String id, String path, MultipartFile file) {
        try {
            return imgService.saveImg(id, path, file);

        } catch (IOException e) {
            return new ResponseEntity<>("Error writing img - " + file.getOriginalFilename(),
                    HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("invalid product id - " + id,
                    HttpStatus.BAD_REQUEST);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>("No product found for this ID - " + id,
                    HttpStatus.BAD_REQUEST);
        } catch (InvalidFileException e) {
            return new ResponseEntity<>("File is not input",
                    HttpStatus.OK);
        }
    }
}
