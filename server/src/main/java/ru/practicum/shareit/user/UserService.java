package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import java.util.List;

public interface UserService {

    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto, Long userId);

    UserDto findById(Long userId);

    List<UserDto> findUsersAll();

    void delete(Long userId);

}
