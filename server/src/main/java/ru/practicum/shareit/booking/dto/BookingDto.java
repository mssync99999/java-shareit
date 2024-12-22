package ru.practicum.shareit.booking.dto;

//-import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
//import org.antlr.v4.runtime.misc.NotNull;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validated.Create;

import java.time.LocalDateTime;

@Data //Lombok, чтобы сгенерировать геттеры и сеттеры для полей
@Builder //создаёт через билдер произвольный конструктор
public class BookingDto {

    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;

//-    @NotNull(groups = {Create.class})
    private User booker;

    private Status status;
}
