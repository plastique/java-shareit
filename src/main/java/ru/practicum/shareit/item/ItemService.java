package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.contracts.BookingRepositoryInterface;
import ru.practicum.shareit.exception.EmptyIdException;
import ru.practicum.shareit.exception.UserDoesNotHaveBookedItem;
import ru.practicum.shareit.item.contracts.CommentRepositoryInterface;
import ru.practicum.shareit.item.contracts.ItemRepositoryInterface;
import ru.practicum.shareit.item.contracts.ItemServiceInterface;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.exception.InvalidOwnerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.contracts.UserRepositoryInterface;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class ItemService implements ItemServiceInterface {

    private static final String USER_NOT_FOUND = "User with id='%d' not found";
    private static final String ITEM_NOT_FOUND = "Item with id='%d' not found";

    private final ItemRepositoryInterface itemRepository;
    private final UserRepositoryInterface userRepository;
    private final CommentRepositoryInterface commentRepository;
    private final BookingRepositoryInterface bookingRepository;

    @Override
    public ItemDto create(final ItemCreateDto itemDto, final Long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(USER_NOT_FOUND.formatted(userId))
        );

        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setOwner(user);
        item.setAvailable(itemDto.getAvailable());

        Item newItem = itemRepository.save(item);

        return ItemMapper.toItemDto(newItem);
    }

    @Override
    public ItemDto update(final ItemUpdateDto itemDto, final Long userId) {
        if (itemDto.getId() == null) {
            throw new EmptyIdException("Item id is empty");
        }

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(USER_NOT_FOUND.formatted(userId))
        );

        Item item = itemRepository.findById(itemDto.getId()).orElseThrow(
                () -> new NotFoundException(ITEM_NOT_FOUND.formatted(itemDto.getId()))
        );

        if (!Objects.equals(item.getOwner(), user)) {
            throw new InvalidOwnerException("Owner is not the same user");
        }

        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        item = itemRepository.save(item);

        return ItemMapper.toItemDto(item);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto findItemById(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException(ITEM_NOT_FOUND.formatted(itemId))
        );

        return ItemMapper.toItemDto(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> findItemsByUser(final Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(USER_NOT_FOUND.formatted(userId))
        );

        return itemRepository
                .findAllByOwnerId(user.getId())
                .stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> findItemsByText(final String text) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyList();
        }

        return itemRepository
                .findAllByText(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public CommentDto addComment(final Long itemId, final Long userId, final CommentCreateDto commentDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException(ITEM_NOT_FOUND.formatted(itemId))
        );

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(USER_NOT_FOUND.formatted(userId))
        );

        if (!bookingRepository.existsByBooker(user)) {
            throw new UserDoesNotHaveBookedItem("User doesn't have booked item");
        }

        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setUser(user);
        comment.setItem(item);

        commentRepository.save(comment);

        return CommentMapper.toCommentDto(comment);
    }

}
