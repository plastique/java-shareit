package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.contracts.ItemRepositoryInterface;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestService implements ItemRequestServiceInterface {

    private final ItemRequestRepositoryInterface itemRequestRepository;
    private final UserRepositoryInterface userRepository;
    private final ItemRepositoryInterface itemRepository;

    @Override
    public ItemRequestResponseDto create(final ItemRequestCreateDto itemRequestCreateDto) {
        log.info("Create itemRequest request: {}", itemRequestCreateDto);

        User user = userRepository.findById(itemRequestCreateDto.getUserId()).orElseThrow(
                () -> new NotFoundException("User not found with id: " + itemRequestCreateDto.getUserId())
        );

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(user);
        itemRequest.setDescription(itemRequestCreateDto.getDescription());
        itemRequest.setCreated(LocalDateTime.now());

        itemRequestRepository.save(itemRequest);

        return ItemRequestMapper.toItemRequestResponseDto(itemRequest, null);
    }

    @Override
    public List<ItemRequestResponseDto> getListByUser(final Long userId) {
        log.info("Get itemRequests by user: {}", userId);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found with id: " + userId)
        );

        return itemRequestRepository.findAllByRequestor_IdOrderByCreatedDesc(user.getId())
                .stream()
                .map(itemRequest -> ItemRequestMapper.toItemRequestResponseDto(itemRequest, null))
                .toList();
    }

    @Override
    public List<ItemRequestResponseDto> getList(final Long userId) {
        log.info("Get itemRequests with user: {}", userId);

        return (
                userId == null
                        ? itemRequestRepository.findAll(Sort.by(Sort.Direction.DESC, "created"))
                        : itemRequestRepository.findAllByRequestor_IdIsNotOrderByCreatedDesc(userId)
        ).stream()
         .map(itemRequest -> ItemRequestMapper.toItemRequestResponseDto(itemRequest, null))
         .toList();
    }

    @Override
    public ItemRequestResponseDto getById(final Long id) {
        log.info("Get itemRequest by id: {}", id);

        ItemRequest itemRequest = itemRequestRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Item request with id: " + id)
        );

        return ItemRequestMapper.toItemRequestResponseDto(
                itemRequest,
                itemRepository.findAllByRequest_IdOrderByIdDesc(itemRequest.getId())
        );
    }

}
