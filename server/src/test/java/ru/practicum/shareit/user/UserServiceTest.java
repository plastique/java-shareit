package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.EmptyIdException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotUniqueEmailException;
import ru.practicum.shareit.user.contracts.UserRepositoryInterface;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;
import java.util.Random;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceTest {

    @Autowired
    UserRepositoryInterface userRepository;

    @Autowired
    UserService userService;

    @Test
    void create() {
        UserCreateDto userCreateDto = makeUserCreateDto();

        UserDto userDto = userService.create(userCreateDto);

        Optional<User> user = userRepository.findById(userDto.getId());

        Assertions.assertTrue(user.isPresent());
    }

    @Test
    void createWithExistingEmailReturnThrow() {
        UserCreateDto userCreateDto = makeUserCreateDto();

        userService.create(userCreateDto);

        Assertions.assertThrowsExactly(
                NotUniqueEmailException.class,
                () -> userService.create(userCreateDto)
        );
    }

    @Test
    void update() {
        UserCreateDto userCreateDto = makeUserCreateDto();

        UserDto userDto = userService.create(userCreateDto);

        String userNewName = "user new name";

        userService.update(
                new UserUpdateDto(
                        userDto.getId(),
                        userNewName,
                        userDto.getEmail()
                )
        );

        Optional<User> user = userRepository.findById(userDto.getId());

        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals(userNewName, user.get().getName());
    }

    @Test
    void updateWithWrongUserIdReturnThrow() {
        UserCreateDto userCreateDto = makeUserCreateDto();

        Assertions.assertThrowsExactly(
                NotFoundException.class,
                () -> userService.update(
                        new UserUpdateDto(
                                new Random().nextLong(),
                                userCreateDto.getName(),
                                userCreateDto.getEmail()
                        )
                )
        );
    }

    @Test
    void updateWithNonUniqueEmailReturnThrow() {

        UserDto userDto1 = userService.create(makeUserCreateDto());
        UserDto userDto2 = userService.create(makeUserCreateDto());

        Assertions.assertThrowsExactly(
                NotUniqueEmailException.class,
                () -> userService.update(
                        new UserUpdateDto(
                                userDto2.getId(),
                                userDto2.getName(),
                                userDto1.getEmail()
                        )
                )
        );
    }

    @Test
    void updateWithIdIsNullReturnThrow() {

        UserDto userDto = userService.create(makeUserCreateDto());

        Assertions.assertThrowsExactly(
                EmptyIdException.class,
                () -> userService.update(
                        new UserUpdateDto(
                                null,
                                userDto.getName(),
                                userDto.getEmail()
                        )
                )
        );
    }

    @Test
    void findItemById() {
        UserCreateDto userCreateDto = makeUserCreateDto();

        UserDto userDto = userService.create(userCreateDto);
        UserDto user = userService.findById(userDto.getId());

        Assertions.assertNotNull(user);
        Assertions.assertEquals(user.getId(), userDto.getId());
    }

    @Test
    void findItemByIdReturnThrow() {
        Assertions.assertThrowsExactly(
                NotFoundException.class,
                () -> userService.findById(new Random().nextLong())
        );
    }

    @Test
    void delete() {
        UserCreateDto userCreateDto = makeUserCreateDto();

        UserDto userDto = userService.create(userCreateDto);
        Optional<User> userBeforeDelete = userRepository.findById(userDto.getId());

        userService.deleteById(userDto.getId());

        Optional<User> userAfterDelete = userRepository.findById(userDto.getId());

        Assertions.assertTrue(userBeforeDelete.isPresent());
        Assertions.assertFalse(userAfterDelete.isPresent());
    }

    private UserCreateDto makeUserCreateDto() {
        long id = new Random().nextLong();

        return new UserCreateDto(
                "username #" + id,
                "user-" + id + "@yandex.net"
        );
    }

}