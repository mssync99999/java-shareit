package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepositoryImpl;

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepositoryImpl.save(user)); //save
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto, Long userId) {
        userDto.setId(userId);
        User user = UserMapper.toUser(userDto);

        if (!user.getId().equals(userId)) {
            throw new NotFoundException("Пользователь отличается");
        }

        if (this.findById(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        User userTemp = UserMapper.toUser(this.findById(userId));

        if (user.getName() != null) {
            userTemp.setName(user.getName());
        }
        if (user.getEmail() != null) {
            userTemp.setEmail(user.getEmail());
        }

        return UserMapper.toUserDto(userRepositoryImpl.save(userTemp));
    }

    @Override
    public UserDto findById(Long userId) {
        return UserMapper.toUserDto(userRepositoryImpl.findById(userId).orElseThrow(() -> new NotFoundException("User не найден")));
    }

    @Override
    public List<UserDto> findUsersAll() {
        return userRepositoryImpl.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        User userTemp = UserMapper.toUser(this.findById(userId));
        userRepositoryImpl.delete(userTemp);
    }
}
