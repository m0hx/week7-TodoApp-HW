package com.ga.todo.controller;

import com.ga.todo.model.Category;
import com.ga.todo.model.Item;
import com.ga.todo.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/")
public class ItemController {

    private ItemService itemService;

    @Autowired
    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }


    // Create Item
    @PostMapping("/categories/{categoryId}/items")
    public Item createItem(@PathVariable(value = "categoryId") Long categoryId, @RequestBody Item item) {
        System.out.println("Calling createItem ==>");
        return itemService.createItem(categoryId, item);
    }

    // Read All Items
    @GetMapping("/items/")
    public List<Item> getItems() {
        System.out.println("Calling getItems ==>");
        return itemService.getItems();
    }


    // Read One Item
    @GetMapping(path = "/items/{itemId}")
    public Optional<Item> getItem(@PathVariable("itemId") Long itemId) {
        System.out.println("Calling getItem ==>");
        return itemService.getItem(itemId);
    }

    // Update Item
    @PutMapping(path = "/items/{itemId}")
    public Item updateItem(@PathVariable Long itemId , @RequestBody Item itemObject){
        System.out.println("Calling UpdateItem ==>");
        return itemService.updateItem(itemId , itemObject) ;
    }

    // Delete Item
    @DeleteMapping(path = "/items/{itemId}")
    public Item deleteItem(@PathVariable("itemId") Long itemId) {
        System.out.println("Calling deleteItem ==>");
        return itemService.deleteItem(itemId);
    }




}
