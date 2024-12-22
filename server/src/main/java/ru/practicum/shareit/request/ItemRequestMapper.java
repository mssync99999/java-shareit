package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User requestor /*, Item item, User booker*/) {
        return ItemRequest.builder()
                .description(itemRequestDto.getDescription())
                .requestor(requestor)
                .created(LocalDateTime.now())
                .build();
    }

    /*
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                //.id(itemRequest.getId())
                .description(itemRequest.getDescription())
                //.end(booking.getEnd())
                //.itemId(booking.getItem() != null ? booking.getItem().getId() : null)
                //.booker(booking.getBooker() != null ? booking.getBooker() : null)
                //.status(booking.getStatus() != null ? booking.getStatus() : null)
                .build();
    }

     */


    public static ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest) {
        return ItemRequestResponseDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                //.requestor(itemRequest.getRequestor() != null ? itemRequest.getRequestor() : null)
                .created(itemRequest.getCreated())
                .items(itemRequest.getItems() != null ? itemRequest.getItems() : null)
                .build();
    }
}
