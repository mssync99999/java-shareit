package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.validated.Create;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@Validated({Create.class}) @RequestHeader("X-Sharer-User-Id") Long userId,
                                                                @RequestBody BookingDto bookingDto) {

        return bookingClient.create(userId, bookingDto);
    }


    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateApproved(@PathVariable Long bookingId,
                             @RequestHeader("X-Sharer-User-Id") Long userId,
                             @RequestParam("approved") Boolean isApproved) {
        return bookingClient.updateApproved(bookingId, userId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findBooking(@PathVariable Long bookingId,
                                  @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingClient.findBooking(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findBookingAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam(name = "state", defaultValue = "ALL") State state) {
        return bookingClient.findBookingAll(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findBookingOwnerAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam(name = "state", defaultValue = "ALL") State state) {
        return bookingClient.findBookingOwnerAll(userId, state);
    }



}