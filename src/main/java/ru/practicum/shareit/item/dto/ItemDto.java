package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.validated.Create;
import ru.practicum.shareit.user.model.User;
import java.util.List;

@Data //Lombok, чтобы сгенерировать геттеры и сеттеры для полей
@Builder //создаёт через билдер произвольный конструктор
public class ItemDto {
    @NotNull
    private Long id;
    @NotBlank(groups = {Create.class}, message = "Название Item не может быть пустым")
    private String name;
    @NotBlank(groups = {Create.class}, message = "Описание Item не может быть пустым")
    private String description;
    @NotNull(groups = {Create.class})
    private Boolean available;
    private User owner;
    private Long request;
    private Booking lastBooking;
    private Booking nextBooking;
    private List<CommentDto> comments;
}
