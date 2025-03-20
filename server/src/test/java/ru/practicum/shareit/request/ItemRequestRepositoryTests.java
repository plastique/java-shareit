package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.request.contracts.ItemRequestRepositoryInterface;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@DataJpaTest()
class ItemRequestRepositoryTests {

    @Autowired
    ItemRequestRepositoryInterface repository;

    @Autowired
    TestEntityManager em;

    @Test
    void findAllByRequestor_IdOrderByCreatedDesc() {
        int rowsCount = 0;
        int userCount = 2;

        List<User> userList = new ArrayList<>(userCount);

        for (int j = 1; j <= userCount; j++) {
            User user = new User(null, "username" + j, "user" + j);
            em.persist(user);
            userList.add(user);

            rowsCount = new Random().nextInt(1, 10);

            for (int i = 1; i <= rowsCount; i++) {
                ItemRequest itemRequest = new ItemRequest(
                        null,
                        "description",
                        user,
                        LocalDateTime.now()
                );
                em.persist(itemRequest);
            }
        }

        em.flush();

        Assertions.assertEquals(
                rowsCount,
                repository.findAllByRequestor_IdOrderByCreatedDesc(
                        userList.getLast().getId()
                ).size()
        );
    }

    @Test
    void findAllByRequestor_IdIsNotOrderByCreatedDesc() {
        int rowsCount = 0;
        int userCount = 2;

        List<User> userList = new ArrayList<>(userCount);

        for (int j = 1; j <= userCount; j++) {
            User user = new User(null, "username" + j, "user" + j);
            em.persist(user);
            userList.add(user);

            rowsCount = new Random().nextInt(1, 10);

            for (int i = 1; i <= rowsCount; i++) {
                ItemRequest itemRequest = new ItemRequest(
                        null,
                        "description",
                        user,
                        LocalDateTime.now()
                );
                em.persist(itemRequest);
            }
        }

        em.flush();

        Assertions.assertEquals(
                rowsCount,
                repository.findAllByRequestor_IdIsNotOrderByCreatedDesc(
                        userList.getFirst().getId()
                ).size()
        );
    }
}
