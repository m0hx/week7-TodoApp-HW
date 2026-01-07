package com.ga.todo.service;

import com.ga.todo.exception.InformationExistException;
import com.ga.todo.exception.InformationNotFoundException;
import com.ga.todo.model.Category;
import com.ga.todo.model.User;
import com.ga.todo.repository.CategoryRepository;
import com.ga.todo.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired // injecting the dependency ... initializing it as type of category repository.
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    // JWT Stuff User<-->Category

    // used for in order to get info of current logged-in user., in security context holder info, we set,, now we take info
    public static User getCurrentLoggedInUser() {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUser();
    }



    // Create Category
    public Category createCategory(Category categoryObject) {
        System.out.println("Service Calling createCategory ==>");

        Category category = categoryRepository.findByUserIdAndName(
                CategoryService.getCurrentLoggedInUser().getId(), // JWT get userid
                categoryObject.getName()
        );

        if(category != null){
            throw new InformationExistException("Category with name " + category.getName() + " already exists");
        } else {

            categoryObject.setUser(getCurrentLoggedInUser()); // JWT set user by current user logged in id.
            categoryObject.setCreatedAt(LocalDateTime.now());
            return categoryRepository.save(categoryObject); // take this category object and save to database!
        }

    }

    // Get all categories
    public List<Category> getCategories() {
        System.out.println("Service Calling getCategories ==>");
        return categoryRepository.findAll();
    }

    // Get One category by Id
    public Optional<Category> getCategory(Long categoryId) {
        System.out.println("Service Calling getCategory ==>");
        Optional<Category> category = categoryRepository.findById(categoryId);

        if(category.isPresent()){
            return category;
        } else {
            throw new InformationNotFoundException("Category with id " + categoryId + " not found");
        }
    }



    // Update Category
    public Category updateCategory(long categoryId, @RequestBody Category categoryObject){
        System.out.println("Service Calling updateCategory");

        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new InformationNotFoundException("Category with id " + categoryId + " not found")
                );

        // Check if the new name already exists (but allow same name if it's the same category)
        if (!categoryObject.getName().equals(existingCategory.getName())) {
            Category categoryWithName = categoryRepository.findByName(categoryObject.getName());
            if(categoryWithName != null){
                throw new InformationExistException("Category with name " + categoryObject.getName() + " already exists");
            }
        }

        existingCategory.setName(categoryObject.getName());
        existingCategory.setDescription(categoryObject.getDescription());
        existingCategory.setUpdatedAt(LocalDateTime.now()); // <-- set updatedAt
        return categoryRepository.save(existingCategory);
    }



    // Delete Category
    public Category deleteCategory(Long categoryId) {
        System.out.println("Service Calling deleteCategory ==>");

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new InformationNotFoundException("Category with id " + categoryId + " not found")
                );

        categoryRepository.delete(category);
        return category;
    }


}
