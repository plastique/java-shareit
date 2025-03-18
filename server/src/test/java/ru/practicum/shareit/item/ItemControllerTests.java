package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTests {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createItem() throws Exception {
        ItemDto itemDto = new ItemDto(
                null,
                "item name",
                "item description",
                true
        );

        when(itemService.create(Mockito.any(), Mockito.anyLong()))
                .thenReturn(itemDto);

        mockMvc.perform(
                       post("/items")
                               .content(mapper.writeValueAsString(itemDto))
                               .header("X-Sharer-User-Id", "1")
                               .characterEncoding(StandardCharsets.UTF_8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value(itemDto.getName()))
               .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
               .andExpect(jsonPath("$.available").value(itemDto.getAvailable()));
    }

}
