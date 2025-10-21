package com.example.mystore.database.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
// Represents a product entity in the system.
// Mapped to the `ITEM` table in the database.
@Entity
@Table(name = "ITEM",
        // Indexes were added to improve query performance,
// given that the `ITEM` table stores a relatively high volume of records.
        indexes = {
                @Index(name = "idx_item_general_category",   columnList = "general_category"),
                @Index(name = "idx_item_sub_category",       columnList = "sub_category"),
                @Index(name = "idx_item_specific_category",  columnList = "specific_category"),
                @Index(name = "idx_item_name",               columnList = "item_name")
                //Note: `item_id` is automatically indexed as it is the primary key.
        }
)
public class Item {
    @Id
    @Column(name = "item_id")
    private String itemID;  // Product ID, Primary key
// One-to-Many with `ItemPrice`: Each item can have multiple price records (different prices in different stores).
// When an Item is deleted, all its ItemPrice entries are automatically removed (CascadeType.REMOVE)
// If the `Item` itself is deleted, all its related `ItemPrice` entries will also be deleted (due to cascade = REMOVE).
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY , cascade = CascadeType.REMOVE , orphanRemoval = true)
    private List<ItemPrice> itemPrices = new ArrayList<>();

// One-to-Many with `CartItems`: Each item can appear in multiple cart entries (i.e., in multiple users' carts).
// When an Item is deleted, all its cartItems entries are automatically removed (CascadeType.REMOVE)
//// If the `Item` itself is deleted, all associated `CartItems` entries will also be deleted (cascade = REMOVE).
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE , orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    @Column( name = "item_name")
    private String itemName;  // Name of the product
    @Column( name = "manufacturer_name")
    private String manufacturerName;  // Name of the manufacturer
    @Column( name = "manufacture_country")
    private String manufactureCountry;  // Country of manufacture
    @Column( name = "manufacturer_item_description")
    private String manufacturerItemDescription;  // Description of the item by the manufacturer
    //@Enumerated(EnumType.STRING)
    @Column( name = "unit_qty")
    private String unitQty;  // Unit of measure for the product (e.g. grams, liters)
    @Column( name = "quantity")
    private Double quantity;  // Quantity of the product (e.g. 250 grams)
    @Column( name = "is_weighted")
    private Boolean isWeighted;  // Whether the product is measured by weight
    @Column( name = "unit_of_measure")
    private  String unitOfMeasure;  // Unit of measure in which price is defined (e.g. 100 grams)
    @Column( name = "lowest_price")
    private Double lowestPrice;  // Lowest price for the product
    @Column( name = "highest_price")
    private Double highestPrice;  // Highest price for the product
    @Column(name = "general_category")
    private String generalCategory;
    @Column(name = "sub_category")
    private String subCategory;
    @Column(name = "specific_category")
    private String specificCategory;
    @Column(name = "image_url")
    private String imageUrl;



    public Item(String itemID, String itemName, String manufacturerName, String manufactureCountry, String manufacturerItemDescription, String unitQty, Double quantity, Boolean isWeighted, String unitOfMeasure) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.manufacturerName = manufacturerName;
        this.manufactureCountry = manufactureCountry;
        this.manufacturerItemDescription = manufacturerItemDescription;
        this.unitQty = unitQty;
        this.quantity = quantity;
        this.isWeighted = isWeighted;
        this.unitOfMeasure = unitOfMeasure;
    }

    public Item() {}
    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getManufactureCountry() {
        return manufactureCountry;
    }
    public void setManufactureCountry(String manufactureCountry) {
        this.manufactureCountry = manufactureCountry;
    }
    public String getManufacturerName() {
        return manufacturerName;
    }
    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }
    public String getManufacturerItemDescription() {
        return manufacturerItemDescription;
    }

    public void setManufacturerItemDescription(String manufacturerItemDescription) {
        this.manufacturerItemDescription = manufacturerItemDescription;
    }
    public String getUnitQty() {
        return unitQty;
    }

    public void setUnitQty(String unitQty) {
        this.unitQty = unitQty;
    }
    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
    public Boolean getBIsWeighted() {
        return isWeighted;
    }
    public void setBIsWeighted(Boolean bIsWeighted) {
        this.isWeighted = bIsWeighted;
    }

    public Double getLowestPrice() {
        return lowestPrice;
    }
    public Double getHighestPrice() {
        return highestPrice;
    }
    public void setHighestPrice(Double highestPrice) {
        this.highestPrice = highestPrice;
    }
    public void setLowestPrice(Double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public String getGeneralCategory() {
        return generalCategory;
    }

    public void setGeneralCategory(String generalCategory) {
        this.generalCategory = generalCategory;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getSpecificCategory() {
        return specificCategory;
    }

    public void setSpecificCategory(String specificCategory) {
        this.specificCategory = specificCategory;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<ItemPrice> getItemPrices() {
        return itemPrices;
    }

    public void setItemPrices(List<ItemPrice> itemPrices) {
        this.itemPrices = itemPrices;
    }
}
