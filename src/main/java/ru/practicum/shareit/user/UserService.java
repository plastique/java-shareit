package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotUniqueEmailException;
import ru.practicum.shareit.exception.EmptyIdException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.contracts.UserRepositoryInterface;
import ru.practicum.shareit.user.contracts.UserServiceInterface;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

@Service
@AllArgsConstructor
public class UserService implements UserServiceInterface {

    private final UserRepositoryInterface userRepository;

    @Override
    public UserDto create(final UserCreateDto dto) {
        User user = UserMapper.toUser(dto);

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new NotUniqueEmailException("Email already exists");
        }

        User newUser = userRepository.save(user);

        return UserMapper.toUserDto(newUser);
    }

    @Override
    public UserDto update(final UserUpdateDto dto) {
        if (dto.getId() == null) {
            throw new EmptyIdException("id is empty");
        }

        User user = userRepository.findById(dto.getId()).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        if (dto.getName() != null && Strings.isNotBlank(dto.getName())) {
            user.setName(dto.getName());
        }

        if (dto.getEmail() == null) {
            return UserMapper.toUserDto(user);
        }

        if (
                !user.getEmail().equals(dto.getEmail())
                        && userRepository.existsByEmail(dto.getEmail())
        ) {
            throw new NotUniqueEmailException("Email already exists");
        }

        user.setEmail(dto.getEmail());

        user = userRepository.save(user);

        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findById(final Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteById(final Long id) {
        userRepository.deleteById(id);
    }

}
