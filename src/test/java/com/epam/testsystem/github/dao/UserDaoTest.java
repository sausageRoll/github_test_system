package com.epam.testsystem.github.dao;

import com.epam.testsystem.github.TestUtil;
import com.epam.testsystem.github.enums.UserRoleType;
import com.epam.testsystem.github.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.epam.testsystem.github.EnvironmentConstant.SPRING_PROFILE_TEST;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * github_test
 * Created on 05.07.17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_TEST)
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ContactDao contactDao;

    @Autowired
    private TestUtil testUtil;

    @Test
    @Transactional
    public void add() throws Exception {
        final String email = "EMAIL";
        final String gitNick = "github_nick";
        final String password = "password";
        final String userRole = "ROLE_USER";

        assertThat(userDao.add(email, gitNick, password, UserRoleType.ROLE_USER.name()))
                .satisfies(
                        u -> {
                            assertThat(u.getEmail()).isEqualTo(email);
                            assertThat(u.getGitNick()).isEqualTo(gitNick);
                            assertThat(u.getPassword()).isEqualTo(password);
                            assertThat(u.getRole()).isEqualTo(userRole);
                            assertThat(u.getId()).isGreaterThan(0);
                        }
                );
    }

    @Test
    @Transactional
    @Sql(statements = {
            "INSERT INTO users(id, email, git_nick, password, role) VALUES(1000, 'email', 'gitNick', 'password', 'ROLE_USER')"
    })
    public void findById() throws Exception {
        final User user = User.builder().id(1000).email("email").gitNick("gitNick").password("password").role("ROLE_USER").build();
        assertThat(userDao.findById(user.getId())).contains(user);
    }

    public void findByIdNotExists() {
        assertThat(userDao.findById(0)).isEmpty();
    }

    @Test
    @Transactional
    @Sql(statements = {
            "INSERT INTO users(id, email, git_nick, password, role) VALUES(1000, 'email', 'gitNick', 'password', 'ROLE_USER')"
    })
    public void findByEmail() throws Exception {
        final User user = User.builder().id(1000).email("email").gitNick("gitNick").password("password").role("ROLE_USER").build();
        assertThat(userDao.findByEmail("email")).contains(user);
    }

    public void findByEmailNotExists() {
        assertThat(userDao.findByEmail("")).isEmpty();
    }

    @Test
    @Transactional
    public void findAllWithTasks() {
        final User user1 = testUtil.makeUser();
        final User user2 = testUtil.makeUser();
        final Repo repo = testUtil.addRepo();

        final Task task11 = testUtil.addTask(repo.getId(), user1.getId());

        final Task task21 = testUtil.addTask(repo.getId(), user2.getId());
        final Task task22 = testUtil.addTask(repo.getId(), user2.getId());
        final Task task23 = testUtil.addTask(repo.getId(), user2.getId());

        final List<UserWithTasks> tmp = userDao.findAllWithTasks();

        assertThat(tmp)
                .contains(
                        UserWithTasks.builder()
                                .user(user1)
                                .tasks(Collections.singletonList(task11))
                                .build(),
                        UserWithTasks.builder()
                                .user(user2)
                                .tasks(Arrays.asList(task21, task22, task23))
                                .build()
                );
    }

    @Test
    @Transactional
    public void findUserByContact() {
        final User user = testUtil.makeUser();

        final Contact contact = contactDao.add(user.getId(), "VK", "inf");

        assertThat(userDao.findByContact(contact.getType(), contact.getInf()).get(0))
                .isEqualTo(user);
    }
}