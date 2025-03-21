package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.user.contracts.UserRepositoryInterface;
import ru.practicum.shareit.user.model.User;

@DataJpaTest()
class UserRepositoryTests {

    @Autowired
    UserRepositoryInterface repository;

    @Autowired
    TestEntityManager em;

    @Test
    void existsByEmail() {
        String email = "user@somehost.net";
        User user = new User(null, "username", email);

        em.persist(user);
        em.flush();

        Assertions.assertTrue(
                repository.existsByEmail(email)
        );
    }
}
