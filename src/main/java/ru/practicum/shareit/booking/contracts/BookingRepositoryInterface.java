package ru.practicum.shareit.booking.contracts;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

public interface BookingRepositoryInterface extends JpaRepository<Booking, Long> {
}
