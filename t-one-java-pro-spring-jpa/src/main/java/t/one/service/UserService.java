package t.one.service;

import t.one.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(String username);

    Optional<User> getUserById(Long id);

    List<User> getAllUsers();

    User updateUser(Long id, String newUsername);

    void deleteUser(Long id);

    List<User> findUsersByUsernamePrefix(String prefix);

    Optional<User> findByUsername(String username);
}
