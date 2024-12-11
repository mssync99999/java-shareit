package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.BadStateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserWrongException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final ItemRepository itemRepositoryImpl;
    private final BookingRepository bookingRepositoryImpl;
    private final UserServiceImpl userServiceImpl;

    @Override
    public BookingResponseDto create(Long userId, BookingDto bookingDto) {
        User booker = UserMapper.toUser(userServiceImpl.findById(userId));

        bookingDto.setStatus(Status.WAITING);

        if (bookingDto.getItemId() == null) {
            throw new NotFoundException("Item не найден");
        }
        Item item = itemRepositoryImpl.findById(bookingDto.getItemId()).orElseThrow(() -> new NotFoundException("Item не найден"));

        if (!item.getAvailable()) {
            throw new ValidationException("Item забронирована");
        }
        Booking booking = BookingMapper.toBooking(bookingDto, item, booker);

        return BookingMapper.toBookingResponseDto(bookingRepositoryImpl.save(booking));
    }

    @Override
    public BookingResponseDto updateApproved(Long bookingId,
                                     Long userId,
                                     Boolean isApproved) {
        Booking booking = this.findByIdWithoutDto(bookingId);

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new UserWrongException("Пользователь не владелец вещи");
        }

        if (!booking.getStatus().equals(Status.WAITING)) {
            throw new ValidationException("Статус уже установлен");
        }

        if (isApproved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        return BookingMapper.toBookingResponseDto(bookingRepositoryImpl.save(booking));
    }

    @Override
    public BookingResponseDto findBooking(Long bookingId, Long userId) {
        Booking booking = this.findByIdWithoutDto(bookingId);

        if (!booking.getItem().getOwner().getId().equals(userId) &&
                !booking.getBooker().getId().equals(userId)) {
            throw new NotFoundException("Пользователь не имеет доступа к чтению данных");
        }

        return BookingMapper.toBookingResponseDto(booking);
    }

    @Override
    public List<BookingResponseDto> findBookingAll(Long userId, State state) {

        User booker = UserMapper.toUser(userServiceImpl.findById(userId));

        List<Booking> tempBooking = new ArrayList<>();
        switch (state) {
            case ALL:
                tempBooking = bookingRepositoryImpl.findAllByBookerId(userId, Sort.by(Sort.Direction.DESC, "start"));
                break;
            case CURRENT:
                tempBooking = bookingRepositoryImpl.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case PAST:
                tempBooking = bookingRepositoryImpl.findAllByBookerAndStartAfterAndEndAfterOrderByStartDesc(booker, LocalDateTime.now(), LocalDateTime.now());
                break;
            case FUTURE:
                tempBooking = bookingRepositoryImpl.findJpqlQueryFuture(userId, LocalDateTime.now());
                break;
            case WAITING:
                tempBooking = bookingRepositoryImpl.findJpqlQueryStatus(userId, LocalDateTime.now(), Status.WAITING);
                break;
            case REJECTED:
                tempBooking = bookingRepositoryImpl.findJpqlQueryStatus(userId, LocalDateTime.now(), Status.REJECTED);
                break;
            default:
                throw new BadStateException("Нет такого статуса");
        }

        return tempBooking.stream()
                .map(BookingMapper::toBookingResponseDto)
                .collect(Collectors.toList());

    }

    @Override
    public List<BookingDto> findBookingOwnerAll(Long userId, State state) {

        User booker = UserMapper.toUser(userServiceImpl.findById(userId));

        List<Booking> tempBooking = new ArrayList<>();
        switch (state) {
            case ALL:
                tempBooking = bookingRepositoryImpl.findAllByItemOwnerId(userId, Sort.by(Sort.Direction.DESC, "start"));
                break;
            case CURRENT:
                tempBooking = bookingRepositoryImpl.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case PAST:
                tempBooking = bookingRepositoryImpl.findAllByItemOwnerAndStartAfterAndEndAfterOrderByStartDesc(booker, LocalDateTime.now(), LocalDateTime.now());
                break;
            case FUTURE:
                tempBooking = bookingRepositoryImpl.findJpqlQueryFutureOwner(userId, LocalDateTime.now());
                break;
            case WAITING:
                tempBooking = bookingRepositoryImpl.findJpqlQueryStatusOwner(userId, LocalDateTime.now(), Status.WAITING);
                break;
            case REJECTED:
                tempBooking = bookingRepositoryImpl.findJpqlQueryStatusOwner(userId, LocalDateTime.now(), Status.REJECTED);
                break;
            default:
                throw new BadStateException("Нет такого статуса");
        }

        return tempBooking.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDto findById(Long bookingId) {
        return BookingMapper.toBookingDto(bookingRepositoryImpl.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking не найден")));
    }

    @Override
    public Booking findByIdWithoutDto(Long bookingId) {
        return bookingRepositoryImpl.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking не найден"));
    }
}