package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

@Data //Lombok, чтобы сгенерировать геттеры и сеттеры для полей
@Builder //создаёт через билдер произвольный конструктор
public class User {
    private Long id;
    private String name;
    private String email;
}