package t.one.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import t.one.entity.UserLimit;
import t.one.service.LimitService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/limits")
public class LimitController {

    private final LimitService limitService;

    public LimitController(LimitService limitService) {
        this.limitService = limitService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserLimit> getLimit(@PathVariable Long userId) {
        UserLimit limit = limitService.getOrCreateLimit(userId);
        return ResponseEntity.ok(limit);
    }

    @PostMapping("/pay")
    public ResponseEntity<Boolean> processPayment(@RequestParam Long userId, @RequestParam BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body(false);
        }
        boolean success = limitService.processPayment(userId, amount);
        return ResponseEntity.ok(success);
    }

    @GetMapping("/{userId}/available")
    public ResponseEntity<BigDecimal> getAvailableLimit(@PathVariable Long userId) {
        BigDecimal available = limitService.getAvailableLimit(userId);
        return ResponseEntity.ok(available);
    }

}
