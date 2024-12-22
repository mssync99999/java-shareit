package ru.practicum.shareit.request.dto;


//-import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ItemRequestDto {
    //запрос входящий в контроллер
    //-@NotEmpty
    String description;
}
