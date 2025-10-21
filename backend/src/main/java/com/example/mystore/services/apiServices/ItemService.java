package com.example.mystore.services.apiServices;
import com.example.mystore.database.entities.Item;
import com.example.mystore.database.entities.ItemPrice;
import com.example.mystore.database.entities.ItemPriceKey;
import com.example.mystore.database.repositories.ItemPriceRepository;
import com.example.mystore.database.repositories.ItemRepository;
import com.example.mystore.dto.api.response.ItemDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemPriceRepository itemPriceRepository;

    public ItemService(ItemRepository itemRepository, ItemPriceRepository itemPriceRepository) {
        this.itemRepository = itemRepository;
        this.itemPriceRepository = itemPriceRepository;
    }
    /**
     * Returns a list of products based on the given page number, page size, and category level.
     * For example, if page = 3 and size = 20, returns items 60–79 (page 3 represents the 4th group since page indexing starts from 0).
     *
     * @param level category level ("General", "Sub", "Specific")
     * @param category the name of the category
     * @param page page number (starting from 0)
     * @param size number of items per page
     * @return list of ItemDTO
     */


/*This function returns a list of products based on the given page number and page size , and by order category.
For example, if the requested page is 3 and the size is 20,
 the function will return the products that correspond to rows 60 through 79
(page 3 represents the fourth group of 20 items, since page numbers start at 0)*/
 public List<ItemDTO> getItemsByCategory(String level, String category, int page, int size) {
        Page<Item> itemPage;
        Pageable pageable = PageRequest.of(page, size);

        if ("General".equalsIgnoreCase(level)) {
            itemPage = itemRepository.findByGeneralCategoryWithMinStores(category, pageable);
        } else if ("Sub".equalsIgnoreCase(level)) {
            itemPage = itemRepository.findBySubCategoryWithMinStores(category, pageable);
        } else if ("Specific".equalsIgnoreCase(level)) {
            itemPage = itemRepository.findBySpecificCategoryWithMinStores(category, pageable);
        } else {
            throw new IllegalArgumentException("Invalid category level: " + level);
        }
        return itemPage.stream().map(this::mapToItemDTO).collect(Collectors.toList());
    }




    /**
     * Searches items by either item ID or item name based on the query.
     *
     * @param query the search keyword
     * @param page page number
     * @param size number of items per page
     * @return a page of ItemDTO matching the search query
     */
/* This function returns a page of items based on the submitted search query.
 If the query is numeric, it returns a page of items filtered by item_id.
 Otherwise, it returns a page of items filtered by item_name.*/
    public Page<ItemDTO> searchItem (String query, int page, int size) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("שאילתת חיפוש ריקה");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> resultPage;

        if (query.matches("\\d+")) {
            // This is a number - therefore search by item id
            resultPage = itemRepository.findByItemIDContaining(query, pageable);
        } else {
            //Otherwise search by product name
            resultPage = itemRepository.findByItemNameContainingIgnoreCase(query, pageable);
        }
        return resultPage.map(this::mapToItemDTO);
    }
    /**
     * Finds alternative items from the same or related categories within a given store.
     *
     * @param storeId the ID of the store
     * @param itemId the ID of the original item
     * @return a list of alternative ItemDTOs with their prices in the given store
     */

    @Transactional
    public List<ItemDTO> findAlternatives(Long storeId, String itemId) {
        //find item category
        Item originalItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("המוצר לא נמצא"));

        String category = originalItem.getSpecificCategory();

        List<Item> alternatives = itemRepository.findAlternativesBySpecificCategoryAndStore(category, storeId);

        if (alternatives.isEmpty()) {
            category = originalItem.getSubCategory();
            alternatives = itemRepository.findAlternativesBySubCategoryAndStore(category, storeId);
        }

        //map any alternative item - to his price in this store
        return alternatives.stream()
                .map(item -> {
                    ItemPriceKey key = new ItemPriceKey(item.getItemID(), storeId);
                    Double price = itemPriceRepository.findByItemPriceKey(key)
                            .map(ItemPrice::getPrice)
                            .orElse(null);
                    return mapToItemDTO(item, price);
                })
                .collect(Collectors.toList());
    }
    /**
     * Maps an Item entity to an ItemDTO with a custom price.
     *
     * @param item the Item entity
     * @param customPrice the custom price to use
     * @return the mapped ItemDTO
     */
    private ItemDTO mapToItemDTO(Item item, Double customPrice) {
        return new ItemDTO(
                item.getItemID(),
                item.getItemName(),
                customPrice,
                customPrice,
                item.getImageUrl(),
                item.getManufacturerName(),
                item.getManufactureCountry(),
                item.getUnitQty(),
                item.getQuantity(),
                item.getBIsWeighted()
        );
    }

    /**
     * Maps an Item entity to an ItemDTO using the item's lowest and highest prices.
     *
     * @param item the Item entity
     * @return the mapped ItemDTO
     */
    private ItemDTO mapToItemDTO(Item item) {
        return new ItemDTO(
                item.getItemID(),
                item.getItemName(),
                item.getLowestPrice(),
                item.getHighestPrice(),
                item.getImageUrl(),
                item.getManufacturerName(),
                item.getManufactureCountry(),
                item.getUnitQty(),
                item.getQuantity(),
                item.getBIsWeighted()
        );
    }

}