package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.contracts.UserRepositoryInterface;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository implements UserRepositoryInterface {

    private final Map<Integer, User> users = new HashMap<>();
    private Integer increment = 0;

    @Override
    public User create(final User user) {
        User newUser = new User(
                getNextId(),
                user.getName(),
                user.getEmail()
        );

        users.put(newUser.getId(), newUser);

        return newUser;
    }

    @Override
    public User update(final User user) {

        User currentUser = users.get(user.getId());

        if (currentUser == null) {
            throw new NotFoundException("User not found");
        }

        currentUser.setName(user.getName());
        currentUser.setEmail(user.getEmail());

        users.put(currentUser.getId(), currentUser);

        return user;
    }

    @Override
    public void delete(final Integer id) {
        users.remove(id);
    }

    @Override
    public User findById(final Integer id) {
        return users.get(id);
    }

    private Integer getNextId() {
        return ++increment;
    }

    @Override
    public boolean emailExists(String email) {
        if (email == null) {
            return false;
        }
        return users.values().stream().anyMatch(u -> u.getEmail().equals(email));
    }

    @Override
    public boolean userExists(Integer id) {
        return id != null && users.containsKey(id);
    }

}
