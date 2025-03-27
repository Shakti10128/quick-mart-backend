package com.ecommerce.respository;

import com.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findById(int id);

    boolean existsById(int id);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    List<Product> findProductByCategoryId(@Param("categoryId") int categoryId);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchProductsByKeywords(@Param("keyword") String keyword);

    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findProductsByPriceRange(@Param("minPrice") Integer minPrice, @Param("maxPrice") Integer maxPrice);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId " +
            "AND p.id <> :productId " +
            "AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword1, '%')) " +
            "OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword2, '%')) " +
            "OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword3, '%'))) " +
            "ORDER BY p.name ASC")
    Page<Product> findSimilarProducts(@Param("categoryId") Integer categoryId,
                                      @Param("keyword1") String keyword1,
                                      @Param("keyword2") String keyword2,
                                      @Param("keyword3") String keyword3,
                                      @Param("productId") Integer productId,
                                      Pageable pageable);


}
