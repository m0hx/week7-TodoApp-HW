package com.ga.todo.service;

import com.ga.todo.exception.InformationExistException;
import com.ga.todo.exception.InformationNotFoundException;
import com.ga.todo.model.Category;
import com.ga.todo.model.Item;
import com.ga.todo.repository.CategoryRepository;
import com.ga.todo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    private CategoryRepository categoryRepository;
    private ItemRepository itemRepository;

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Autowired
    public void setItemRepository(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    // Create Item
    public Item createItem(Long categoryId, Item item) {
        System.out.println("Service Calling createItem ==>");
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow( () ->
                        new InformationNotFoundException("Category with id " + categoryId + " not found.")

                );

        item.setCategory(category);
        return itemRepository.save(item);
    }


    // Get all items
    public List<Item> getItems() {
        System.out.println("Service Calling getItems ==>");
        return itemRepository.findAll();
    }

    // Get One items by Id
    public Optional<Item> getItem(Long itemId) {
        System.out.println("Service Calling getItem ==>");
        Optional<Item> item = itemRepository.findById(itemId);

        if(item.isPresent()){
            return item;
        } else {
            throw new InformationNotFoundException("Item with id " + itemId + " not found");
        }
    }

    // findByIdAndCategoryId

    // Update Item
    public Item updateItem(long itemId, @RequestBody Item itemObject){
        System.out.println("Service Calling updateItem");

        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() ->
                        new InformationNotFoundException("Item with id " + itemId + " not found")
                );

        // Check if the new name already exists (but allow same name if it's the same category)
        if (!itemObject.getName().equals(existingItem.getName())) {
            Item itemWithName = itemRepository.findByName(itemObject.getName());
            if(itemWithName != null){
                throw new InformationExistException("Item with name " + itemObject.getName() + " already exists");
            }
        }

        existingItem.setName(itemObject.getName());
        existingItem.setDescription(itemObject.getDescription());
        existingItem.setDueDate(itemObject.getDueDate());
        existingItem.setCategory(itemObject.getCategory());
        return itemRepository.save(existingItem);
    }

    // Delete Item
    public Item deleteItem(Long itemId) {
        System.out.println("Service Calling deleteItem ==>");

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new InformationNotFoundException("Item with id " + itemId + " not found")
                );

        itemRepository.delete(item);
        return item;
    }




}
