package ru.practicum.shareit.item.contracts;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemServiceInterface {

    ItemDto create(ItemDto itemDto, Integer userId);

    ItemDto update(ItemDto itemDto, Integer userId);

    ItemDto findItemById(Integer itemId);

    List<ItemDto> findItemsByUser(Integer userId);

    List<ItemDto> findItemsByText(String text);
}
