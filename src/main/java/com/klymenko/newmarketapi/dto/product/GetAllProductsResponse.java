package com.klymenko.newmarketapi.dto.product;

import com.klymenko.newmarketapi.entities.PaginationModel;
import com.klymenko.newmarketapi.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllProductsResponse {
    private List<Product> content;
    private PaginationModel page;
}