package ru.practicum.shareit.booking.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDto {

    @Nullable
    private Long id;

    @NotNull
    @FutureOrPresent
    private LocalDateTime start;

    @NotNull
    @Future
    private LocalDateTime end;

    private Long itemId;

    private Long bookerId;

    @Nullable
    private BookingStatus bookingStatus;
}
