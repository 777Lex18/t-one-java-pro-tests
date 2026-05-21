package t.one.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import t.one.entity.PaymentAttempt;
import t.one.entity.UserLimit;
import t.one.entity.paymentstatus.PaymentStatus;
import t.one.repository.PaymentAttemptRepository;
import t.one.repository.UserLimitRepository;
import t.one.service.LimitService;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Transactional
public class LimitServiceImpl implements LimitService {
    private final UserLimitRepository userLimitRepository;
    private final PaymentAttemptRepository paymentAttemptRepository;

    public LimitServiceImpl(UserLimitRepository userLimitRepository, PaymentAttemptRepository paymentAttemptRepository) {
        this.userLimitRepository = userLimitRepository;
        this.paymentAttemptRepository = paymentAttemptRepository;
    }

    @Override
    public boolean processPayment(Long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) return false;

        UserLimit limit = userLimitRepository.findByUserId(userId)
                .orElseGet(() -> userLimitRepository.save(new UserLimit(userId, BigDecimal.valueOf(10000.0), LocalDate.now())));

        BigDecimal spentToday = getSpentToday(userId); // ← ИСПОЛЬЗУЕТСЯ!

        if (limit.getDailyLimit().compareTo(spentToday.add(amount)) < 0) {
            return false;
        }

        PaymentAttempt attempt = new PaymentAttempt(userId, amount, PaymentStatus.PENDING);
        paymentAttemptRepository.save(attempt);

        boolean externalSuccess = externalPaymentService.charge(userId, amount);

        attempt.setStatus(externalSuccess ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        paymentAttemptRepository.save(attempt);

        return externalSuccess;
    }

    private BigDecimal getSpentToday(Long userId) {
        return paymentAttemptRepository.sumSuccessfulPaymentsByUserIdAndDate(
                userId, PaymentStatus.SUCCESS, LocalDate.now()
        );
    }
    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAvailableLimit(Long userId) {
        UserLimit limit = getOrCreateLimit(userId);
        BigDecimal spentToday = getSpentToday(userId);
        return limit.getDailyLimit().subtract(spentToday);
    }

    @Override
    public UserLimit getOrCreateLimit(Long userId) {
        return userLimitRepository.findByUserId(userId)
                .orElseGet(() -> userLimitRepository.save(new UserLimit(userId, BigDecimal.valueOf(10000.0), LocalDate.now())));
    }

    private final ExternalPaymentService externalPaymentService = new ExternalPaymentService();

    static class ExternalPaymentService {
        boolean charge(Long userId, BigDecimal amount) {
            return Math.random() > 0.2;
        }
    }
}
