package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data //Lombok, чтобы сгенерировать геттеры и сеттеры для полей
@Builder //создаёт через билдер произвольный конструктор
public class CommentDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}

