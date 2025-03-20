package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.contracts.ItemRepositoryInterface;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.contracts.ItemRequestRepositoryInterface;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.contracts.UserRepositoryInterface;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestServiceTest {

    @Autowired
    ItemRequestRepositoryInterface itemRequestRepository;

    @MockBean
    ItemRepositoryInterface itemRepository;

    @Autowired
    UserRepositoryInterface userRepository;

    @Autowired
    ItemRequestService itemRequestService;

    @Test
    void create() {
        User user = createUser();
        ItemRequestCreateDto itemRequestCreateDto = makeItemRequestCreateDto(user);

        ItemRequestResponseDto itemRequestResponseDto = itemRequestService.create(itemRequestCreateDto);

        Optional<ItemRequest> itemRequest = itemRequestRepository.findById(itemRequestResponseDto.getId());

        Assertions.assertTrue(itemRequest.isPresent());
    }

    @Test
    void createWithInvalidUserReturnThrow() {
        Assertions.assertThrowsExactly(
                NotFoundException.class,
                () -> itemRequestService.create(makeItemRequestCreateDto(
                        new User(new Random().nextLong(), "username", "email")
                ))
        );
    }

    @Test
    void getById() {
        User user = createUser();
        ItemRequestCreateDto itemRequestCreateDto = makeItemRequestCreateDto(user);

        ItemRequestResponseDto itemRequestResponseDto = itemRequestService.create(itemRequestCreateDto);

        Mockito.when(
                itemRepository.findAllByRequest_IdOrderByIdDesc(Mockito.anyLong())
        ).thenReturn(
                List.of(
                        new Item(
                                null,
                                "item name",
                                "item description",
                                true,
                                user,
                                null
                        )
                )
        );

        ItemRequestResponseDto itemRequest = itemRequestService.getById(itemRequestResponseDto.getId());

        Assertions.assertEquals(itemRequestResponseDto.getId(), itemRequest.getId());
        Assertions.assertFalse(itemRequest.getItems().isEmpty());
    }

    @Test
    void getByIdWithInvalidIdReturnThrow() {
        Assertions.assertThrows(
                NotFoundException.class, () -> itemRequestService.getById(new Random().nextLong())
        );
    }

    @Test
    void getListByUser() {
        User user = createUser();
        int userRequestsCount = new Random().nextInt(10);

        for (int i = 1; i <= userRequestsCount; i++) {
            ItemRequestCreateDto itemRequestCreateDto = makeItemRequestCreateDto(user);
            itemRequestService.create(itemRequestCreateDto);
        }

        List<ItemRequestResponseDto> itemRequests = itemRequestService.getListByUser(user.getId());

        Assertions.assertEquals(userRequestsCount, itemRequests.size());
    }

    @Test
    void getListByUserWithInvalidUserReturnThrow() {
        Assertions.assertThrowsExactly(
                NotFoundException.class,
                () -> itemRequestService.getListByUser(new Random().nextLong())
        );
    }

    @Test
    void getList() {
        List<User> users = new ArrayList<>();
        int cnt = 0;

        for (int j = 0; j < 2; j++) {
            User user = createUser();
            cnt = new Random().nextInt(2, 5);
            users.add(user);

            for (int i = 1; i <= cnt; i++) {
                ItemRequestCreateDto itemRequestCreateDto = makeItemRequestCreateDto(user);
                itemRequestService.create(itemRequestCreateDto);
            }
        }

        List<ItemRequestResponseDto> itemRequests = itemRequestService.getList(users.getLast().getId());

        Assertions.assertEquals(cnt, itemRequests.size());
    }

    @Test
    void getListWithAllUsers() {
        int total = 0;

        for (int j = 0; j < 2; j++) {
            User user = createUser();
            int cnt = new Random().nextInt(2, 5);
            total += cnt;

            for (int i = 1; i <= cnt; i++) {
                ItemRequestCreateDto itemRequestCreateDto = makeItemRequestCreateDto(user);
                itemRequestService.create(itemRequestCreateDto);
            }
        }

        List<ItemRequestResponseDto> itemRequests = itemRequestService.getList(null);

        Assertions.assertEquals(total, itemRequests.size());
    }

    private User createUser() {
        long id = new Random().nextLong();

        return userRepository.save(new User(
                null,
                "user name #" + id,
                "user" + id + "@yandex.net"
        ));
    }

    private ItemRequestCreateDto makeItemRequestCreateDto(User user) {
        return new ItemRequestCreateDto(
                "description",
                user.getId()
        );
    }

}