package com.klymenko.newmarketapi.mappers;

import com.klymenko.newmarketapi.dto.ProductDTO;
import com.klymenko.newmarketapi.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product mapToProductEntity(ProductDTO productDTO);
}
