package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.contracts.BookingRepositoryInterface;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.EmptyIdException;
import ru.practicum.shareit.exception.UserDoesNotHaveBookedItem;
import ru.practicum.shareit.item.contracts.CommentRepositoryInterface;
import ru.practicum.shareit.item.contracts.ItemRepositoryInterface;
import ru.practicum.shareit.item.contracts.ItemServiceInterface;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.exception.InvalidOwnerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.contracts.ItemRequestRepositoryInterface;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.contracts.UserRepositoryInterface;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

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
    private final ItemRequestRepositoryInterface itemRequestRepositoryInterface;

    private final Sort commentsSort = Sort.by(Sort.Direction.DESC, "created");
    private final Sort bookingOrder = Sort.by(Sort.Direction.ASC, "start");

    @Override
    public ItemDto create(final ItemCreateDto itemDto, final Long userId) {
        log.info("Create item {} by user {}", itemDto, userId);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(USER_NOT_FOUND.formatted(userId))
        );

        ItemRequest itemRequest = null;

        if (itemDto.getRequestId() != null) {
            itemRequest = itemRequestRepositoryInterface.findById(itemDto.getRequestId()).orElseThrow(
                    () -> new NotFoundException("Item request with id='%d' not found".formatted(itemDto.getRequestId()))
            );
        }

        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setOwner(user);
        item.setAvailable(itemDto.getAvailable());

        if (itemRequest != null) {
            item.setRequest(itemRequest);
        }

        Item newItem = itemRepository.save(item);

        return ItemMapper.toItemDto(newItem);
    }

    @Override
    public ItemDto update(final ItemUpdateDto itemDto, final Long userId) {
        log.info("Update item {} by user {}", itemDto, userId);

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
    public ItemInfoDto findItemById(Long itemId, Long userId) {
        log.info("Find item by id {} and user {}", itemId, userId);

        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException(ITEM_NOT_FOUND.formatted(itemId))
        );

        List<Booking> bookings = Collections.emptyList();

        if (item.getOwner().getId().equals(userId)) {
            bookings = bookingRepository.findAllByItem_IdInAndStatus(
                    List.of(item.getId()),
                    BookingStatus.APPROVED,
                    bookingOrder
            );
        }

        LocalDateTime currentTime = LocalDateTime.now();

        return ItemMapper.toItemCardDto(
                item,
                commentRepository.findAllByItem_Id(item.getId(), commentsSort),
                getLastBooking(bookings, currentTime),
                getNextBooking(bookings, currentTime)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemInfoDto> findItemsByOwner(final Long ownerId) {
        log.info("Find items by owner {}", ownerId);

        User user = userRepository.findById(ownerId).orElseThrow(
                () -> new NotFoundException(USER_NOT_FOUND.formatted(ownerId))
        );

        List<Item> items = itemRepository.findAllByOwnerId(user.getId());

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> itemIds = items.stream().map(Item::getId).toList();
        Map<Long, List<Comment>> comments = commentRepository
                .findAllByItem_IdIn(itemIds, commentsSort)
                .stream()
                .collect(groupingBy(comment -> comment.getItem().getId(), toList()));

        Map<Long, List<Booking>> bookings = bookingRepository
                .findAllByItem_IdInAndStatus(itemIds, BookingStatus.APPROVED, bookingOrder)
                .stream()
                .collect(groupingBy(booking -> booking.getItem().getId(), toList()));

        LocalDateTime curDateTime = LocalDateTime.now();

        return items
                .stream()
                .map(item -> ItemMapper.toItemCardDto(
                        item,
                        comments.getOrDefault(item.getId(), Collections.emptyList()),
                        getLastBooking(
                                bookings.getOrDefault(item.getId(), Collections.emptyList()),
                                curDateTime
                        ),
                        getNextBooking(
                                bookings.getOrDefault(item.getId(), Collections.emptyList()),
                                curDateTime
                        )
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> findItemsByText(final String text) {
        log.info("Find items by text {}", text);

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
    public CommentDto addComment(final Long itemId, final Long authorId, final CommentCreateDto commentDto) {
        log.info("Add comment {} for item {} by user {}", commentDto, itemId, authorId);

        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException(ITEM_NOT_FOUND.formatted(itemId))
        );

        User author = userRepository.findById(authorId).orElseThrow(
                () -> new NotFoundException(USER_NOT_FOUND.formatted(authorId))
        );

        if (
                !bookingRepository.existsByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(
                        author.getId(),
                        item.getId(),
                        BookingStatus.APPROVED,
                        LocalDateTime.now()
                )
        ) {
            throw new UserDoesNotHaveBookedItem("User doesn't have booked item");
        }

        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        commentRepository.save(comment);

        return CommentMapper.toCommentDto(comment);
    }

    private static Booking getNextBooking(List<Booking> bookings, LocalDateTime curDateTime) {
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }

        return bookings.stream()
                       .filter(booking -> booking.getStart().isAfter(curDateTime))
                       .findFirst()
                       .orElse(null);
    }

    private static Booking getLastBooking(List<Booking> bookings, LocalDateTime curDateTime) {
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }

        return bookings.stream()
                       .filter(booking -> !booking.getStart().isAfter(curDateTime))
                       .reduce((a, b) -> b)
                       .orElse(null);
    }
}
