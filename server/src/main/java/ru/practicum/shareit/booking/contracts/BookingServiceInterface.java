package ru.practicum.shareit.booking.contracts;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingServiceInterface {

    BookingDto create(BookingCreateDto bookingDto);

    BookingDto approve(Long id, Long ownerId, boolean approved);

    BookingDto getById(Long id, Long userId);

    List<BookingDto> getByBookerAndState(Long bookerId, BookingState state, Integer from, Integer size);

    List<BookingDto> getByOwnerAndState(Long ownerId, BookingState state, Integer from, Integer size);

}
