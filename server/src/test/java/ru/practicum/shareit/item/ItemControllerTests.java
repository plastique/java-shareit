package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

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
        ItemDto itemDto = makeItemDto();

        when(itemService.create(Mockito.any(ItemCreateDto.class), Mockito.anyLong()))
                .thenReturn(itemDto);

        mockMvc.perform(
                       post("/items")
                               .content(mapper.writeValueAsString(new ItemCreateDto(
                                       itemDto.getName(),
                                       itemDto.getDescription(),
                                       itemDto.getAvailable(),
                                       null
                               )))
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

    @Test
    void updateItem() throws Exception {
        ItemDto itemDto = makeItemDto();

        when(itemService.update(Mockito.any(ItemUpdateDto.class), Mockito.anyLong()))
                .thenReturn(itemDto);

        mockMvc.perform(
                       patch("/items/{id}", itemDto.getId())
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

    @Test
    void getItem() throws Exception {
        ItemInfoDto itemInfoDto = makeItemInfoDto();

        when(itemService.findItemById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemInfoDto);

        mockMvc.perform(
                       get("/items/{id}", itemInfoDto.getId())
                               .header("X-Sharer-User-Id", "1")
                               .characterEncoding(StandardCharsets.UTF_8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value(itemInfoDto.getName()))
               .andExpect(jsonPath("$.description").value(itemInfoDto.getDescription()))
               .andExpect(jsonPath("$.available").value(itemInfoDto.getAvailable()));
    }

    @Test
    void getItemsByOwner()  throws Exception {
        ItemInfoDto itemInfoDto = makeItemInfoDto();

        when(itemService.findItemsByOwner(Mockito.anyLong()))
                .thenReturn(List.of(itemInfoDto));

        mockMvc.perform(
                       get("/items")
                               .header("X-Sharer-User-Id", "1")
                               .characterEncoding(StandardCharsets.UTF_8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].name").value(itemInfoDto.getName()))
               .andExpect(jsonPath("$[0].description").value(itemInfoDto.getDescription()))
               .andExpect(jsonPath("$[0].available").value(itemInfoDto.getAvailable()));
    }

    @Test
    void search() throws Exception {
        ItemDto itemDto = makeItemDto();
        String text = "search text";

        when(itemService.findItemsByText(text))
                .thenReturn(List.of(itemDto));

        mockMvc.perform(
                       get("/items/search")
                               .header("X-Sharer-User-Id", "1")
                               .param("text", text)
                               .characterEncoding(StandardCharsets.UTF_8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].name").value(itemDto.getName()))
               .andExpect(jsonPath("$[0].description").value(itemDto.getDescription()))
               .andExpect(jsonPath("$[0].available").value(itemDto.getAvailable()));
    }

    @Test
    void addComment() throws Exception {
        ItemDto itemDto = makeItemDto();
        CommentDto commentDto = new CommentDto(
                new Random().nextLong(),
                "comment text",
                "author name",
                LocalDateTime.now()
        );

        when(itemService.addComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(CommentCreateDto.class)))
                .thenReturn(commentDto);

        mockMvc.perform(
                       post("/items/{id}/comment", itemDto.getId())
                               .content(mapper.writeValueAsString(new CommentCreateDto(
                                       commentDto.getText()
                               )))
                               .header("X-Sharer-User-Id", "1")
                               .characterEncoding(StandardCharsets.UTF_8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.text").value(commentDto.getText()))
               .andExpect(jsonPath("$.authorName").value(commentDto.getAuthorName()));
    }

    private ItemDto makeItemDto() {
        Long id = new Random().nextLong();

        return new ItemDto(
                id,
                "item name #" + id,
                "item description #" + id,
                true
        );
    }

    private ItemInfoDto makeItemInfoDto() {
        Long id = new Random().nextLong();

        return new ItemInfoDto(
                id,
                "item name #" + id,
                "item description #" + id,
                true,
                null,
                null,
                null
        );
    }

}
