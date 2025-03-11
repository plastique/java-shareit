package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDto {

    private Long id;

    private ItemDto item;

    private BookerDto booker;

    private LocalDateTime start;

    private LocalDateTime end;

    private BookingStatus status;

    public record ItemDto(Long id, String name) {
    }

    public record BookerDto(Long id, String name) {
    }

}
