package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTests {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createUser() throws Exception {
        UserDto userDto = makeUser();

        when(userService.create(Mockito.any(UserCreateDto.class)))
                .thenReturn(userDto);

        mockMvc.perform(
                       post("/users")
                               .content(mapper.writeValueAsString(new UserCreateDto(
                                       userDto.getName(),
                                       userDto.getEmail()
                               )))
                               .characterEncoding(StandardCharsets.UTF_8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value(userDto.getName()))
               .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    void updateUser() throws Exception {
        UserDto userDto = makeUser();

        when(userService.update(Mockito.any(UserUpdateDto.class)))
                .thenReturn(userDto);

        mockMvc.perform(
                       patch("/users/{id}", userDto.getId())
                               .content(mapper.writeValueAsString(new UserUpdateDto(
                                       userDto.getId(),
                                       userDto.getName(),
                                       userDto.getEmail()
                               )))
                               .characterEncoding(StandardCharsets.UTF_8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value(userDto.getName()))
               .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    void getUser() throws Exception {
        UserDto userDto = makeUser();

        when(userService.findById(Mockito.anyLong()))
                .thenReturn(userDto);

        mockMvc.perform(
                       get("/users/{id}", userDto.getId())
                               .characterEncoding(StandardCharsets.UTF_8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value(userDto.getName()))
               .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    void deleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteById(Mockito.anyLong());

        mockMvc.perform(
                       delete("/users/{id}", new Random().nextLong())
                               .characterEncoding(StandardCharsets.UTF_8)
                               .contentType(MediaType.APPLICATION_JSON)
                               .accept(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk());
    }

    private UserDto makeUser() {
        Long id = new Random().nextLong();

        return new UserDto(
                id,
                "username #" + id,
                "user-" + id + "@yandex.net"
        );
    }

}
