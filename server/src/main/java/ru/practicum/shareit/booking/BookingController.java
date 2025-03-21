package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.contracts.BookingServiceInterface;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    public static final String HEADER_USER_ID = "X-Sharer-User-Id";

    private final BookingServiceInterface bookingService;

    @PostMapping
    public BookingDto create(
            final @RequestBody BookingCreateDto bookingDto,
            final @RequestHeader(name = HEADER_USER_ID) Long bookerId
    ) {
        bookingDto.setBookerId(bookerId);

        return bookingService.create(bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(
        final @PathVariable Long bookingId,
        final @RequestParam boolean approved,
        final @RequestHeader(name = HEADER_USER_ID) Long ownerId
    ) {
        return bookingService.approve(bookingId, ownerId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(
            final @PathVariable Long bookingId,
            final @RequestHeader(name = HEADER_USER_ID) Long userId
    ) {
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getByBookerAndState(
            final @RequestParam(required = false) BookingState state,
            final @RequestHeader(name = HEADER_USER_ID) Long bookerId
    ) {
        return bookingService.getByBookerAndState(
                bookerId,
                state == null ? BookingState.ALL : state
        );
    }

    @GetMapping("/owner")
    public List<BookingDto> getByOwnerAndState(
            final @RequestParam(required = false) BookingState state,
            final @RequestHeader(name = HEADER_USER_ID) Long ownerId
    ) {
        return bookingService.getByOwnerAndState(ownerId, state);
    }

}
