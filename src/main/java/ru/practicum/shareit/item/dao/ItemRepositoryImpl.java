package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private HashMap<Long, Item> unit = new HashMap<>();
    private Long idUnit = 0L; //инициализируем счётчик id

    @Override
    public Item create(Item item) {
        unit.put(++idUnit, item);
        item.setId(idUnit);
        return item;
    }

    @Override
    public Item update(Item item,
                          Long itemId,
                          Long userId) {

        Long id = item.getId();
        Item itemTemp = unit.get(id);

        if (item.getName() != null) {
            itemTemp.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemTemp.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemTemp.setAvailable(item.getAvailable());
        }

        return itemTemp;
    }

    @Override
    public Optional<Item> findById(Long itemId) {
        return Optional.ofNullable(unit.get(itemId));
    }

    @Override
    public List<Item> findItemsAll(Long userId) {
        List<Item> itemTemp = unit.values().stream()
                .filter(a -> a.getOwner().getId().equals(userId))
                .collect(Collectors.toList());

        return itemTemp;
    }

    public List<Item> searchByText(String text) {
        if (text.isBlank()) {
            return List.of();
        }

        //система ищет вещи, содержащие этот текст в названии или описании
        List<Item> itemTemp = unit.values().stream()
                .filter(a -> a.getDescription().toLowerCase().contains(text.toLowerCase()) ||
                        a.getName().toLowerCase().contains(text.toLowerCase()) &&
                        a.getAvailable().equals(true))
                .collect(Collectors.toList());

        return itemTemp;
    }
}

