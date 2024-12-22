package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {

    BookingResponseDto create(Long userId, BookingDto bookingDto);

    BookingResponseDto updateApproved(Long bookingId, Long userId, Boolean isApproved);

    BookingDto findById(Long bookingId);

    BookingResponseDto findBooking(Long bookingId, Long userId);

    List<BookingResponseDto> findBookingAll(Long userId, State state);

    List<BookingDto> findBookingOwnerAll(Long userId, State state);

    Booking findByIdWithoutDto(Long bookingId);

}
