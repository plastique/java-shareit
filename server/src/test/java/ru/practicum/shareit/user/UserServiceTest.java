package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
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
    void findItemById() {
        UserCreateDto userCreateDto = makeUserCreateDto();

        UserDto userDto = userService.create(userCreateDto);
        UserDto user = userService.findById(userDto.getId());

        Assertions.assertNotNull(user);
        Assertions.assertEquals(user.getId(), userDto.getId());
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