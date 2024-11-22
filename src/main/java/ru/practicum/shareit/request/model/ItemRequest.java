package ru.practicum.shareit.request.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;

@Data //Lombok, чтобы сгенерировать геттеры и сеттеры для полей
@Builder //создаёт через билдер произвольный конструктор
public class ItemRequest {
    @NotNull
    private Long id;
    @Size(min = 1, message = "Описание ItemRequest должно быть больше 1 символа")
    private String description;
    @NotNull
    private User requestor;
    @PastOrPresent
    private LocalDateTime created;
}
