package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;

@Data //Lombok, чтобы сгенерировать геттеры и сеттеры для полей
@Builder //создаёт через билдер произвольный конструктор
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private User booker;
    private Status status;
}
