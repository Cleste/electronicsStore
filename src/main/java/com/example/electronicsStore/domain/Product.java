package com.example.electronicsStore.domain;


import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(message = "Product must have name!")
    @Length(max = 50, message = "Name too long")
    private String name;

    @NotBlank(message = "Product must have description!")
    private String description;

    @Range(min = 0, message = "Product must have positive price!")
    private Float price;


    private Integer quantity;

    @Column(name = "img_path")
    private String imgPath;
}
