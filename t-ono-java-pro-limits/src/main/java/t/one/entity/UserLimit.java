package t.one.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "user_limits")
@Getter @Setter @NoArgsConstructor
public class UserLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long userId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal dailyLimit = BigDecimal.valueOf(10000.0);

    @Column(name = "last_reset")
    private LocalDate lastReset = LocalDate.now();

    public UserLimit(Long userId, BigDecimal dailyLimit, LocalDate lastReset) {
        this.userId = userId;
        this.dailyLimit = dailyLimit;
        this.lastReset = lastReset;
    }
}
