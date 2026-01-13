package t.one.repository;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import t.one.entity.User;

import java.util.List;
import java.util.Optional;

//@Repository
public class UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) { // ← внедряем бин
        this.jdbcTemplate = jdbcTemplate;
    }
        private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        return user;
    };

    public void create(User user) {
        jdbcTemplate.update(
                "INSERT INTO users (username) VALUES (?)",
                user.getUsername()
        );
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM users WHERE id = ?",
                        USER_ROW_MAPPER,
                        id
                )
        );
    }

    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", USER_ROW_MAPPER);
    }

    public void update(User user) {
        jdbcTemplate.update(
                "UPDATE users SET username = ? WHERE id = ?",
                user.getUsername(),
                user.getId()
        );
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }
}
