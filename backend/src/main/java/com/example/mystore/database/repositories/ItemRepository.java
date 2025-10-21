package com.example.mystore.database.repositories;
import com.example.mystore.database.entities.Item;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.yaml.snakeyaml.events.Event;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item,String> {
    // Checks if an item exists based on its item ID
    boolean existsByItemID(String itemID);
    // Finds items by their general category and supports pagination
    Page<Item> findByGeneralCategory(String generalCategory , Pageable pageable);
    // Finds items by their sub-category and supports pagination
    Page<Item> findBySubCategory(String subCategory , Pageable pageable);
    // Finds items by their specific category and supports pagination
    Page<Item> findBySpecificCategory(String specificCategory , Pageable pageable);
    // Finds items by their name (case-insensitive search) and supports pagination
    Page<Item> findByItemNameContainingIgnoreCase(String name, Pageable pageable);
    // Finds items by their item ID (partial match) and supports pagination
    Page<Item> findByItemIDContaining(String id, Pageable pageable);


// Finds items that are available in at least a specified number of stores
// This query finds items that are available in at least `minStores` stores
    @Query("""
    SELECT i
    FROM Item i
    WHERE (
        SELECT COUNT(DISTINCT ip.store.storeID)
        FROM ItemPrice ip
        WHERE ip.item = i
    ) >= :minStores
""")
    List<Item> findItemsAvailableInAtLeastNStores(@Param("minStores") long minStores);

    // Finds items that have no image URL set (or the image URL is empty)
    @Query("SELECT i FROM Item i WHERE i.imageUrl IS NULL OR TRIM(i.imageUrl) = ''")
    List<Item> findItemsWithoutImage();



// Updates the minimum and maximum prices for each item by calculating them from the `ItemPrice` table
    @Modifying
    @Transactional
    @Query(value = """
        UPDATE item i
        SET lowest_price = sub.min_price,
            highest_price = sub.max_price
        FROM (
            SELECT item_id,
                   MIN(price) AS min_price,
                   MAX(price) AS max_price
            FROM item_price
            GROUP BY item_id
        ) sub
        WHERE i.item_id = sub.item_id
        """, nativeQuery = true)
    void updateMinAndMaxPrices();

// Finds items in a specific store (by store id) and by their specific category
    @Query("""
    SELECT i FROM Item i
    WHERE i.specificCategory = :category
    AND EXISTS (
        SELECT ip FROM ItemPrice ip
        WHERE ip.item = i
        AND ip.itemPriceKey.storeID = :storeId
    )
""")
    List<Item> findAlternativesBySpecificCategoryAndStore(@Param("category") String category, @Param("storeId") Long storeId);

// Finds items in a specific store (by store id) and by their sub-category
    @Query("""
    SELECT i FROM Item i
    WHERE i.subCategory = :category
    AND EXISTS (
        SELECT ip FROM ItemPrice ip
        WHERE ip.item = i
        AND ip.itemPriceKey.storeID = :storeId
    )
""")
    List<Item> findAlternativesBySubCategoryAndStore(@Param("category") String category, @Param("storeId") Long storeId);



    @Query("SELECT i FROM Item i WHERE i.generalCategory = :category AND " +
            "(SELECT COUNT(DISTINCT ip.store.storeID) FROM ItemPrice ip WHERE ip.item.itemID = i.itemID) >= 20")
    Page<Item> findByGeneralCategoryWithMinStores(@Param("category") String category, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.subCategory = :category AND " +
            "(SELECT COUNT(DISTINCT ip.store.storeID) FROM ItemPrice ip WHERE ip.item.itemID = i.itemID) >= 20")
    Page<Item> findBySubCategoryWithMinStores(@Param("category") String category, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.specificCategory = :category AND " +
            "(SELECT COUNT(DISTINCT ip.store.storeID) FROM ItemPrice ip WHERE ip.item.itemID = i.itemID) >= 20")
    Page<Item> findBySpecificCategoryWithMinStores(@Param("category") String category, Pageable pageable);


}
