package ru.practicum.shareit.request.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
/*--*/
    //запросный метод +  OrderBy...Desc
    List<ItemRequest> findAllByRequestorOrderByCreatedDesc(User requestor);

    //запросный метод + OrderBy...Desc
    List<ItemRequest> findAllByRequestorNotOrderByCreatedDesc(User requestor);

    ItemRequest findAllById(Long id);


}
