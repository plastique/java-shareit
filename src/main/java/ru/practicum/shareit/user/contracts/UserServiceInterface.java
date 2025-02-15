package ru.practicum.shareit.user.contracts;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserServiceInterface {

    UserDto create(UserDto dto);

    UserDto update(UserDto dto);

    UserDto findById(Integer id);

    void deleteById(Integer id);

}
