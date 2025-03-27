package com.ecommerce.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTOWIthSimilarProduct {
    private ProductDTO productDTO;
    private List<ProductDTO> similarProducts;
}