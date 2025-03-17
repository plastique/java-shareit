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
import ru.practicum.shareit.item.contracts.CommentRepositoryInterface;
import ru.practicum.shareit.item.contracts.ItemRepositoryInterface;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.contracts.UserRepositoryInterface;
import ru.practicum.shareit.user.model.User;

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

    @Test
    void create() {
        User owner = createUser();
        ItemCreateDto itemCreateDto = makeItemCreateDto();

        ItemDto itemRes = itemService.create(itemCreateDto, owner.getId());

        Optional<Item> item = itemRepository.findById(itemRes.getId());

        Assertions.assertTrue(item.isPresent());
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
    void findItemsByOwner() {
        User owner = createUser();
        int ownerItemsCount = new Random().nextInt(10);

        for (int i = 1; i <= ownerItemsCount; i++) {
            ItemCreateDto itemCreateDto = makeItemCreateDto();
            itemService.create(itemCreateDto, owner.getId());
        }

        User user = createUser();
        int userItemsCount = new Random().nextInt(10);

        for (int i = 0; i <= userItemsCount; i++) {
            ItemCreateDto itemCreateDto = makeItemCreateDto();
            itemService.create(itemCreateDto, user.getId());
        }

        List<ItemInfoDto> items = itemService.findItemsByOwner(owner.getId());

        Assertions.assertEquals(ownerItemsCount, items.size());
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