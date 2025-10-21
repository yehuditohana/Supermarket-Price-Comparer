package com.example.mystore.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
// This class represents a composite key for the `ItemPrice` table.
// The key is composed of the product ID and the store ID where the product is offered.
@Embeddable
public class ItemPriceKey implements Serializable {
    @Column(name = "item_id")
    private String itemID;  // Product reference (foreign key)
    @Column(name = "store_id")
    private Long storeID;

    public ItemPriceKey() {
    }

    public ItemPriceKey(String itemID, Long storeID) {
        this.itemID = itemID;
        this.storeID = storeID;
    }

    @Override
    public int hashCode() {
        return Objects.hash( itemID, storeID);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        return Objects.equals(this.itemID, ((ItemPriceKey) obj).itemID)
                && Objects.equals(this.storeID , ((ItemPriceKey) obj).storeID);
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public Long getStoreID() {
        return storeID;
    }

    public void setStoreID(Long storeID) {
        this.storeID = storeID;
    }
}
