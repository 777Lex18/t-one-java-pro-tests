package t.one.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import t.one.dto.ProductResponse;
import t.one.entity.Product;
import t.one.service.ProductService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Product>> getProductsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(productService.getProductsByUserId(userId));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Optional<Product> product = productService.getProductById(productId);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ProductResponse>> getProductsByUserIdCore(@PathVariable Long userId) {
        System.out.println("@PathVariable Long userId=" + userId);
        List<Product> products = productService.getProductsByUserId(userId);
        System.out.println("products.getFirst() =" + products.getFirst());
        List<ProductResponse> responses = products.stream()
                .map(p -> new ProductResponse(
                        p.getId(),
                        p.getAccountNumber(),
                        p.getBalance(),
                        p.getProductType().name()
                ))
                .toList();
        System.out.println("responses.getFirst() =" + responses.getFirst());
        return ResponseEntity.ok(responses);
    }
}
