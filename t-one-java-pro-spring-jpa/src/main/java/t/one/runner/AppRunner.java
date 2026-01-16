package t.one.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import t.one.service.UserService;

@Component
public class AppRunner implements CommandLineRunner {

    private final UserService userService;

    public AppRunner(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n🔹 === Запуск демо-сценария ===");

        // 1. Создание
        var alice = userService.createUser("alice");
        var bob = userService.createUser("bob");
        System.out.println("✅ Созданы: " + alice + ", " + bob);

        // 2. Все пользователи
        System.out.println("\n🔹 Все пользователи:");
        userService.getAllUsers().forEach(System.out::println);

        // 3. Поиск по ID
        System.out.println("\n🔹 Пользователь с ID=1:");
        userService.getUserById(1L).ifPresent(System.out::println);

        // 4. Обновление
        System.out.println("\n🔹 Обновляем пользователя с ID=1:");
        var updated = userService.updateUser(1L, "alice_new");
        System.out.println("✅ Обновлён: " + updated);

        // 5. Кастомный поиск
        System.out.println("\n🔹 Пользователи с именем, начинающимся на 'a':");
        userService.findUsersByUsernamePrefix("a").forEach(System.out::println);

        // 6. Удаление
        System.out.println("\n🔹 Удаляем пользователя с ID=2");
        userService.deleteUser(2L);

        // 7. Финальный список
        System.out.println("\n🔹 Оставшиеся пользователи:");
        userService.getAllUsers().forEach(System.out::println);

        System.out.println("\n✅ Демо завершено.");
    }
}
