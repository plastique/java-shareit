package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.contracts.ItemRepositoryInterface;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@DataJpaTest()
class ItemRepositoryTests {

    @Autowired
    ItemRepositoryInterface repository;

    @Autowired
    TestEntityManager em;

    @Test
    void findAllByOwnerId() {
        int rowsCount = 0;
        int userCount = 2;

        List<User> userList = new ArrayList<>(userCount);

        for (int j = 1; j <= userCount; j++) {
            User user = new User(null, "username" + j, "user" + j);
            em.persist(user);
            userList.add(user);

            rowsCount = new Random().nextInt(1, 10);

            for (int i = 1; i <= rowsCount; i++) {
                Item item = new Item(
                        null,
                        "Name" + i,
                        "Description" + i,
                        true,
                        user,
                        null
                );
                em.persist(item);
            }
        }

        em.flush();

        Assertions.assertEquals(
                rowsCount,
                repository.findAllByOwnerId(
                        userList.getLast().getId()
                ).size()
        );
    }

    @Test
    void findAllByText() {

        User user = new User(null, "username", "user");
        em.persist(user);

        Item item = new Item(
                null,
                "Name",
                "Description",
                true,
                user,
                null
        );
        em.persist(item);

        em.flush();

        Assertions.assertEquals(1, repository.findAllByText("description").size());
        Assertions.assertEquals(1, repository.findAllByText("name").size());
        Assertions.assertEquals(0, repository.findAllByText("some text").size());
    }

    @Test
    void findAllByRequest_IdOrderByIdDesc() {

        User user = new User(null, "username", "user");
        em.persist(user);

        ItemRequest itemRequest = new ItemRequest(null, "Description", user, LocalDateTime.now());
        em.persist(itemRequest);

        Item item = new Item(
                null,
                "Name",
                "Description",
                true,
                user,
                itemRequest
        );
        em.persist(item);

        em.flush();

        Assertions.assertEquals(
                1,
                repository.findAllByRequest_IdOrderByIdDesc(itemRequest.getId()).size()
        );
    }
}
