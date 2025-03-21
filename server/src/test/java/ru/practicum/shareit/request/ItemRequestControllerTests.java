package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTests {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void create() throws Exception {
        ItemRequestResponseDto itemRequestResponseDto = makeItemRequestDto();
        User user = makeUser();

        when(itemRequestService.create(Mockito.any(ItemRequestCreateDto.class)))
                .thenReturn(itemRequestResponseDto);

        mockMvc.perform(
                       post("/requests")
                               .content(mapper.writeValueAsString(new ItemRequestCreateDto(
                                       itemRequestResponseDto.getDescription(),
                                       user.getId()
                               )))
                               .header("X-Sharer-User-Id", user.getId().toString())
                               .characterEncoding(StandardCharsets.UTF_8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.description").value(itemRequestResponseDto.getDescription()));
    }

    @Test
    void getListByUser() throws Exception {
        ItemRequestResponseDto itemRequestResponseDto = makeItemRequestDto();
        User user = makeUser();

        when(itemRequestService.getListByUser(Mockito.anyLong()))
                .thenReturn(List.of(itemRequestResponseDto));

        mockMvc.perform(
                       get("/requests")
                               .header("X-Sharer-User-Id", user.getId().toString())
                               .characterEncoding(StandardCharsets.UTF_8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].description").value(itemRequestResponseDto.getDescription()));
    }

    @Test
    void getList() throws Exception {
        ItemRequestResponseDto itemRequestResponseDto = makeItemRequestDto();
        User user = makeUser();

        when(itemRequestService.getList(Mockito.anyLong()))
                .thenReturn(List.of(itemRequestResponseDto));

        mockMvc.perform(
                       get("/requests/all")
                               .header("X-Sharer-User-Id", user.getId().toString())
                               .characterEncoding(StandardCharsets.UTF_8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].description").value(itemRequestResponseDto.getDescription()));
    }

    @Test
    void getById() throws Exception {
        ItemRequestResponseDto itemRequestResponseDto = makeItemRequestDto();
        User user = makeUser();

        when(itemRequestService.getById(Mockito.anyLong()))
                .thenReturn(itemRequestResponseDto);

        mockMvc.perform(
                       get("/requests/{id}", itemRequestResponseDto.getId())
                               .header("X-Sharer-User-Id", user.getId().toString())
                               .characterEncoding(StandardCharsets.UTF_8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.description").value(itemRequestResponseDto.getDescription()));
    }

    private ItemRequestResponseDto makeItemRequestDto() {
        Long id = new Random().nextLong();

        return new ItemRequestResponseDto(
                id,
                "description #" + id,
                LocalDateTime.now(),
                Collections.emptyList()
        );
    }

    private User makeUser() {
        Long id = new Random().nextLong();

        return new User(
                id,
                "user name #" + id,
                "user-" + id + "@yandex.net"
        );
    }

}
