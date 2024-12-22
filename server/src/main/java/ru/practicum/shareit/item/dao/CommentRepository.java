package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    //+!!! Для этого также понадобится создать интерфейс CommentRepository.
    // Вероятно, вам также захочется добавить в
    //+CommentRepository новые методы для поиска комментариев к вещи.
    //List<Comment> findAllByItemId(Long itemId);

    List<Comment> findAllByItem(Item item);
}
