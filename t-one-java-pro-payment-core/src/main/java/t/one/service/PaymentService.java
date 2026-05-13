package t.one.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import t.one.client.ProductClient;
import t.one.dto.ProductResponse;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {
    private final ProductClient productClient;

    @Autowired
    public PaymentService(ProductClient productClient) {
        this.productClient = productClient;
    }

    public void executePayment(Long clientId, Long productId, Double amount) {
        List<ProductResponse> products = productClient.getProductsByClientId(clientId);
        if (products == null || products.isEmpty()) {
            throw new PaymentException("Продукты для клиента не найдены", "CLIENT_PRODUCTS_NOT_FOUND");
        }

        ProductResponse product = products.stream()
                .filter(p -> p.id().equals(productId))
                .findFirst()
                .orElseThrow(() -> new PaymentException("Продукт не найден", "PRODUCT_NOT_FOUND"));

        if (product.balance().compareTo(BigDecimal.valueOf(amount)) < 0) {
            throw new PaymentException("Недостаточно средств на продукте", "INSUFFICIENT_FUNDS");
        }

        // Здесь логика списания средств (в реальном проекте — транзакция, обновление баланса)
        // Для примера — просто лог
        System.out.println("Платеж выполнен: продукт " + product.accountNumber() + ", сумма " + amount);
    }

    public static class PaymentException extends RuntimeException {
        public PaymentException(String message, String detail) {
            super(message);
            this.detail = detail;
        }

        public final String detail;
    }
}
