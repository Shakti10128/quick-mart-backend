package com.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Description can't contain only whitespace")
    @NotEmpty(message = "Description must not be empty or null")
    private String description;

    @Min(value = 1, message = "Price must be greater than 1")
    private Integer price;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotBlank(message = "URL can't contain only whitespace")
    @NotEmpty(message = "URL must not be empty or null")
    private String productUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore // Prevents serialization of the orders list in JSON response
    private List<Orders> orders;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference // Prevents infinite recursion
    private Category category;

    public Product(){
        this.orders = new ArrayList<>();
    }
}
