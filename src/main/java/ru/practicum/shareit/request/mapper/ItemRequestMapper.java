package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;


public class ItemRequestMapper {

    private ItemRequestMapper() {
    }

    public static ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest) {
        return new ItemRequestResponseDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated()
        );
    }

}
