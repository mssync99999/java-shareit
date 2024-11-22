package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.shareit.validated.Create;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

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
    @NotNull
    private User owner;
    @NotNull
    private ItemRequest request;
}
