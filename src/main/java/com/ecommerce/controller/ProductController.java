package com.ecommerce.controller;

import com.ecommerce.Dto.ProductDTO;
import com.ecommerce.Dto.ProductDTOWIthSimilarProduct;
import com.ecommerce.entity.Product;
import com.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create-product")
    public ResponseEntity<Object> createProduct(@RequestBody Product product) {
        ProductDTO productDTO = productService.addProduct(product);
        Map<String,Object> apiResponse = Map.of(
                "success",true,
                "message","Product created successfully",
                "data",productDTO
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/allproducts")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> productDTOList = productService.getAllProducts();
        Map<String,Object> apiResponse = Map.of(
                "success",true,
                "message",productDTOList.isEmpty()?"No products found": "Product fetched successfully",
                "data",productDTOList
        );
        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }

    @GetMapping("/{productid}")
    public ResponseEntity<Object> getProduct(@PathVariable("productid") Integer id) {
        ProductDTOWIthSimilarProduct ProductDTOWIthSimilarProduct = productService.getProductById(id);
        Map<String,Object> apiResponse = Map.of(
                "success",true,
                "message","product fetched successfully",
                "data",ProductDTOWIthSimilarProduct
        );
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update-product")
    public ResponseEntity<Object> updateProduct(@RequestBody Product product) {
        ProductDTO productDTO = productService.updateProduct(product);
        Map<String,Object> apiResponse = Map.of(
                "success",true,
                "message","product updated successfully",
                "data",productDTO
        );
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{productid}")
    public ResponseEntity<Object> deleteProduct(@PathVariable("productid") Integer id) {
        productService.deleteProduct(id);
        Map<String,Object> apiResponse = Map.of(
                "success",true,
                "message","product deleted successfully"
        );
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/category/{categoryid}")
    public ResponseEntity<Object> getProductsByCategory(@PathVariable("categoryid") Integer id) {
        List<ProductDTO> productDTOList = productService.getProductsByCategory(id);
        Map<String,Object> apiResponse = Map.of(
                "success",true,
                "message",productDTOList.isEmpty() ? "No products found" : "products fetched successfully",
                "data",productDTOList
        );
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getProductsByKeyword(@RequestParam("keyword") String keyword) {
        List<ProductDTO> productDTOList = productService.searchProducts(keyword);
        Map<String,Object> apiResponse = Map.of(
                "success",true,
                "message",productDTOList.isEmpty() ? "No product found" : "product fetched successfully",
                "data",productDTOList
        );
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("price-range")
    public ResponseEntity<Object> getProductsByRange(@RequestParam(value = "minPrice") Integer minPrice,@RequestParam("maxPrice") Integer maxPrice) {
        List<ProductDTO> productDTOList = productService.getProductsByPriceRange(minPrice,maxPrice);
        Map<String,Object> apiResponse = Map.of(
                "success",true,
                "message",productDTOList.isEmpty() ? "No product found" : "product fetched successfully",
                "data",productDTOList
        );
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }



    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/filter")
    public ResponseEntity<Object> filterProducts(
            @RequestParam(value ="categoryId",required = false) Integer categoryId,
            @RequestParam(value ="minPrice",required = false) Integer minPrice,
            @RequestParam(value ="maxPrice",required = false) Integer maxPrice,
            @RequestParam(value ="keyword",required = false) String keyword) {

        List<ProductDTO> filteredProducts = productService.filterProducts(categoryId, minPrice, maxPrice, keyword);

        Map<String, Object> apiResponse = Map.of(
                "success",true,
                "message",filteredProducts.isEmpty() ? "No products found" : "Filtered products",
                "data",filteredProducts
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}
