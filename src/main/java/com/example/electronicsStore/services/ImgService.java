package com.example.electronicsStore.services;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface ImgService {
    ResponseEntity<?> saveImg(String id, String uploadPath, MultipartFile productImg) throws IOException;
    ResponseEntity<InputStreamResource> loadImg(String id, String uploadPath) throws IOException;
}
