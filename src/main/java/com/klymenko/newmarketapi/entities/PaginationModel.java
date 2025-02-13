package com.klymenko.newmarketapi.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationModel {
    private Integer size;
    private Integer number;
    private Long totalElements;
    private Long totalPages;
}
