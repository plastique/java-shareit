package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.contracts.BookingRepositoryInterface;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.EmptyIdException;
import ru.practicum.shareit.exception.InvalidOwnerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserDoesNotHaveBookedItem;
import ru.practicum.shareit.item.contracts.CommentRepositoryInterface;
import ru.practicum.shareit.item.contracts.ItemRepositoryInterface;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.contracts.ItemRequestRepositoryInterface;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.contracts.UserRepositoryInterface;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemServiceTest {

    @MockBean
    BookingRepositoryInterface bookingRepository;

    @Autowired
    ItemRepositoryInterface itemRepository;

    @Autowired
    UserRepositoryInterface userRepository;

    @Autowired
    CommentRepositoryInterface commentRepository;

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRequestRepositoryInterface itemRequestRepository;

    @Test
    void create() {
        User owner = createUser();
        ItemCreateDto itemCreateDto = makeItemCreateDto();

        ItemDto itemRes = itemService.create(itemCreateDto, owner.getId());

        Optional<Item> item = itemRepository.findById(itemRes.getId());

        Assertions.assertTrue(item.isPresent());
    }

    @Test
    void createWithRequest() {
        User owner = createUser();
        ItemRequest itemRequest = itemRequestRepository.save(new ItemRequest(
                null,
                "Request description",
                owner,
                LocalDateTime.now()
        ));

        ItemCreateDto itemCreateDto = makeItemCreateDto();
        itemCreateDto.setRequestId(itemRequest.getId());

        ItemDto itemRes = itemService.create(itemCreateDto, owner.getId());

        Optional<Item> item = itemRepository.findById(itemRes.getId());

        Assertions.assertTrue(item.isPresent());
    }

    @Test
    void createWithWrongUserReturnThrow() {
        ItemCreateDto itemCreateDto = makeItemCreateDto();

        Assertions.assertThrowsExactly(
                NotFoundException.class,
                () -> itemService.create(itemCreateDto, new Random().nextLong())
        );
    }

    @Test
    void createWithWrongRequestReturnThrow() {
        User owner = createUser();
        ItemCreateDto itemCreateDto = makeItemCreateDto();
        itemCreateDto.setRequestId(new Random().nextLong());

        Assertions.assertThrowsExactly(
                NotFoundException.class,
                () -> itemService.create(itemCreateDto, owner.getId())
        );
    }

    @Test
    void update() {
        User owner = createUser();
        ItemCreateDto itemCreateDto = makeItemCreateDto();

        ItemDto itemRes = itemService.create(itemCreateDto, owner.getId());
        String itemNewName = "item new name";

        itemService.update(
                new ItemUpdateDto(
                        itemRes.getId(),
                        itemNewName,
                        itemRes.getDescription(),
                        itemRes.getAvailable()
                ),
                owner.getId()
        );

        Optional<Item> item = itemRepository.findById(itemRes.getId());

        Assertions.assertTrue(item.isPresent());
        Assertions.assertEquals(itemNewName, item.get().getName());
    }

    @Test
    void updateWithWrongIdReturnThrows() {
        User owner = createUser();
        ItemCreateDto itemCreateDto = makeItemCreateDto();

        Assertions.assertThrowsExactly(
                NotFoundException.class,
                () -> itemService.update(
                        new ItemUpdateDto(
                                new Random().nextLong(),
                                itemCreateDto.getName(),
                                itemCreateDto.getDescription(),
                                itemCreateDto.getAvailable()
                        ),
                        owner.getId()
                )
        );
    }

    @Test
    void updateWithWrongOwnerReturnThrow() {
        User owner = createUser();
        ItemCreateDto itemCreateDto = makeItemCreateDto();

        ItemDto itemRes = itemService.create(itemCreateDto, owner.getId());

        User wrongOwner = createUser();

        Assertions.assertThrowsExactly(
                InvalidOwnerException.class,
                () -> itemService.update(
                        new ItemUpdateDto(
                                itemRes.getId(),
                                itemRes.getName(),
                                itemRes.getDescription(),
                                itemRes.getAvailable()
                        ),
                        wrongOwner.getId()
                )
        );
    }

    @Test
    void updateWithWrongUserReturnThrow() {
        User owner = createUser();
        ItemCreateDto itemCreateDto = makeItemCreateDto();

        ItemDto itemRes = itemService.create(itemCreateDto, owner.getId());

        Assertions.assertThrowsExactly(
                NotFoundException.class,
                () -> itemService.update(
                        new ItemUpdateDto(
                                itemRes.getId(),
                                itemRes.getName(),
                                itemRes.getDescription(),
                                itemRes.getAvailable()
                        ),
                        new Random().nextLong()
                )
        );
    }

    @Test
    void updateWithInvalidItemIdReturnThrow() {
        User owner = createUser();
        ItemCreateDto itemCreateDto = makeItemCreateDto();

        ItemDto itemRes = itemService.create(itemCreateDto, owner.getId());

        Assertions.assertThrowsExactly(
                EmptyIdException.class,
                () -> itemService.update(
                        new ItemUpdateDto(
                                null,
                                itemRes.getName(),
                                itemRes.getDescription(),
                                itemRes.getAvailable()
                        ),
                        new Random().nextLong()
                )
        );
    }

    @Test
    void findItemById() {
        User owner = createUser();
        ItemCreateDto itemCreateDto = makeItemCreateDto();
        ItemDto itemRes = itemService.create(itemCreateDto, owner.getId());

        User user = createUser();
        ItemInfoDto item = itemService.findItemById(itemRes.getId(), user.getId());

        Assertions.assertNotNull(item);
        Assertions.assertEquals(item.getId(), itemRes.getId());
    }

    @Test
    void findItemByIdWithWrongIdReturnThrow() {
        Random random = new Random();

        Assertions.assertThrowsExactly(
                NotFoundException.class,
                () -> itemService.findItemById(random.nextLong(), random.nextLong())
        );
    }

    @Test
    void findItemByIdByOwner() {
        User owner = createUser();
        ItemCreateDto itemCreateDto = makeItemCreateDto();
        ItemDto itemRes = itemService.create(itemCreateDto, owner.getId());

        ItemInfoDto item = itemService.findItemById(itemRes.getId(), owner.getId());

        Assertions.assertNotNull(item);
        Assertions.assertEquals(item.getId(), itemRes.getId());
    }

    @Test
    void findItemsByOwner() {
        User owner = createUser();
        int ownerItemsCount = new Random().nextInt(2, 10);
        ItemDto itemDto = null;

        for (int i = 1; i <= ownerItemsCount; i++) {
            ItemCreateDto itemCreateDto = makeItemCreateDto();
            itemDto = itemService.create(itemCreateDto, owner.getId());
        }

        User user = createUser();
        int userItemsCount = new Random().nextInt(2,10);

        for (int i = 1; i <= userItemsCount; i++) {
            ItemCreateDto itemCreateDto = makeItemCreateDto();
            itemService.create(itemCreateDto, user.getId());
        }

        Random random = new Random();
        Item item = new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                owner,
                null
        );

        Mockito.when(bookingRepository.findAllByItem_IdInAndStatus(
                Mockito.anyList(),
                Mockito.any(),
                Mockito.any()
        )).thenReturn(
                List.of(
                        new Booking(
                                random.nextLong(),
                                item,
                                user,
                                LocalDateTime.now().minusDays(4),
                                LocalDateTime.now().minusDays(3),
                                BookingStatus.APPROVED
                        ),
                        new Booking(
                                random.nextLong(),
                                item,
                                user,
                                LocalDateTime.now().plusDays(1),
                                LocalDateTime.now().plusDays(2),
                                BookingStatus.APPROVED
                        )
                )
        );

        commentRepository.save(new Comment(
                null,
                "comment text",
                item,
                user,
                LocalDateTime.now()
        ));

        List<ItemInfoDto> items = itemService.findItemsByOwner(owner.getId());

        Assertions.assertEquals(ownerItemsCount, items.size());
    }

    @Test
    void findItemsByOwnerWithWrongIdReturnThrow() {
        Assertions.assertThrowsExactly(
                NotFoundException.class,
                () -> itemService.findItemsByOwner(new Random().nextLong())
        );
    }

    @Test
    void findItemsByText() {
        String searchText = "text for search test";

        ItemCreateDto itemCreateDto = makeItemCreateDto();
        itemCreateDto.setDescription(searchText);
        User owner = createUser();

        itemService.create(itemCreateDto, owner.getId());

        List<ItemDto> res = itemService.findItemsByText(searchText);

        Assertions.assertFalse(res.isEmpty());
        Assertions.assertEquals(searchText, res.getFirst().getDescription());

    }

    @Test
    void findItemsByTextWithEmptyString() {

        ItemCreateDto itemCreateDto = makeItemCreateDto();
        User owner = createUser();

        itemService.create(itemCreateDto, owner.getId());

        List<ItemDto> res = itemService.findItemsByText("");

        Assertions.assertTrue(res.isEmpty());
    }

    @Test
    void addComment() {
        User owner = createUser();
        ItemCreateDto itemCreateDto = makeItemCreateDto();
        ItemDto itemRes = itemService.create(itemCreateDto, owner.getId());

        User user = createUser();
        String commentText = "test comment";

        Mockito.when(
                bookingRepository.existsByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(), Mockito.any())
        ).thenReturn(true);

        itemService.addComment(
                itemRes.getId(),
                user.getId(),
                new CommentCreateDto(commentText)
        );

        List<Comment> comments = commentRepository.findAllByItem_Id(itemRes.getId(), Sort.by(Sort.Direction.DESC, "created"));

        Assertions.assertFalse(comments.isEmpty());
        Assertions.assertEquals(commentText, comments.getFirst().getText());
    }

    @Test
    void addCommentWithWrongIdReturnThrow() {

        Assertions.assertThrowsExactly(
                NotFoundException.class,
                () -> itemService.addComment(
                        new Random().nextLong(),
                        null,
                        new CommentCreateDto("text")
                )
        );
    }

    @Test
    void addCommentWithWrongUserReturnThrow() {
        User owner = createUser();
        ItemCreateDto itemCreateDto = makeItemCreateDto();
        ItemDto itemRes = itemService.create(itemCreateDto, owner.getId());

        Assertions.assertThrowsExactly(
                NotFoundException.class,
                () -> itemService.addComment(
                        itemRes.getId(),
                        new Random().nextLong(),
                        new CommentCreateDto("text")
                )
        );
    }

    @Test
    void addCommentWithInvalidBookerIdReturnThrow() {
        User owner = createUser();
        ItemCreateDto itemCreateDto = makeItemCreateDto();
        ItemDto itemRes = itemService.create(itemCreateDto, owner.getId());

        Mockito.when(
                bookingRepository.existsByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(
                        Mockito.anyLong(), Mockito.anyLong(), Mockito.any(), Mockito.any()
                )
        ).thenReturn(false);

        User user = createUser();

        Assertions.assertThrowsExactly(
                UserDoesNotHaveBookedItem.class,
                () -> itemService.addComment(
                        itemRes.getId(),
                       user.getId(),
                        new CommentCreateDto("comment text")
                )
        );
    }

    private User createUser() {
        Random random = new Random();

        return userRepository.save(new User(
                null,
                "user name #" + random.nextInt(),
                "user" + random.nextInt() + "@yandex.net"
        ));
    }

    private ItemCreateDto makeItemCreateDto() {
        return new ItemCreateDto(
                "Item name",
                "item description",
                true,
                null
        );
    }

}