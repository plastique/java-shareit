package ru.practicum.shareit.request.contracts;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestServiceInterface {

    ItemRequestResponseDto create(ItemRequestCreateDto itemRequestCreateDto);

    List<ItemRequestResponseDto> getListByUser(Long userId);

    List<ItemRequestResponseDto> getList(Long userId);

    ItemRequestResponseDto getById(Long id);
}
