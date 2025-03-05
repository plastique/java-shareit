package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {

    private BookingMapper() {
    }

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId(),
                booking.getBooker().getId(),
                booking.getStatus()
        );
    }

    public static Booking toBooking(BookingDto dto) {
        Item item = ItemMapper.toItem(
                ItemDto.builder().id(dto.getId()).build()
        );
        User booker = UserMapper.toUser(
                UserDto.builder().id(dto.getBookerId()).build()
        );

        return new Booking(
                null,
                dto.getStart(),
                dto.getEnd(),
                item,
                booker,
                null
        );
    }

}
