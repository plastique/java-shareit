package ru.practicum.shareit.user.contracts;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

public interface UserRepositoryInterface extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
