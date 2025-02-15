package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.contracts.ItemRepositoryInterface;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository implements ItemRepositoryInterface {

    private final Map<Integer, Item> items = new HashMap<>();
    private Integer increment = 0;

    @Override
    public Item create(final Item item) {
        Item newItem = new Item(
                getNextId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequest()
        );

        items.put(newItem.getId(), newItem);
        item.setId(newItem.getId());

        return item;
    }

    @Override
    public Item update(final Item item) {

        Item currentItem = items.get(item.getId());

        if (currentItem == null) {
            throw new NotFoundException("Item not found");
        }

        currentItem.setName(item.getName());
        currentItem.setDescription(item.getDescription());
        currentItem.setAvailable(item.getAvailable());
        currentItem.setOwner(item.getOwner());
        currentItem.setRequest(item.getRequest());

        items.put(currentItem.getId(), currentItem);

        return item;
    }

    @Override
    public void delete(final Integer id) {
        items.remove(id);
    }

    @Override
    public Item findById(Integer id) {
        return items.get(id);
    }

    @Override
    public List<Item> findByUserId(Integer userId) {
        return items.values()
                    .stream()
                    .filter(item -> item.getOwner().equals(userId))
                    .toList();
    }

    @Override
    public List<Item> findByText(String text) {
        return items.values()
                    .stream()
                    .filter(Item::getAvailable)
                    .filter(
                            item -> item.getName().toLowerCase().contains(text.toLowerCase())
                                    || item.getDescription().toLowerCase().contains(text.toLowerCase())
                    )
                    .toList();
    }

    private Integer getNextId() {
        return ++increment;
    }

}
