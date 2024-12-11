package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    //Item create(Item item);

    //Item update(Item item, Long itemId, Long userId);

    //Optional<Item> findById(Long itemId);

    //List<Booking> findByBooker_IdAndEndIsBefore(Long bookerId, LocalDateTime end, Sort sort);
    List<Item> findAllByOwnerId(Long userId);  //findAllByOwnerIdOrderByIdAsc запросный метод

    @Query(" select i from Item i " +
            " where (upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%'))) " +
            " and i.available = true ")
    List<Item> searchByText(String text);
}

/*
save — метод, объединяющий в себе операции Create и Update. Позволяет сохранить как одну, так и сразу несколько сущностей.
count() — позволяет узнать общее количество строк в таблице.
getById(ID) и findById(ID) — позволяют найти в базе данных сущность по её идентификатору. Реализуют операцию Read.
delete(ID) — реализует операцию удаления сущности Delete.
findAll() — позволяет запросить все сущности в таблице.
 */
