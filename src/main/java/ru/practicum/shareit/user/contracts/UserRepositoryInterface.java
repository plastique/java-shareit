package ru.practicum.shareit.user.contracts;

import ru.practicum.shareit.user.User;

public interface UserRepositoryInterface {

    User create(User user);

    User update(User user);

    void delete(Integer id);

    User findById(Integer id);

    boolean emailExists(String email);

    boolean userExists(Integer id);

}
