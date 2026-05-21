package t.one.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import t.one.entity.PaymentAttempt;
import t.one.entity.paymentstatus.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface PaymentAttemptRepository extends JpaRepository<PaymentAttempt, Long> {

    @Query("SELECT COALESCE(SUM(pa.amount), 0) FROM PaymentAttempt pa WHERE pa.userId = :userId AND pa.status = :status")
    BigDecimal sumSuccessfulPaymentsByUserId(@Param("userId") Long userId, @Param("status") PaymentStatus status);

    @Query("SELECT COALESCE(SUM(pa.amount), 0) FROM PaymentAttempt pa " +
            "WHERE pa.userId = :userId AND pa.status = :status AND pa.paymentDate = :today")
    BigDecimal sumSuccessfulPaymentsByUserIdAndDate(
            @Param("userId") Long userId,
            @Param("status") PaymentStatus status,
            @Param("today") LocalDate today
    );
}
