package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.contracts.UserServiceInterface;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {

    private final UserServiceInterface userService;

    @PostMapping
    public UserDto create(final @RequestBody UserCreateDto dto) {
        return userService.create(dto);
    }

    @PatchMapping("/{userId}")
    public UserDto update(
            final @RequestBody UserUpdateDto dto,
            final @PathVariable Long userId
    ) {
        dto.setId(userId);

        return userService.update(dto);
    }

    @DeleteMapping("/{userId}")
    public UserDto delete(final @PathVariable Long userId) {
        userService.deleteById(userId);

        return null;
    }

    @GetMapping("/{userId}")
    public UserDto findById(final @PathVariable Long userId) {
        return userService.findById(userId);
    }

}
