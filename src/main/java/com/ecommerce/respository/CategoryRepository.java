package com.ecommerce.respository;

import com.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> getCategoryById(Integer id);

    boolean existsByName(String name);

    boolean existsById(Integer id);

    void deleteById(Integer id);
}
