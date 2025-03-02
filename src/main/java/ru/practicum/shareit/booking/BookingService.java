package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.contracts.BookingRepositoryInterface;
import ru.practicum.shareit.booking.contracts.BookingServiceInterface;

@Service
@AllArgsConstructor
public class BookingService implements BookingServiceInterface {
    private BookingRepositoryInterface bookingRepository;
}
