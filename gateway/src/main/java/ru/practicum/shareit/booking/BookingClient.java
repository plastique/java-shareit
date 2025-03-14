package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;

@Slf4j
@Service
public class BookingClient extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(
            @Value("${shareit-server.url}") String serverUrl,
            RestTemplateBuilder builder
    ) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getBookings(
            long userId,
            BookingState state
    ) {
        log.info("Get bookings by user: {}, state: {}", userId, state);
        return get("?state=" + state, userId);
    }

    public ResponseEntity<Object> bookItem(long userId, BookItemRequestDto requestDto) {
        log.info("Booking by user {} and request: {}", userId, requestDto);
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> approve(Long bookingId, Long ownerId, boolean approved) {
        log.info("Approve booking by bookingId: {}, ownerId: {}", bookingId, ownerId);
        return patch("/" + bookingId + "?approved=" + approved, ownerId, null, null);
    }

    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        log.info("Get booking by user {} and bookingId: {}", userId, bookingId);
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getByOwnerAndState(Long ownerId, BookingState state) {
        log.info("Get booking by owner and state: {}", state);
        return get("/owner?state=" + state, ownerId);
    }

}
