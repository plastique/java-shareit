package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.contracts.BookingRepositoryInterface;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.InvalidBookingStatusException;
import ru.practicum.shareit.exception.InvalidOwnerException;
import ru.practicum.shareit.exception.ItemUnavailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.contracts.ItemRepositoryInterface;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.contracts.UserRepositoryInterface;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingServiceTest {

    @Autowired
    BookingRepositoryInterface bookingRepository;

    @Autowired
    ItemRepositoryInterface itemRepository;

    @Autowired
    UserRepositoryInterface userRepository;

    @Autowired
    BookingService bookingService;

    @Test
    void create() {
        User owner = createUser();
        Item item = createItem(owner);
        BookingCreateDto bookingCreateDto = makeBookingCreateDto(owner, item);

        BookingDto bookingRes = bookingService.create(bookingCreateDto);

        Optional<Booking> booking = bookingRepository.findById(bookingRes.getId());

        Assertions.assertTrue(booking.isPresent());
    }

    @Test
    void createWithInvalidItemReturnThrow() {
        Random random = new Random();

        Assertions.assertThrowsExactly(
                NotFoundException.class,
                () -> bookingService.create(makeBookingCreateDto(
                        new User(random.nextLong(), null, null),
                        new Item(random.nextLong(), null, null, true, null, null)
                ))
        );
    }

    @Test
    void createWithInvalidUserReturnThrow() {
        User owner = createUser();
        User user = new User(new Random().nextLong(), null, null);

        Assertions.assertThrowsExactly(
                NotFoundException.class,
                () -> bookingService.create(makeBookingCreateDto(
                        user,
                        createItem(owner)
                ))
        );
    }

    @Test
    void createWithUnavailableItemReturnThrow() {
        User owner = createUser();

        Item item = createItem(owner);
        item.setAvailable(false);
        itemRepository.save(item);

        Assertions.assertThrowsExactly(
                ItemUnavailableException.class,
                () -> bookingService.create(makeBookingCreateDto(
                        owner,
                        item
                ))
        );
    }

    @Test
    void approve() {
        User owner = createUser();
        Item item = createItem(owner);
        BookingCreateDto bookingCreateDto = makeBookingCreateDto(owner, item);

        BookingDto bookingRes = bookingService.create(bookingCreateDto);

        BookingDto booking = bookingService.approve(
                bookingRes.getId(),
                owner.getId(),
                true
        );

        Assertions.assertNotNull(booking);
        Assertions.assertEquals(BookingStatus.APPROVED, booking.getStatus());
    }

    @Test
    void approveWithInvalidIdReturnThrow() {
        Assertions.assertThrowsExactly(
                NotFoundException.class,
                () -> bookingService.approve(new Random().nextLong(), null, true)
        );
    }

    @Test
    void approveWithInvalidStatusReturnThrow() {
        User owner = createUser();
        Item item = createItem(owner);

        Booking booking = bookingRepository.save(new Booking(
                null,
                item,
                owner,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                BookingStatus.APPROVED
        ));

        Assertions.assertThrowsExactly(
                InvalidBookingStatusException.class,
                () -> bookingService.approve(booking.getId(), owner.getId(), true)
        );
    }

    @Test
    void approveWithInvalidOwnerReturnThrow() {
        User owner = createUser();
        Item item = createItem(owner);
        BookingCreateDto bookingCreateDto = makeBookingCreateDto(owner, item);
        BookingDto bookingRes = bookingService.create(bookingCreateDto);
        User invalidOwner = createUser();

        Assertions.assertThrowsExactly(
                InvalidOwnerException.class,
                () -> bookingService.approve(bookingRes.getId(), invalidOwner.getId(), true)
        );
    }

    @Test
    void getById() {
        User owner = createUser();
        User booker = createUser();
        Item item = createItem(owner);
        BookingCreateDto bookingCreateDto = makeBookingCreateDto(booker, item);

        BookingDto bookingRes = bookingService.create(bookingCreateDto);
        BookingDto booking = bookingService.getById(bookingRes.getId(), owner.getId());

        Assertions.assertEquals(bookingRes.getId(), booking.getId());
    }

    @Test
    void getByIdWithInvalidIdReturnThrow() {
        Assertions.assertThrowsExactly(
                NotFoundException.class,
                () -> bookingService.getById(new Random().nextLong(), null)
        );
    }

    @Test
    void getByIdAndOwner() {
        User owner = createUser();
        Item item = createItem(owner);
        BookingCreateDto bookingCreateDto = makeBookingCreateDto(owner, item);

        BookingDto bookingRes = bookingService.create(bookingCreateDto);
        BookingDto booking = bookingService.getById(bookingRes.getId(), owner.getId());

        Assertions.assertEquals(bookingRes.getId(), booking.getId());
    }

    @Test
    void getByIdAndWrongBooker() {
        User owner = createUser();
        User wrongBooker = createUser();
        Item item = createItem(owner);
        BookingCreateDto bookingCreateDto = makeBookingCreateDto(owner, item);

        BookingDto bookingRes = bookingService.create(bookingCreateDto);

        Assertions.assertThrowsExactly(
                NotFoundException.class,
                () -> bookingService.getById(bookingRes.getId(), wrongBooker.getId())
        );
    }

    @Test
    void findItemsByOwner() {
        User owner = createUser();
        int ownerItemsCount = new Random().nextInt(10);

        for (int i = 1; i <= ownerItemsCount; i++) {
            Item item = createItem(owner);
            BookingCreateDto bookingCreateDto = makeBookingCreateDto(owner, item);
            bookingService.create(bookingCreateDto);
        }

        Assertions.assertEquals(
                ownerItemsCount,
                bookingService.getByOwnerAndState(owner.getId(), BookingState.ALL).size()
        );
        Assertions.assertEquals(
                ownerItemsCount,
                bookingService.getByOwnerAndState(owner.getId(), BookingState.CURRENT).size()
        );
        Assertions.assertEquals(
                0,
                bookingService.getByOwnerAndState(owner.getId(), BookingState.REJECTED).size()
        );
        Assertions.assertEquals(
                ownerItemsCount,
                bookingService.getByOwnerAndState(owner.getId(), BookingState.WAITING).size()
        );
        Assertions.assertEquals(
                0,
                bookingService.getByOwnerAndState(owner.getId(), BookingState.PAST).size()
        );
        Assertions.assertEquals(
                0,
                bookingService.getByOwnerAndState(owner.getId(), BookingState.FUTURE).size()
        );
    }

    @Test
    void findItemsByBooker() {
        User owner = createUser();
        User booker = createUser();
        int ownerItemsCount = new Random().nextInt(10);

        for (int i = 1; i <= ownerItemsCount; i++) {
            Item item = createItem(owner);
            BookingCreateDto bookingCreateDto = makeBookingCreateDto(booker, item);
            bookingService.create(bookingCreateDto);
        }

        Assertions.assertEquals(
                ownerItemsCount,
                bookingService.getByBookerAndState(booker.getId(), BookingState.ALL).size()
        );
        Assertions.assertEquals(
                ownerItemsCount,
                bookingService.getByBookerAndState(booker.getId(), BookingState.CURRENT).size()
        );
        Assertions.assertEquals(
                0,
                bookingService.getByBookerAndState(booker.getId(), BookingState.REJECTED).size()
        );
        Assertions.assertEquals(
                ownerItemsCount,
                bookingService.getByBookerAndState(booker.getId(), BookingState.WAITING).size()
        );
        Assertions.assertEquals(
                0,
                bookingService.getByBookerAndState(booker.getId(), BookingState.PAST).size()
        );
        Assertions.assertEquals(
                0,
                bookingService.getByBookerAndState(booker.getId(), BookingState.FUTURE).size()
        );
    }

    private User createUser() {
        long id = new Random().nextLong();

        return userRepository.save(new User(
                null,
                "user name #" + id,
                "user" + id + "@yandex.net"
        ));
    }

    private Item createItem(User owner) {
        long id = new Random().nextLong();

        return itemRepository.save(new Item(
                null,
                "name #" + id,
                "description " + id,
                true,
                owner,
                null
        ));
    }

    private BookingCreateDto makeBookingCreateDto(User booker, Item item) {
        return new BookingCreateDto(
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2L),
                item.getId(),
                booker.getId()
        );
    }

}