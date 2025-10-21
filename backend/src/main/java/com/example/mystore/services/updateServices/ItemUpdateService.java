package com.example.mystore.services.updateServices;

import com.example.mystore.database.entities.Item;
import com.example.mystore.database.repositories.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItemUpdateService {

    private final ItemRepository itemRepository;

    public ItemUpdateService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // Find all items
    /**
     * Retrieves all items from the database.
     *
     * @return a list of all Item entities
     */
    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }


     // Updates the minimum and maximum prices for each item based on its available price records.

    public void updateItemPriceRange() {
        itemRepository.updateMinAndMaxPrices();
    }
    /**
     * Retrieves a list of items that do not have an associated image.
     *
     * @return a list of Item entities without an image
     */

    public List<Item> getItemsWithoutImage() {
        return itemRepository.findItemsWithoutImage();
    }
    /**
     * Saves a list of Item entities to the database.
     *
     * @param items the list of Item entities to save
     */
    public void saveAllItems(List<Item> items) {
        itemRepository.saveAll(items);
    }
    /**
     * Finds an Item by its item ID.
     *
     * @param itemId the ID of the item
     * @return an Optional containing the Item if found, or empty if not found
     */
    public Optional<Item> findById(String itemId) {
        return itemRepository.findById(itemId);
    }


}