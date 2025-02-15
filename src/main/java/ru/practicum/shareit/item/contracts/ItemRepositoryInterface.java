package ru.practicum.shareit.item.contracts;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemRepositoryInterface {
    Item create(Item item);

    Item update(Item item);

    void delete(Integer id);

    Item findById(Integer id);

    List<Item> findByUserId(Integer userId);

    List<Item> findByText(String text);
}
