package t.one.service;


import t.one.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> getProductsByUserId(Long userId);

    Optional<Product> getProductById(Long productId);

    Product createProduct(Long userId, String accountNumber, BigDecimal balance, String productType);

}