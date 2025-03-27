package com.ecommerce.service.Impl;

import com.ecommerce.entity.Category;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.category.CategoryAlreadyExistException;
import com.ecommerce.exception.category.CategoryNotExistException;
import com.ecommerce.respository.CategoryRepository;
import com.ecommerce.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(Category category) {
        boolean isCategoryExist = categoryRepository.existsByName(category.getName().toLowerCase());
        if (isCategoryExist) {
            throw new CategoryAlreadyExistException("Category already exist");
        }
        category.setName(category.getName().toLowerCase());
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category) {
        boolean isCategoryExist = categoryRepository.existsById(category.getId());
        if (!isCategoryExist) {
            throw new CategoryNotExistException("Category does not exist");
        }
        category.setName(category.getName().toLowerCase());
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteCategory(Integer categoryId) {
        boolean isCategoryExist = categoryRepository.existsById(categoryId);
        if (!isCategoryExist) {
            throw new CategoryNotExistException("Category does not exist");
        }
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public Category getCategoryById(Integer categoryId) {
        return categoryRepository.getCategoryById(categoryId)
                .orElseThrow(() -> new CategoryNotExistException("Category does not exist"));
    }

    @Override
    public Category addProductToCategory(Integer categoryId, Product product) {
        return null;
    }

    @Override
    public Category removeProductFromCategory(Integer categoryId, Integer productId) {
        return null;
    }
}
