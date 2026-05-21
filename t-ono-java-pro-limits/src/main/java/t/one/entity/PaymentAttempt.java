package t.one.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import t.one.entity.paymentstatus.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "payment_attempts")
public class PaymentAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private PaymentStatus status;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    public PaymentAttempt(Long userId, BigDecimal amount, PaymentStatus status) {
        this.userId = userId;
        this.amount = amount;
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.paymentDate = LocalDate.now();
    }
}
