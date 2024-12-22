package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.validated.Create;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
//@AllArgsConstructor
//@NoArgsConstructor
public class BookingController {
    private final BookingServiceImpl bookingServiceImpl;

    @PostMapping
    public BookingResponseDto create(@Validated({Create.class}) @RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestBody BookingDto bookingDto) {

        return bookingServiceImpl.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto updateApproved(@PathVariable Long bookingId,
                             @RequestHeader("X-Sharer-User-Id") Long userId,
                             @RequestParam("approved") Boolean isApproved) {
        return bookingServiceImpl.updateApproved(bookingId, userId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto findBooking(@PathVariable Long bookingId,
                                  @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingServiceImpl.findBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingResponseDto> findBookingAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam(name = "state", defaultValue = "ALL") State state) {
        return bookingServiceImpl.findBookingAll(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> findBookingOwnerAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam(name = "state", defaultValue = "ALL") State state) {
        return bookingServiceImpl.findBookingOwnerAll(userId, state);
    }

}