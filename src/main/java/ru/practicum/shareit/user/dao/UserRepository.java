package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User create(User user);

    User update(User user, Long userId);

    Optional<User> findById(Long userId);

    List<User> findUsersAll(Long userId);

    void delete(Long userId);

    boolean checkEmail(User user);
}
