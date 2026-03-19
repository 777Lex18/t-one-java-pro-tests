package t.one.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import t.one.entity.Product;
import t.one.entity.producttype.ProductType;
import t.one.repository.ProductRepository;
import t.one.service.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getProductsByUserId(Long userId) {
        return productRepository.findByUserId(userId);
    }

    @Override
    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    @Transactional
    public Product createProduct(Long userId, String accountNumber, BigDecimal balance, String productType) {
        Product product = new Product(
                accountNumber,
                balance,
                ProductType.valueOf(productType),
                userId
        );
        return productRepository.save(product);
    }
}
