package com.ecommerce.service;

import com.ecommerce.entity.Category;
import com.ecommerce.entity.Product;

import java.util.List;

public interface CategoryService {

    /**
     * Creates a new category.
     * Only admins are allowed to perform this action.
     *
     * @param category The category object to be created.
     * @return The created category with an auto-generated ID.
     */
    Category createCategory(Category category);

    /**
     * Updates an existing category.
     * Only admins can update category details like name or associated products.
     *
     * @param category The updated category object.
     * @return The updated category.
     */
    Category updateCategory(Category category);

    /**
     * Retrieves a list of all categories.
     * Accessible to admins and users.
     *
     * @return A list of all available categories.
     */
    List<Category> getAllCategories();

    /**
     * Deletes a category by its ID.
     * Only admins can delete a category.
     *
     * @param categoryId The ID of the category to be deleted.
     */
    void deleteCategory(Integer categoryId);

    /**
     * Retrieves a category by its ID.
     * Useful for admin dashboards or detailed category views.
     *
     * @param categoryId The ID of the category.
     * @return The category object if found.
     */
    Category getCategoryById(Integer categoryId);

    /**
     * Adds a product to an existing category.
     * Only admins can perform this operation.
     *
     * @param categoryId The ID of the category where the product should be added.
     * @param product The product to be added.
     * @return The updated category object with the newly added product.
     */
    Category addProductToCategory(Integer categoryId, Product product);

    /**
     * Removes a product from an existing category.
     * Only admins can remove a product from a category.
     *
     * @param categoryId The ID of the category.
     * @param productId The ID of the product to be removed.
     * @return The updated category object after the product is removed.
     */
    Category removeProductFromCategory(Integer categoryId, Integer productId);
}

