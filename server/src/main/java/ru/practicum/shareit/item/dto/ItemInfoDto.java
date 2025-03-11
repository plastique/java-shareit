package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ItemInfoDto {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private List<CommentDto> comments;

    private BookingDto lastBooking;

    private BookingDto nextBooking;

    public record CommentDto(Long id, String authorName, String text) {
    }

    public record BookingDto(Long id, LocalDateTime start, LocalDateTime end) {
    }

}
