package t.one.service;

import t.one.entity.UserLimit;

import java.math.BigDecimal;

public interface LimitService {
    UserLimit getOrCreateLimit(Long userId);
    boolean processPayment(Long userId, BigDecimal amount);
    BigDecimal getAvailableLimit(Long userId);
}
