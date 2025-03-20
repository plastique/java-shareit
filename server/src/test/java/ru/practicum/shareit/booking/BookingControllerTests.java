package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTests {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createBooking() throws Exception {
        BookingDto bookingDto = makeBookingDto();

        when(bookingService.create(Mockito.any(BookingCreateDto.class)))
                .thenReturn(bookingDto);

        mockMvc.perform(
                       post("/bookings")
                               .content(mapper.writeValueAsString(new BookingCreateDto(
                                       bookingDto.getStart(),
                                       bookingDto.getEnd(),
                                       bookingDto.getBooker().id(),
                                       bookingDto.getItem().id()
                               )))
                               .header("X-Sharer-User-Id", "1")
                               .characterEncoding(StandardCharsets.UTF_8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.item.name").value(bookingDto.getItem().name()));
    }

    @Test
    void approveBooking() throws Exception {
        BookingDto bookingDto = makeBookingDto();

        when(bookingService.approve(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(bookingDto);

        mockMvc.perform(
                       patch("/bookings/{id}?approved={approve}", bookingDto.getId(), true)
                               .header("X-Sharer-User-Id", "1")
                               .characterEncoding(StandardCharsets.UTF_8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.item.name").value(bookingDto.getItem().name()));
    }

    @Test
    void getById() throws Exception {
        BookingDto bookingDto = makeBookingDto();

        when(bookingService.getById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(bookingDto);

        mockMvc.perform(
                       get("/bookings/{id}", bookingDto.getId())
                               .header("X-Sharer-User-Id", "1")
                               .characterEncoding(StandardCharsets.UTF_8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.item.name").value(bookingDto.getItem().name()));
    }

    @Test
    void getByBookerAndState()  throws Exception {
        BookingDto bookingDto = makeBookingDto();

        when(bookingService.getByBookerAndState(Mockito.anyLong(), Mockito.any()))
                .thenReturn(List.of(bookingDto));

        mockMvc.perform(
                       get("/bookings")
                               .header("X-Sharer-User-Id", "1")
                               .characterEncoding(StandardCharsets.UTF_8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].item.name").value(bookingDto.getItem().name()));
    }

    @Test
    void getByOwnerAndState()  throws Exception {
        BookingDto bookingDto = makeBookingDto();

        when(bookingService.getByOwnerAndState(Mockito.anyLong(), Mockito.any()))
                .thenReturn(List.of(bookingDto));

        mockMvc.perform(
                       get("/bookings/owner")
                               .header("X-Sharer-User-Id", "1")
                               .characterEncoding(StandardCharsets.UTF_8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].item.name").value(bookingDto.getItem().name()));
    }

    private BookingDto makeBookingDto() {
        return new BookingDto(
                new Random().nextLong(),
                new BookingDto.ItemDto(null, "item name"),
                new BookingDto.BookerDto(null, "user name"),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2L),
                BookingStatus.APPROVED
        );
    }

}
