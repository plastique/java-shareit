package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.contracts.BookingRepositoryInterface;
import ru.practicum.shareit.booking.contracts.BookingServiceInterface;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.InvalidBookingStatusException;
import ru.practicum.shareit.exception.InvalidOwnerException;
import ru.practicum.shareit.exception.ItemUnavailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.contracts.ItemRepositoryInterface;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.contracts.UserRepositoryInterface;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class BookingService implements BookingServiceInterface {

    private static final String BOOKING_NOT_FOUND = "Booking with id='%d' not found";
    private static final String USER_NOT_FOUND = "User with id='%d' not found";

    private final BookingRepositoryInterface bookingRepository;
    private final ItemRepositoryInterface itemRepository;
    private final UserRepositoryInterface userRepository;
    private final Sort sort = Sort.by(Sort.Direction.DESC, "start");

    @Override
    @Transactional
    public BookingDto create(final BookingCreateDto bookingDto) {
        log.info("Create booking={}", bookingDto);

        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(
                () -> new NotFoundException("Item with id='%d' not found".formatted(bookingDto.getItemId()))
        );

        User booker = userRepository.findById(bookingDto.getBookerId()).orElseThrow(
                () -> new NotFoundException(USER_NOT_FOUND.formatted(bookingDto.getBookerId()))
        );

        if (!item.getAvailable()) {
            throw new ItemUnavailableException("Item not available");
        }

        Booking booking = new Booking();

        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setStatus(BookingStatus.WAITING);

        return BookingMapper.toBookingDto(
                bookingRepository.save(booking)
        );
    }

    @Override
    @Transactional
    public BookingDto approve(final Long id, final Long ownerId, final boolean approved) {
        log.info("Approve booking={}, owner: {}", id, ownerId);

        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new NotFoundException(BOOKING_NOT_FOUND.formatted(id))
        );

        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new InvalidBookingStatusException("Booking with id='%d' is already in progress".formatted(id));
        }

        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new InvalidOwnerException("User id='%d' is not owner".formatted(ownerId));
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        bookingRepository.save(booking);

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getById(final Long id, final Long userId) {
        log.info("Get booking={}, userId: {}", id, userId);

        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new NotFoundException(BOOKING_NOT_FOUND.formatted(id))
        );

        if (
                booking.getBooker().getId().equals(userId)
                        || booking.getItem().getOwner().getId().equals(userId)
        ) {
            return BookingMapper.toBookingDto(booking);
        }

        throw new NotFoundException(BOOKING_NOT_FOUND.formatted(id));
    }

    @Override
    public List<BookingDto> getByBookerAndState(final Long bookerId, final BookingState state) {
        log.info("Get bookings by booker={}, state: {}", bookerId, state);

        User booker = userRepository.findById(bookerId).orElseThrow(
                () -> new NotFoundException(USER_NOT_FOUND.formatted(bookerId))
        );

        List<Booking> list = switch (state) {
            case WAITING -> bookingRepository.findAllByBookerAndStatus(booker, BookingStatus.WAITING, sort);
            case REJECTED -> bookingRepository.findAllByBookerAndStatus(booker, BookingStatus.REJECTED, sort);
            case CURRENT -> bookingRepository.findAllByBookerAndStartBeforeAndEndAfter(
                    booker,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    sort
            );
            case PAST -> bookingRepository.findAllByBookerAndEndBefore(booker, LocalDateTime.now(), sort);
            case FUTURE -> bookingRepository.findAllByBookerAndStartAfter(booker, LocalDateTime.now(), sort);
            default -> bookingRepository.findAllByBooker(booker, sort);
        };

        return list
                .stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public List<BookingDto> getByOwnerAndState(final Long ownerId, final BookingState state) {

        User owner = userRepository.findById(ownerId).orElseThrow(
                () -> new NotFoundException(USER_NOT_FOUND.formatted(ownerId))
        );

        List<Booking> list = switch (state) {
            case WAITING -> bookingRepository.findAllByItemOwnerAndStatus(owner, BookingStatus.WAITING, sort);
            case REJECTED -> bookingRepository.findAllByItemOwnerAndStatus(owner, BookingStatus.REJECTED, sort);
            case CURRENT -> bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfter(
                    owner,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    sort
            );
            case PAST -> bookingRepository.findAllByItemOwnerAndEndBefore(owner, LocalDateTime.now(), sort);
            case FUTURE -> bookingRepository.findAllByItemOwnerAndStartAfter(owner, LocalDateTime.now(), sort);
            default -> bookingRepository.findAllByItemOwner(owner, sort);
        };

        return list
                .stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

}
