package com.ecommerce.service;

import com.ecommerce.Dto.ProductDTO;
import com.ecommerce.Dto.ProductDTOWIthSimilarProduct;
import com.ecommerce.entity.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    /**
     * Add a new product to the database.
     *
     * @param product The product entity to be added.
     * @return The saved product with an assigned ID.
     */
    ProductDTO addProduct(Product product);

    /**
     * Retrieve all products available in the database.
     *
     * @return A list of all products.
     */
    List<ProductDTO> getAllProducts();

    /**
     * Retrieve a single product by its ID.
     *
     * @param id The ID of the product.
     * @return The product if found, otherwise Optional.empty().
     */
    ProductDTOWIthSimilarProduct getProductById(Integer id);

    /**
     * Update product details.
     * @param updatedProduct The updated product details.
     * @return The updated product entity.
     */
    ProductDTO updateProduct(Product updatedProduct);

    /**
     * Delete a product by its ID.
     *
     * @param id The ID of the product to be deleted.
     */
    void deleteProduct(Integer id);

    /**
     * Retrieve all products belonging to a specific category.
     *
     * @param categoryId The ID of the category.
     * @return A list of products under the given category.
     */
    List<ProductDTO> getProductsByCategory(Integer categoryId);

    /**
     * Retrieve products that match a given search keyword (e.g., name or description).
     *
     * @param keyword The search keyword.
     * @return A list of products that match the keyword.
     */
    List<ProductDTO> searchProducts(String keyword);

    /**
     * Retrieve products within a specified price range.
     *
     * @param minPrice The minimum price.
     * @param maxPrice The maximum price.
     * @return A list of products within the price range.
     */
    List<ProductDTO> getProductsByPriceRange(Integer minPrice, Integer maxPrice);

    /**
     * Check if a product exists by its ID.
     *
     * @param id The ID of the product.
     * @return True if the product exists, otherwise false.
     */
    boolean existsById(Integer id);

    List<ProductDTO> filterProducts(Integer categoryId, Integer minPrice, Integer maxPrice, String keyword);

}

