package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private static final String LOG_GET_LIST_BY_PARAMS = "Get booking with state {}, userId={}, from={}, size={}";

    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookings(
            @RequestHeader(HEADER_USER_ID) long userId,
            @RequestParam(name = "state", defaultValue = "all") String stateParam,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        BookingState state = getBookingState(stateParam);

        log.info(LOG_GET_LIST_BY_PARAMS, stateParam, userId, from, size);
        return bookingClient.getBookings(userId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> bookItem(
            @RequestHeader(HEADER_USER_ID) long userId,
            @RequestBody @Valid BookItemRequestDto requestDto
    ) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(
            @PathVariable Long bookingId,
            @RequestParam boolean approved,
            @RequestHeader(name = HEADER_USER_ID) Long ownerId
    ) {
        return bookingClient.approve(bookingId, ownerId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(
            @RequestHeader(HEADER_USER_ID) long userId,
            @PathVariable Long bookingId
    ) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getByOwnerAndState(
            @RequestParam(required = false, defaultValue = "all") String stateParam,
            @RequestHeader(name = HEADER_USER_ID) Long ownerId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        BookingState state = getBookingState(stateParam);

        log.info(LOG_GET_LIST_BY_PARAMS, stateParam, ownerId, from, size);
        return bookingClient.getByOwnerAndState(ownerId, state, from, size);
    }

    private static BookingState getBookingState(String stateParam) {
       return BookingState.from(stateParam).orElseThrow(
                () -> new IllegalArgumentException("Unknown state: " + stateParam)
        );
    }

}
