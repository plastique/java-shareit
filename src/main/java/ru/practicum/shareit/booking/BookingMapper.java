package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {

    private BookingMapper() {
    }

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                new BookingDto.ItemDto(booking.getItem().getId(), booking.getItem().getName()),
                new BookingDto.BookerDto(booking.getBooker().getId(), booking.getBooker().getName()),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus()
        );
    }
}
