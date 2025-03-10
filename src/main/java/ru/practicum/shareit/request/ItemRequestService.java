package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.contracts.ItemRequestRepositoryInterface;
import ru.practicum.shareit.request.contracts.ItemRequestServiceInterface;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.contracts.UserRepositoryInterface;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestService implements ItemRequestServiceInterface {

    private final ItemRequestRepositoryInterface itemRequestRepository;
    private final UserRepositoryInterface userRepository;

    @Override
    public ItemRequestResponseDto create(final ItemRequestCreateDto itemRequestCreateDto) {
        User user = userRepository.findById(itemRequestCreateDto.getUserId()).orElseThrow(
                () -> new NotFoundException("User not found with id: " + itemRequestCreateDto.getUserId())
        );

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(user);
        itemRequest.setDescription(itemRequestCreateDto.getText());
        itemRequest.setCreated(LocalDateTime.now());

        itemRequestRepository.save(itemRequest);

        return ItemRequestMapper.toItemRequestResponseDto(itemRequest);
    }

    @Override
    public List<ItemRequestResponseDto> getListByUser(final Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found with id: " + userId)
        );

        return itemRequestRepository.findAllByRequestor_IdOrderByCreatedDesc(user.getId())
                .stream()
                .map(ItemRequestMapper::toItemRequestResponseDto)
                .toList();
    }

    @Override
    public List<ItemRequestResponseDto> getList(final Long userId) {
        return (
                userId == null
                        ? itemRequestRepository.allOrderByCreatedDesc()
                        : itemRequestRepository.findAllByRequestor_IdIsNotOrderByCreatedDesc(userId)
        ).stream()
         .map(ItemRequestMapper::toItemRequestResponseDto)
         .toList();
    }

    @Override
    public ItemRequestResponseDto getById(final Long id) {
        return ItemRequestMapper.toItemRequestResponseDto(itemRequestRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Item request with id: " + id)
        ));
    }

}
