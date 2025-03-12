package ru.practicum.shareit.booking.contracts;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepositoryInterface extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerAndStatus(User booker, BookingStatus bookingStatus, Pageable pageable);

    List<Booking> findAllByBooker(User booker, Pageable pageable);

    List<Booking> findAllByBookerAndStartBeforeAndEndAfter(
            User booker,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

    List<Booking> findAllByBookerAndEndBefore(User booker, LocalDateTime start, Pageable pageable);

    List<Booking> findAllByBookerAndStartAfter(User booker, LocalDateTime start, Pageable pageable);

    List<Booking> findAllByItemOwnerAndStatus(User owner, BookingStatus bookingStatus, Pageable pageable);

    List<Booking> findAllByItemOwner(User owner, Pageable pageable);

    List<Booking> findAllByItemOwnerAndStartBeforeAndEndAfter(
            User owner,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

    List<Booking> findAllByItemOwnerAndEndBefore(User owner, LocalDateTime start, Pageable pageable);

    List<Booking> findAllByItemOwnerAndStartAfter(User owner, LocalDateTime start, Pageable pageable);

    boolean existsByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(Long bookerId, Long itemId, BookingStatus bookingStatus, LocalDateTime end);

    @EntityGraph(Booking.ENTITY_GRAPH_BOOKING_ITEM)
    List<Booking> findAllByItem_IdInAndStatus(List<Long> itemIds, BookingStatus bookingStatus, Sort sort);

}
