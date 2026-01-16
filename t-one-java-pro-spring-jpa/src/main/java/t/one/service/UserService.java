package t.one.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import t.one.entity.User;
import t.one.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(String username) {
        User user = new User(username);
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser(Long id, String newUsername) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(newUsername);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    @Transactional
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    // Дополнительный метод из Query
    @Transactional
    public List<User> findUsersByUsernamePrefix(String prefix) {
        return userRepository.findByUsernameStartingWith(prefix);
    }
}
