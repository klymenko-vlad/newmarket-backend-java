package com.klymenko.newmarketapi.io;

import com.klymenko.newmarketapi.enums.Categories;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Data
public class ProductResponse implements Serializable {
    private String id;
    private String title;
    private BigDecimal price;
    private BigDecimal pastPrice;
    private String mainPictureUrl;
    private List<String> picturesUrl;
    private String description;
    private Integer quantity;
    private Categories category;
    private Integer rating;
    private Date updatedAt;
    private Date createdAt;
}