package com.ecommerce.controller;


import com.ecommerce.entity.Category;
import com.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create-category")
    public ResponseEntity<Object> createCategory(@Valid @RequestBody Category category) {
        Category savedCategory = categoryService.createCategory(category);
        Map<String, Object> apiResponse = Map.of(
                "success",true,
                "message","category created successfully",
                "data",savedCategory
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update-category")
    public ResponseEntity<Object> updateCategory(@Valid @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(category);
        Map<String, Object> apiResponse = Map.of(
                "success",true,
                "message","category updated successfully",
                "data",updatedCategory
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/get-all-categories")
    public ResponseEntity<Object> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        Map<String, Object> apiResponse = Map.of(
                "success",true,
                "message",categories.isEmpty() ? "no category found" : "category fetched successfully",
                "data",categories
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{categoryid}")
    public ResponseEntity<Object> deleteCategoryHandler(@PathVariable("categoryid") Integer id) {
        categoryService.deleteCategory(id);
        Map<String, Object> apiResponse = Map.of(
                "success", true,
                "message", "Category deleted successfully"
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{categoryid}")
    public ResponseEntity<Object> getCategoryHandler(@PathVariable("categoryid") Integer id) {
        Category category = categoryService.getCategoryById(id);
        Map<String, Object> apiResponse = Map.of(
                "success",true,
                "message","category fetched successfully",
                "data",category
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
