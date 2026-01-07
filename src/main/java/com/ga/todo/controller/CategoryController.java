package com.ga.todo.controller;

import com.ga.todo.model.Category;
import com.ga.todo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
// Prefix for all of the APIs
@RequestMapping(path = "/api")
public class CategoryController {

    // to be able to use the category // instantiate the instance ... connect our repo.
    private CategoryService categoryService;

    @Autowired // injecting the dependency ... initializing it as type of category repository.
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // ####################################
    // here we'll write all of our API's

    // CRUD |   Method      |   Purpose
    // C =  |   HTTP POST   |   To create a record (Category)
    @PostMapping("/categories/")
    public Category createCategory(@RequestBody Category categoryObject) {
        System.out.println("Calling createCategory ==>");
        return categoryService.createCategory(categoryObject);
    }


    // R =  |   HTTP GET    |   To read all records (categoryList) / record by id (category) from database.
    @GetMapping("/categories/")
    public List<Category> getCategories() {
        System.out.println("Calling getCategories ==>");
        return categoryService.getCategories(); // service\CategoryService.java
    }


    // Read One
    @GetMapping(path = "/categories/{categoryId}")
    public Optional<Category> getCategory(@PathVariable("categoryId") Long categoryId) {
//        public Optional<Category> getCategory(@PathVariable Long categoryId) {
        System.out.println("Calling getCategory ==>");
        return categoryService.getCategory(categoryId);
    }


    // U =  |   HTTP PUT    |   To update a category
    @PutMapping(path = "/categories/{categoryId}")
    public Category updateCategory(@PathVariable Long categoryId , @RequestBody Category categoryObject){
        System.out.println("Calling UpdateCategory ==>");
        return categoryService.updateCategory(categoryId , categoryObject) ;
    }


    // D =  |   HTTP DELETE |   To remove the category from the database.
    @DeleteMapping(path = "/categories/{categoryId}")
    public Category deleteCategory(@PathVariable("categoryId") Long categoryId) {
        System.out.println("Calling deleteCategory ==>");
        return categoryService.deleteCategory(categoryId);
    }

}
