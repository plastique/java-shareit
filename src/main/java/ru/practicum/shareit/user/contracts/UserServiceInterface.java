package ru.practicum.shareit.user.contracts;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserServiceInterface {

    UserDto create(UserDto dto);

    UserDto update(UserDto dto);

    UserDto findById(Long id);

    void deleteById(Long id);

}
