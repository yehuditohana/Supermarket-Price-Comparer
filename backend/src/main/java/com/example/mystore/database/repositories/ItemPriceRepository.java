package com.example.mystore.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.mystore.database.entities.ItemPrice;
import com.example.mystore.database.entities.ItemPriceKey;

import java.util.Optional;

public interface ItemPriceRepository extends JpaRepository<ItemPrice,ItemPriceKey> {
    Optional<ItemPrice> findByItemPriceKey(ItemPriceKey itemPriceKey);// Custom query method to find an `ItemPrice` by its composite key (`ItemPriceKey`).

}
