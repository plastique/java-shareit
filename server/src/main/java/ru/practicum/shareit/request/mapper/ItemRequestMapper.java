package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.List;

public class ItemRequestMapper {

    private ItemRequestMapper() {
    }

    public static ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest, List<Item> items) {
        List<ItemRequestResponseDto.ItemDto> itemDtos = new ArrayList<>();

        if (items != null) {
            for (Item item : items) {
                itemDtos.add(
                        new ItemRequestResponseDto.ItemDto(item.getId(), item.getName())
                );
            }
        }

        return new ItemRequestResponseDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                itemDtos
        );
    }

}
