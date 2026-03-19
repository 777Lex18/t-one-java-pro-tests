package t.one.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import t.one.service.ProductService;
import t.one.service.UserService;

import java.math.BigDecimal;

@Component
public class AppRunner implements CommandLineRunner {

    private final UserService userService;
    private final ProductService productService;

    public AppRunner(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n🔹 === Запуск демо-сценария ===");

        // Проверяем/создаём alice
        var alice = userService.findByUsername("alice")
                .orElseGet(() -> {
                    System.out.println("✨ Создаю пользователя 'alice'");
                    return userService.createUser("alice");
                });
        System.out.println("✅ Пользователь 'alice' готов (ID=" + alice.getId() + ")");

        // Проверяем/создаём bob
        var bob = userService.findByUsername("bob")
                .orElseGet(() -> {
                    System.out.println("✨ Создаю пользователя 'bob'");
                    return userService.createUser("bob");
                });
        System.out.println("✅ Пользователь 'bob' готов (ID=" + bob.getId() + ")");

        // Добавляем продукты alice (всегда, независимо от того, был создан или найден)
        System.out.println("\n🔹 Добавляю продукты для alice:");
        productService.createProduct(alice.getId(), "ACC-ALICE-001", new BigDecimal("5000.00"), "ACCOUNT");
        productService.createProduct(alice.getId(), "CARD-ALICE-4242", new BigDecimal("150.75"), "CARD");
        System.out.println("✅ Продукты для alice добавлены");

        // Добавляем продукты bob
        System.out.println("\n🔹 Добавляю продукты для bob:");
        productService.createProduct(bob.getId(), "CARD-BOB-9999", new BigDecimal("2500.00"), "CARD");
        System.out.println("✅ Продукты для bob добавлены");

        // Демонстрация: запрос продуктов по userId
        System.out.println("\n🔹 Продукты alice:");
        productService.getProductsByUserId(alice.getId()).forEach(p ->
                System.out.println("   - " + p)
        );

        // Демонстрация: запрос продукта по productId
        System.out.println("\n🔹 Поиск продукта по ID:");
        var firstProduct = productService.getProductsByUserId(alice.getId()).stream().findFirst();
        firstProduct.flatMap(p -> productService.getProductById(p.getId()))
                .ifPresent(p -> System.out.println("   Найден: " + p));

        System.out.println("\n✅ Демо завершено.");
    }
}
