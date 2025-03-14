package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Collections;
import java.util.List;

public class ItemMapper {

    private ItemMapper() {
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public static ItemInfoDto toItemCardDto(Item item, List<Comment> comments, Booking lastBooking, Booking nextBooking) {
        return new ItemInfoDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                comments == null
                        ? Collections.emptyList()
                        : comments.stream()
                                  .map(comment -> new ItemInfoDto.CommentDto(
                                          comment.getId(),
                                          comment.getAuthor().getName(),
                                          comment.getText()
                                  )).toList(),
                lastBooking != null
                        ? new ItemInfoDto.BookingDto(lastBooking.getId(), lastBooking.getStart(), lastBooking.getEnd())
                        : null,
                nextBooking != null
                        ? new ItemInfoDto.BookingDto(nextBooking.getId(), nextBooking.getStart(), nextBooking.getEnd())
                        : null
        );
    }

}
