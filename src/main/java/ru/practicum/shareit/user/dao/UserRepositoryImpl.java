package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private HashMap<Long, User> unit = new HashMap<>();
    private Long idUnit = 0L; //инициализируем счётчик id

    @Override
    public User create(User user) {
        unit.put(++idUnit, user);
        user.setId(idUnit);
        return user;
    }

    @Override
    public User update(User user, Long userId) {
        User userTemp = unit.get(userId);

        if (user.getName() != null) {
            userTemp.setName(user.getName());
        }
        if (user.getEmail() != null) {
            userTemp.setEmail(user.getEmail());
        }
        return userTemp;
    }

    @Override
    public Optional<User> findById(Long userId) {
        return Optional.ofNullable(unit.get(userId));
    }

    @Override
    public List<User> findUsersAll(Long userId) {
        List<User> userTemp = new ArrayList<>(unit.values());
        return userTemp;
    }

    @Override
    public void delete(Long userId) {
        unit.remove(userId);
    }

    @Override
    public boolean checkEmail(User user) {
        boolean result = unit.values().stream()
                .anyMatch(a -> a.getEmail().equals(user.getEmail()));
        return result;
    }
}
