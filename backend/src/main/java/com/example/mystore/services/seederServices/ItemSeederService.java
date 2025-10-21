package com.example.mystore.services.seederServices;

import com.example.mystore.database.cleaning.ItemCleaner;
import com.example.mystore.database.entities.Item;
import com.example.mystore.database.repositories.ItemRepository;
import com.example.mystore.dto.xml.ItemXmlDTO;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.mystore.utils.CollectionUtils.partition;

@Service
public class ItemSeederService {
    private static final Logger logger = LoggerFactory.getLogger(ItemSeederService.class);

    private static final int BATCH_SIZE = 300;
    private final ItemRepository itemRepository;
    private final ItemCleaner itemCleaner;


    public ItemSeederService(ItemRepository itemRepository, ItemCleaner itemCleaner) {
        this.itemRepository = itemRepository;
        this.itemCleaner = itemCleaner;
    }
    /**
     * Cleans a list of ItemXmlDTO objects and converts them into Item entities.
     *
     * @param dtos the list of ItemXmlDTO objects
     * @return a list of cleaned Item entities
     */
    public List<Item> createCleanItems(List<ItemXmlDTO> dtos){
        List<Item> items = new ArrayList<>();
        for(ItemXmlDTO dto: dtos){
            Item item = new Item();
            itemCleaner.clean(dto, item);
            items.add(item);
        }
        return items;
    }

    /**
     * Saves only new items to the database, ignoring duplicates.
     * Performs insertion in batches for better performance.
     *
     * @param items the list of Item entities to save
     * @return the number of items successfully inserted
     */

    @Transactional
    public int saveNewItems(List<Item> items) {
        // Extract all unique IDs from the list of items
        Set<String> allUniqueIds  = new HashSet<>();
        for (Item item : items) {
            String id = item.getItemID();
            if (id != null) {
                allUniqueIds .add(id);
            }
        }
        //Checking whether the identifier already exists
        //Contacting DB in batches
        Set<String> existingIds = new HashSet<>();
        List<List<String>> idBatches = partition(new ArrayList<>(allUniqueIds), BATCH_SIZE);


        //Saving in 'existingIds' all ids that already exist in DB
        for (List<String> batch : idBatches) {
            List<Item> existing = itemRepository.findAllById(batch);
            for (Item item : existing) {
                existingIds.add(item.getItemID());
            }
        }

        Set<String> seenIds = new HashSet<>();
        //List of items we will go to insert to DB
        List<Item> toInsert = new ArrayList<>();
        for (Item item : items) {
            String id = item.getItemID();
            if (id != null && !existingIds.contains(id) && seenIds.add(id)) {
                toInsert.add(item);
            }
        }
        if (!toInsert.isEmpty()) {
            //insert with batches
            List<List<Item>> insertBatches = partition(toInsert, BATCH_SIZE);
            for (List<Item> batch : insertBatches) {
                itemRepository.saveAll(batch);
                logger.info(" Inserting {} new items in {} batches", toInsert.size(), insertBatches.size());

            }

        }
        return toInsert.size();
    }
    /**
     * Retrieves items that are available in at least a specified number of different stores.
     *
     * @param minStoreCount the minimum number of stores
     * @return a list of Item entities
     */
    public List<Item> getItemsWithAtLeastNPrices(int minStoreCount) {
        return itemRepository.findItemsAvailableInAtLeastNStores(minStoreCount);
    }

    /**
     * Retrieves all items that do not have an image.
     *
     * @return a list of Item entities without images
     */
    public List<Item> getItemsWithoutImage() {
        return itemRepository.findItemsWithoutImage();
    }


}
