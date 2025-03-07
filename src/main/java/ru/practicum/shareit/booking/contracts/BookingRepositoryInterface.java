package ru.practicum.shareit.booking.contracts;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepositoryInterface extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerAndStatus(User booker, BookingStatus bookingStatus, Sort sort);

    List<Booking> findAllByBooker(User booker, Sort sort);

    List<Booking> findAllByBookerAndStartBeforeAndEndAfter(
            User booker,
            LocalDateTime start,
            LocalDateTime end,
            Sort sort
    );

    List<Booking> findAllByBookerAndEndBefore(User booker, LocalDateTime start, Sort sort);

    List<Booking> findAllByBookerAndStartAfter(User booker, LocalDateTime start, Sort sort);

    List<Booking> findAllByItemOwnerAndStatus(User owner, BookingStatus bookingStatus, Sort sort);

    List<Booking> findAllByItemOwner(User owner, Sort sort);

    List<Booking> findAllByItemOwnerAndStartBeforeAndEndAfter(
            User owner,
            LocalDateTime start,
            LocalDateTime end,
            Sort sort
    );

    List<Booking> findAllByItemOwnerAndEndBefore(User owner, LocalDateTime start, Sort sort);

    List<Booking> findAllByItemOwnerAndStartAfter(User owner, LocalDateTime start, Sort sort);

    boolean existsByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(Long bookerId, Long itemId, BookingStatus bookingStatus, LocalDateTime end);

    @EntityGraph(Booking.ENTITY_GRAPH_BOOKING_ITEM)
    List<Booking> findAllByItem_IdInAndStatus(List<Long> itemIds, BookingStatus bookingStatus, Sort sort);

}
