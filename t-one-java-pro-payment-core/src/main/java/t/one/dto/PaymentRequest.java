package t.one.dto;

public record PaymentRequest(Long clientId, Long productId, Double amount) {}
