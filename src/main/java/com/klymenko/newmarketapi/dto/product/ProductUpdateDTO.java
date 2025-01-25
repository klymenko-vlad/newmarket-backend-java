package com.klymenko.newmarketapi.dto.product;

import com.klymenko.newmarketapi.enums.Categories;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductUpdateDTO {
    @Length(min = 2, max = 100, message = "Title length must be in between 2 and 100 characters")
    private String title;

    @Positive(message = "You can't have a negative or free price")
    private BigDecimal price;

    @Positive(message = "You can't have a negative price")
    private BigDecimal pastPrice;

    private String mainPictureUrl;

    @ElementCollection
    private List<String> picturesUrl;

    private String description;

    @Min(value = 0, message = "Quantity can't be below 0")
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private Categories category;

    @Min(value = 1, message = "Rating can't be below 1")
    @Max(value = 5, message = "Rating can't be above 5")
    private Integer rating;
}
