package com.klymenko.newmarketapi.io;


import com.klymenko.newmarketapi.enums.Categories;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductResponse {
    private String id;
    private String title;
    private BigDecimal price;
    private BigDecimal pastPrice;
    private String mainPictureUrl;
    private List<String> picturesUrl;  // Include this field
    private String description;
    private Integer quantity;
    private Categories category;
    private Integer rating;
    // Exclude User and timestamps
}