package ru.practicum.shareit.item.contracts;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemServiceInterface {

    ItemDto create(ItemDto itemDto, Long userId);

    ItemDto update(ItemDto itemDto, Long userId);

    ItemDto findItemById(Long itemId);

    List<ItemDto> findItemsByUser(Long userId);

    List<ItemDto> findItemsByText(String text);

    CommentDto addComment(Long itemId, Long userId, CommentDto commentDto);

}
