package com.epam.testsystem.github.dao;

import com.epam.testsystem.github.model.User;
import com.epam.testsystem.github.model.UserWithTasks;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.epam.testsystem.github.dao.DaoExtractorUtil.USER_ROW_MAPPER;
import static org.springframework.dao.support.DataAccessUtils.singleResult;

/**
 * github_test
 * Created on 05.07.17.
 */

@Repository
@Transactional(propagation = Propagation.MANDATORY)
@RequiredArgsConstructor
public class UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final DaoExtractorUtil daoExtractorUtil;

    public User add(final String email, final String gitNick, final String password, final String roleName) {
        jdbcTemplate.update(
                "INSERT INTO users(email, git_nick, password, role) VALUES (?, ?, ?, ?) ",
                email, gitNick, password, roleName);

        return singleResult(jdbcTemplate.query(
                "SELECT * FROM users WHERE id = last_insert_id()",
                USER_ROW_MAPPER)
        );
    }

    public Optional<User> findByEmail(final String email) {
        return Optional.ofNullable(
                singleResult(jdbcTemplate.query(
                        "SELECT * FROM users WHERE email = ?",
                        new Object[]{email},
                        USER_ROW_MAPPER))
        );
    }

    public Optional<User> findById(final long id) {
        return Optional.ofNullable(
                singleResult(jdbcTemplate.query(
                        "SELECT * FROM users WHERE id = ?",
                        new Object[]{id},
                        USER_ROW_MAPPER))
        );
    }

    public List<UserWithTasks> findAllWithTasks() {
        return jdbcTemplate.query(
                "SELECT * FROM tasks RIGHT JOIN users ON tasks.user_id = users.id WHERE role = 'ROLE_USER' ORDER BY register_time DESC",
                daoExtractorUtil
        );
    }

    public List<User> findByContact(final String type, final String inf) {
        return jdbcTemplate.query(
                "SELECT * FROM users WHERE id = " +
                        "(SELECT user_id FROM contacts WHERE type = ? AND inf = ?)",
                new Object[]{type, inf},
                USER_ROW_MAPPER
        );
    }
}
