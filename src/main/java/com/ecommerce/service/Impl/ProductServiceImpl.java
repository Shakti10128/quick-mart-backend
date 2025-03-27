package com.ecommerce.service.Impl;

import com.ecommerce.Dto.CategoryDTO;
import com.ecommerce.Dto.ProductDTO;
import com.ecommerce.Dto.ProductDTOWIthSimilarProduct;
import com.ecommerce.entity.Category;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.category.CategoryNotExistException;
import com.ecommerce.exception.product.ProductNotFoundException;
import com.ecommerce.respository.CategoryRepository;
import com.ecommerce.respository.ProductCriteria;
import com.ecommerce.respository.ProductRepository;
import com.ecommerce.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductCriteria productCriteria;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;


    @Override
    public ProductDTO addProduct(Product product) {
        Category category = categoryRepository.getCategoryById(product.getCategory().getId())
                .orElseThrow(()-> new CategoryNotExistException("category not exist"));

        product.setName(product.getName().toLowerCase());
        category.setName(category.getName().toLowerCase());
        product.setCategory(category);
        productRepository.save(product);
        return convertToDTO(product);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products) {
            productDTOS.add(convertToDTO(product));
        }
        return productDTOS;
    }

    @Override
    public ProductDTOWIthSimilarProduct getProductById(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product not found with id: " + id)
        );

        List<ProductDTO> similarProducts = getSimilarProducts(product);
        ProductDTO productDTO = convertToDTO(product);

        return new ProductDTOWIthSimilarProduct(
                productDTO, similarProducts
        );
    }

    @Override
    public ProductDTO updateProduct(Product updatedProduct) {
        boolean isProductExist = productRepository.existsById(updatedProduct.getId());
        if(!isProductExist) {
            throw new ProductNotFoundException("Product not found with id: " + updatedProduct.getId());
        }
        updatedProduct.setName(updatedProduct.getName().toLowerCase());
        Product product = productRepository.save(updatedProduct);
        return convertToDTO(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        if(productRepository.existsById(id)) {
            productRepository.deleteById(id);
        }
        else {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
    }

    @Override
    public List<ProductDTO> getProductsByCategory(Integer categoryId) {
        return productRepository.findProductByCategoryId(categoryId)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> searchProducts(String keyword) {
        return productRepository.searchProductsByKeywords(keyword)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProductsByPriceRange(Integer minPrice, Integer maxPrice) {
        return productRepository.findProductsByPriceRange(minPrice,maxPrice)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Integer id) {
        return false;
    }

    @Override
    public List<ProductDTO> filterProducts(Integer categoryId, Integer minPrice, Integer maxPrice, String keyword) {
        return productCriteria.filterProducts(categoryId, minPrice, maxPrice, keyword)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    private List<ProductDTO> getSimilarProducts(Product product) {
        // Ensure the product name is trimmed and split properly
        String[] words = product.getName().trim().split("\\s+");

        // Extract up to three words safely
        List<String> keywords = Arrays.asList(words).subList(0, Math.min(words.length, 3));

        // Assign keywords dynamically
        String keyword1 = !keywords.isEmpty() ? keywords.get(0) : "";
        String keyword2 = keywords.size() > 1 ? keywords.get(1) : "";
        String keyword3 = keywords.size() > 2 ? keywords.get(2) : "";

        System.out.println("Keywords: " + keyword1 + ", " + keyword2 + ", " + keyword3);

        // Define a Pageable object to limit results to 10
        Pageable pageable = PageRequest.of(0, 10, Sort.by("price").descending());

        Page<Product> similarProductsPage = productRepository.findSimilarProducts(
                product.getCategory().getId(),
                keyword1,
                keyword2,
                keyword3,
                product.getId(),
                pageable
        );

        return similarProductsPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    private ProductDTO convertToDTO(Product product) {
        CategoryDTO categoryDTO = new CategoryDTO(product.getCategory().getId(), product.getCategory().getName());
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getProductUrl(),
                categoryDTO
        );
    }
}
