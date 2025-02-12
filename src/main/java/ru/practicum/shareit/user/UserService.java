package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.contracts.UserServiceInterface;

@Service
public class UserService implements UserServiceInterface {

    @Override
    public User create(User user) {
        return null;
    }

}
