package t.one.service;

import t.one.entity.User;
import t.one.repository.UserDao;

import java.util.List;
import java.util.Optional;

//@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void createUser(String username) {
        User user = new User();
        user.setUsername(username);
        userDao.create(user);
    }

    public Optional<User> getUserById(Long id) {
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public void updateUser(Long id, String newUsername) {
        userDao.findById(id).ifPresent(user -> {
            user.setUsername(newUsername);
            userDao.update(user);
        });
    }

    public void deleteUser(Long id) {
        userDao.deleteById(id);
    }
}
