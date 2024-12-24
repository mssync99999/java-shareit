package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;

@Data //Lombok, чтобы сгенерировать геттеры и сеттеры для полей
@Builder //создаёт через билдер произвольный конструктор
public class ItemShortDto {
    private Long itemId;
    private String name;
    private Long ownerId;
}