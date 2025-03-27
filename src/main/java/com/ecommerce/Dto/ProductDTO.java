package com.ecommerce.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Integer id;
    private String name;
    private String description;
    private Integer price;
    private Integer quantity;
    private String productUrl;
    private CategoryDTO category; // Includes only category ID & name
}
