package ru.practicum.shareit.item.contracts;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepositoryInterface extends JpaRepository<Item, Long> {

    @EntityGraph(Item.ENTITY_GRAPH_ITEM_OWNER)
    List<Item> findAllByOwnerId(Long ownerId);

    @Query(
            "SELECT i " +
            "FROM Item as i " +
            "WHERE i.available " +
                "AND ( LOWER(i.name) LIKE LOWER(CONCAT('%', ?1, '%')) " +
                    "OR LOWER(i.description) LIKE LOWER(CONCAT('%', ?1, '%')))"
    )
    List<Item> findAllByText(String text);

    List<Item> findAllByRequest_IdOrderByIdDesc(Long id);

}
