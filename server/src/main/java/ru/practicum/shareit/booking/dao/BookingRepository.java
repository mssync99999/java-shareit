package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    //запросный метод + ассоциация Booker.Id + сортировочный объект
    List<Booking> findAllByBookerId(Long userId, Sort sort);

    //List<Booking> findByBooker_IdAndEndIsBefore(Long bookerId, LocalDateTime end, Sort sort);
    //запросный метод + ассоциация Booker.Id OrderByStartDesc
    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long userId, LocalDateTime datetimeNowS, LocalDateTime datetimeNowE); //, Sort sort);  //запросный метод

    //LessThanEqual запросный метод без ассоциации Booker.Id
    List<Booking> findAllByBookerAndStartAfterAndEndAfterOrderByStartDesc(User user, LocalDateTime datetimeNowS, LocalDateTime datetimeNowE);

    //jpql + ассоциация Booker.id join User.id + параметр ?1 + параметр :datetimeNow + сортировка
    @Query(" select b from Booking b inner join User u on b.booker.id = u.id " +
            " where u.id = :userId " +
            " and b.start > :datetimeNow " +
            " order by b.start DESC")
    List<Booking> findJpqlQueryFuture(Long userId, LocalDateTime datetimeNow);

    //where b.booker.id = ?1   and b.start > :datetimeNow and b.state = ?3
    //jpql + ассоциация Booker.Id + параметр ?1 + параметр :datetimeNow + сортировка
    @Query(" select b from Booking b " +
            " where b.booker.id = :userId " +
            " and b.start > :datetimeNow and b.status = :status" +
            " order by b.start DESC")
    List<Booking> findJpqlQueryStatus(Long userId, LocalDateTime datetimeNow, Status status); //StatusEquals

    //запросный метод + ассоциация Item.Owner.Id + сортировочный объект
    List<Booking> findAllByItemOwnerId(Long userId, Sort sort);

    //запросный метод + ассоциация Item.Owner.Id
    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long userId, LocalDateTime datetimeNowS, LocalDateTime datetimeNowE); //, Sort sort);  //запросный метод

    //запросный метод без ассоциации Item.Owner.Id
    List<Booking> findAllByItemOwnerAndStartAfterAndEndAfterOrderByStartDesc(User user, LocalDateTime datetimeNowS, LocalDateTime datetimeNowE);

    //jpql + ассоциация Item.Owner.Id join User.id + параметр ?1 + параметр :datetimeNow + сортировка
    @Query(" select b from Booking b " +
            " inner join Item i on b.item.id = i.id " +
            " where i.owner.id = :userId " +
            " and b.start > :datetimeNow " +
            " order by b.start DESC")
    List<Booking> findJpqlQueryFutureOwner(Long userId, LocalDateTime datetimeNow);

    //and b.start > :datetimeNow and b.state = ?3
    //jpql + ассоциация Item.Owner.Id + параметр ?1 + параметр :datetimeNow + сортировка
    @Query(" select b from Booking b " +
            " inner join Item i on b.item.id = i.id " +
            " where i.owner.id = :userId " +
            " and b.start > :datetimeNow and b.status = :status" +
            " order by b.start DESC")
    List<Booking> findJpqlQueryStatusOwner(Long userId, LocalDateTime datetimeNow, Status status);

    Booking findFirst1ByItemAndStartBeforeOrderByStartDesc(Item item, LocalDateTime datetimeNow);

    Booking findFirst1ByItemAndStartAfterOrderByStartAsc(Item item, LocalDateTime datetimeNow);

    List<Booking> findAllByBookerAndEndIsBeforeAndItemAndStatusEquals(User user, LocalDateTime datetimeNow,
                                                                    Item item, Status status);
}
