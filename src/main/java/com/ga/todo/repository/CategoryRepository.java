package com.ga.todo.repository;

import com.ga.todo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // find by name
    Category findByName(String categoryName);

    // find by ID and userID
    Category findByIdAndUserId(Long categoryId,Long userId);

    // Find by UserID
    List<Category> findByUserId(Long userId);

    // find by userid and category name.
    Category findByUserIdAndName(Long userId, String categoryName);

}