package ru.practicum.shareit.user.contracts;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

public interface UserServiceInterface {

    UserDto create(UserCreateDto dto);

    UserDto update(UserUpdateDto dto);

    UserDto findById(Long id);

    void deleteById(Long id);

}
