package com.ga.todo.repository;

import com.ga.todo.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // Custom Methods
    Item findByName(String itemName);

    List<Item> findByCategoryId(Long categoryId);
}
