package com.example.mystore.database.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
// This class represents the table for the `ItemPrice` entity.
// Each record refers to the price of a specific product in a specific store.
@Entity
@Table(name = "ITEM_PRICE",
  indexes = {
        @Index(name = "idx_item_price_item_id" , columnList = "item_id"),
        @Index(name = "idx_item_price_store_id", columnList = "store_id")
  }
)
public class ItemPrice {
    @EmbeddedId
    private ItemPriceKey itemPriceKey;

    @MapsId("itemID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    @MapsId("storeID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    // The `status` field is currently not actively used in the application.
    @Column(name = "status")
    private Boolean status;
    @Column(name = "price")
    private Double price; // Price of the product
    @Column(name = "price_date")
    private LocalDate priceDate; // Date when the price was last updated

    public ItemPrice() {}

    public ItemPrice(Item item,Store store,Boolean status,Double price,LocalDate priceDate) {
        this.item  = item;
        this.store = store;
        this.itemPriceKey = new ItemPriceKey(item.getItemID(), store.getStoreID());
        this.status = status;
        this.price  = price;
        this.priceDate = priceDate;
    }


    public ItemPriceKey getItemPriceKey() {
        return itemPriceKey;
    }

    public void setItemPriceKey(ItemPriceKey itemPriceKey) {
        this.itemPriceKey = itemPriceKey;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDate getPriceDate() {
        return priceDate;
    }

    public void setPriceDate(LocalDate priceDate) {
        this.priceDate = priceDate;
    }
}
