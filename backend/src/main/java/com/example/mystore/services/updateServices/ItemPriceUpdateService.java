package com.example.mystore.services.updateServices;

import static com.example.mystore.utils.CollectionUtils.partition;
import com.example.mystore.database.entities.Item;
import com.example.mystore.database.entities.ItemPrice;
import com.example.mystore.database.entities.ItemPriceKey;
import com.example.mystore.database.entities.Store;
import com.example.mystore.database.repositories.ItemPriceRepository;
import com.example.mystore.database.repositories.ItemRepository;
import com.example.mystore.dto.xml.ItemPriceXmlDTO;
import org.springframework.stereotype.Service;

import java.util.*;

@Service

public class ItemPriceUpdateService {
    private final ItemPriceRepository itemPriceRepository;
    private final ItemRepository itemRepository;
    private static final int BATCH_SIZE = 300;


    public ItemPriceUpdateService(ItemPriceRepository itemPriceRepository, ItemRepository itemRepository) {
        this.itemPriceRepository = itemPriceRepository;
        this.itemRepository = itemRepository;
    }
    /**
     * Maps a list of ItemPriceXmlDTO objects to ItemPrice entities for a specific store.
     *
     * @param dtos the list of ItemPriceXmlDTO objects
     * @param store the Store entity
     * @return a list of mapped ItemPrice entities
     */
    public List<ItemPrice> mapDtosToItemPrice(List<ItemPriceXmlDTO> dtos, Store store) {
        List<ItemPrice> itemPrices = new ArrayList<>();
        for (ItemPriceXmlDTO dto : dtos) {
            if (dto.getItemID() == null) {
                continue;
            }
            Optional<Item> optionalItem = itemRepository.findById(dto.getItemID());
            if (optionalItem.isEmpty()) {
                continue;
            }
            Item item = optionalItem.get();
            ItemPrice itemPrice = new ItemPrice(item, store, dto.getStatus(), dto.getPrice(), dto.getPriceDate());
            itemPrices.add(itemPrice);
        }
        return itemPrices;

    }
    /**
     * Inserts new ItemPrice entities into the database if they do not already exist.
     *
     * @param itemPrices the list of ItemPrice entities
     * @return the number of inserted entities
     */
    public int insertNewPrices(List<ItemPrice> itemPrices) {
        int insertCount = 0;
        List<List<ItemPrice>> batches = partition(itemPrices, BATCH_SIZE);

        for (List<ItemPrice> batch : batches) {
            insertCount += insertNewPricesBatch(batch);
        }

        return insertCount;
    }
    /**
     * Inserts a batch of ItemPrice entities if they do not already exist.
     *
     * @param itemPrices the batch of ItemPrice entities
     * @return the number of inserted entities
     */
    private int insertNewPricesBatch(List<ItemPrice> itemPrices) {
        //  Build a list of all composite keys present in the incoming list
        List<ItemPriceKey> allKeys = getAllKeys(itemPrices);

        // Fetch all ItemPrice rows that already exist for those keys (single DB call)
        List<ItemPrice> existingList = itemPriceRepository.findAllById(allKeys);

        // Collect the keys of those existing rows into a Set for fast lookup
        Set<ItemPriceKey> existingKeys = new HashSet<>();
        for (ItemPrice existing : existingList) {
            existingKeys.add(existing.getItemPriceKey());
        }

        // Build a list of the ItemPrice entities that are not in the existingKeys set
        List<ItemPrice> toInsert = new ArrayList<>();
        for (ItemPrice incoming : itemPrices) {
            ItemPriceKey key = incoming.getItemPriceKey();
            if (key == null || key.getItemID() == null || key.getStoreID() == null) {
                continue;  // skip invalid entries
            }
            if (!existingKeys.contains(key)) {
                toInsert.add(incoming);
            }
        }
        //Perform a single batch insert if there are any new entities
        if (!toInsert.isEmpty()) {
            itemPriceRepository.saveAll(toInsert);
        }
        return toInsert.size();
    }

    /**
     * Updates existing ItemPrice entities in the database with newer prices if needed.
     *
     * @param itemPrices the list of ItemPrice entities
     * @return the number of updated entities
     */
    public int updateExistingPrices(List<ItemPrice> itemPrices) {
        int updatedCount = 0;
        List<List<ItemPrice>> batches = partition(itemPrices, BATCH_SIZE);

        for (List<ItemPrice> batch : batches) {
            updatedCount += updateExistingPricesBatch(batch);
        }

        return updatedCount;
    }

    /**
     * Updates a batch of existing ItemPrice entities in memory and saves the changes.
     *
     * @param itemPrices the batch of ItemPrice entities
     * @return the number of updated entities
     */
    private int updateExistingPricesBatch(List<ItemPrice> itemPrices) {
        //Build a list of all composite keys to check in the database
        List<ItemPriceKey> allKeys = getAllKeys(itemPrices);

        //Fetch all ItemPrice entities that already exist for these keys
        List<ItemPrice> existingList = itemPriceRepository.findAllById(allKeys);

        // Build a map from key
        Map<ItemPriceKey, ItemPrice> existingMap = new HashMap<>();
        for (ItemPrice existing : existingList) {
            existingMap.put(existing.getItemPriceKey(), existing);
        }


        List<ItemPrice> toUpdate = new ArrayList<>();
        for (ItemPrice incoming : itemPrices) {
            ItemPriceKey key = incoming.getItemPriceKey();
            if (key == null || key.getItemID() == null) {
                continue;  // skip invalid or incomplete keys
            }

            ItemPrice entity = existingMap.get(key);

            if (entity != null) {
                if (entity.getPriceDate() == null) {
                    // Always update if existing entity has no price date
                    entity.setPrice(incoming.getPrice());
                    entity.setStatus(incoming.getStatus());
                    entity.setPriceDate(incoming.getPriceDate());
                    toUpdate.add(entity);
                } else if (incoming.getPriceDate() != null && incoming.getPriceDate().isAfter(entity.getPriceDate())) {
                    // Update only if incoming price is newer
                    entity.setPrice(incoming.getPrice());
                    entity.setStatus(incoming.getStatus());
                    entity.setPriceDate(incoming.getPriceDate());
                    toUpdate.add(entity);
                }
            }

        }

        //Perform a single batch update if there are entities to update
        if (!toUpdate.isEmpty()) {
            itemPriceRepository.saveAll(toUpdate);
        }
        return toUpdate.size();
    }


    /**
     * Extracts all valid composite keys (item_id + store_id) from a list of ItemPrice entities.
     *
     * @param itemPrices the list of ItemPrice entities
     * @return a list of valid ItemPriceKey objects
     */
    private List<ItemPriceKey> getAllKeys(List<ItemPrice> itemPrices){

        List<ItemPriceKey> allKeys = new ArrayList<>();
        for (ItemPrice ip : itemPrices) {
            ItemPriceKey key = ip.getItemPriceKey();
            if (key != null && key.getItemID() != null && key.getStoreID() != null) {
                allKeys.add(key);
            }
        }
        return allKeys;
    }

}
