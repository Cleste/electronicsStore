package com.example.electronicsStore.services.implementations;

import com.example.electronicsStore.domain.Product;
import com.example.electronicsStore.exceptions.InvalidFileException;
import com.example.electronicsStore.exceptions.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.example.electronicsStore.repositories.ProductRepository;
import com.example.electronicsStore.services.ImgService;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImgServiceIml implements ImgService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ServletContext servletContext;

    @Override
    public ResponseEntity<?> saveImg(String id, String uploadPath, MultipartFile productImg)
            throws IOException, InvalidFileException, ProductNotFoundException {
        if (id != null && !id.isEmpty()) {
            Product product = productRepository.
                    findById(UUID.fromString(id))
                    .orElseThrow(ProductNotFoundException::new);

            if (productImg != null && !productImg.getOriginalFilename().isEmpty()) {

                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdir();
                String fileDir = uploadPath + "/"
                        + UUID.randomUUID().toString() + "."
                        + productImg.getOriginalFilename();
                productImg.transferTo(new File(fileDir));

                product.setImgPath(fileDir);
                productRepository.save(product);

                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("product/img" + product.getId())
                        .toUriString();
                return ResponseEntity.ok().body(fileDownloadUri);
            } else throw new InvalidFileException();
        } else throw new ProductNotFoundException();
    }

    @Override
    public ResponseEntity<InputStreamResource> loadImg(String id, String uploadPath) throws IOException, ProductNotFoundException {
        if (id != null && !id.isEmpty()) {
            Product product = productRepository
                    .findById(UUID.fromString(id))
                    .orElseThrow(ProductNotFoundException::new);

            if (product.getImgPath() != null && !product.getImgPath().isEmpty()) {
                File img = new File(product.getImgPath());
                InputStreamResource resource = new InputStreamResource(new FileInputStream(img));
                MediaType mediaType = MediaType.parseMediaType(servletContext.getMimeType(img.getName()));
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + img.getName())
                        .contentType(mediaType)
                        .contentLength(img.length())
                        .body(resource);
            } else throw new InvalidFileException();
        } else throw new ProductNotFoundException();
    }
}