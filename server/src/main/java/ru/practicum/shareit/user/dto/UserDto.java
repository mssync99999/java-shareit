package ru.practicum.shareit.user.dto;

//-import jakarta.validation.constraints.Email;
//-import jakarta.validation.constraints.NotBlank;
//-import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validated.Create;

@Data
@Builder
public class UserDto {
    //-@NotNull
    private Long id;
    //-@NotBlank(message = "Название User не может быть пустым")
    private String name;
    //-@Email(groups = {Create.class})
    //-@NotBlank(groups = {Create.class})
    private String email;
}
