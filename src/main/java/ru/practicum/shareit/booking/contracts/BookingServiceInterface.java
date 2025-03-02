package ru.practicum.shareit.booking.contracts;

import ru.practicum.shareit.booking.dto.BookingDto;

public interface BookingServiceInterface {

    BookingDto create(BookingDto bookingDto, Long userId);

    BookingDto update(BookingDto bookingDto, Long userId);

}
