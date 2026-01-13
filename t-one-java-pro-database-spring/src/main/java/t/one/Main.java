package t.one;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import t.one.configuration.ConfigurationDatabase;
import t.one.configuration.DatabaseInitializer;
import t.one.service.UserService;

public class Main {

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("✅ PostgreSQL Driver loaded.");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ PostgreSQL Driver NOT found in classpath!");
            e.printStackTrace();
            return;
        }

        try (var context = new AnnotationConfigApplicationContext(ConfigurationDatabase.class,
                DatabaseInitializer.class)) {
            // Получаем UserService
            UserService userService = context.getBean(UserService.class);

            // 1. Создание пользователей
            System.out.println("\n🔹 Создаём пользователей...");
            userService.createUser("alice");
            userService.createUser("bob");

            // 2. Получение всех пользователей
            System.out.println("\n🔹 Все пользователи:");
            userService.getAllUsers().forEach(System.out::println);

            // 3. Получение одного пользователя
            System.out.println("\n🔹 Пользователь с ID=1:");
            userService.getUserById(1L).ifPresent(System.out::println);

            // 4. Обновление пользователя
            System.out.println("\n🔹 Обновляем пользователя с ID=1 на 'alice_new'");
            userService.updateUser(1L, "alice_new");
            userService.getUserById(1L).ifPresent(System.out::println);

            // 5. Удаление пользователя
            System.out.println("\n🔹 Удаляем пользователя с ID=2");
            userService.deleteUser(2L);

            // 6. Финальный список
            System.out.println("\n🔹 Оставшиеся пользователи:");
            userService.getAllUsers().forEach(System.out::println);
        }
    }
}