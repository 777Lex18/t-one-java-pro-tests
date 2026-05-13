package t.one.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import t.one.dto.ErrorResponse;
import t.one.dto.PaymentRequest;
import t.one.service.PaymentService;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/execute")
    public ResponseEntity<?> executePayment(@RequestBody PaymentRequest request) {
        Long clientId = request.clientId();
        Long productId = request.productId();
        Double amount = request.amount();
        System.out.println("📥 Платеж: clientId=" + clientId + ", productId=" + productId + ", amount=" + amount);

        try {
            paymentService.executePayment(clientId, productId, amount);
            return ResponseEntity.ok(Map.of("status", "success"));
        } catch (PaymentService.PaymentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), e.detail);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Внутренняя ошибка сервера", "INTERNAL_ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
