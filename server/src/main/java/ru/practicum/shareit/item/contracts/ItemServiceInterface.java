package ru.practicum.shareit.item.contracts;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemServiceInterface {

    ItemDto create(ItemCreateDto itemDto, Long userId);

    ItemDto update(ItemUpdateDto itemDto, Long userId);

    ItemInfoDto findItemById(Long itemId, Long userId);

    List<ItemInfoDto> findItemsByOwner(Long userId);

    List<ItemDto> findItemsByText(String text);

    CommentDto addComment(Long itemId, Long userId, CommentCreateDto commentDto);

}
