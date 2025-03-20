package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingDtoTests {

    private final JacksonTester<BookingDto> json;

    @Test
    void bookingDto() throws Exception {
        BookingDto bookingDto = new BookingDto(
                null,
                new BookingDto.ItemDto(null, "item name"),
                new BookingDto.BookerDto(null, "user name"),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2L),
                BookingStatus.APPROVED
        );

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo(bookingDto.getItem().name());
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo(bookingDto.getBooker().name());
    }

}
