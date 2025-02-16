package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotUniqueEmailException;
import ru.practicum.shareit.exception.EmptyIdException;
import ru.practicum.shareit.exception.InvalidEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.contracts.UserRepositoryInterface;
import ru.practicum.shareit.user.contracts.UserServiceInterface;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UserService implements UserServiceInterface {

    private final UserRepositoryInterface userRepository;

    @Override
    public UserDto create(final UserDto dto) {
        User user = UserMapper.toUser(dto);

        if (userRepository.emailExists(user.getEmail())) {
            throw new NotUniqueEmailException("Email already exists");
        }

        User newUser = userRepository.create(user);

        dto.setId(newUser.getId());

        return dto;
    }

    @Override
    public UserDto update(final UserDto dto) {
        if (dto.getId() == null) {
            throw new EmptyIdException("id is empty");
        }

        User user = userRepository.findById(dto.getId());

        if (user == null) {
            throw new NotFoundException("User not found");
        }

        if (dto.getName() != null && Strings.isNotBlank(dto.getName())) {
            user.setName(dto.getName());
        }

        if (dto.getEmail() == null) {
            return dto;
        }

        if (
                !user.getEmail().equals(dto.getEmail())
                        && userRepository.emailExists(dto.getEmail())
        ) {
            throw new NotUniqueEmailException("Email already exists");
        }

        if (!isValidateEmail(dto.getEmail())) {
            throw new InvalidEmailException("Email is incorrect");
        }

        userRepository.update(user);

        return dto;
    }

    @Override
    public UserDto findById(final Integer id) {
        User user = userRepository.findById(id);

        if (user == null) {
            throw new NotFoundException("user not found");
        }

        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteById(final Integer id) {
        userRepository.delete(id);
    }

    private boolean isValidateEmail(String email) {
        return email != null && Pattern
                .compile(
                        "^([a-z0-9._%+-]+)@([a-z0-9.-]+).([a-z]+)$",
                        Pattern.CASE_INSENSITIVE
                )
                .matcher(email)
                .matches();
    }

}
