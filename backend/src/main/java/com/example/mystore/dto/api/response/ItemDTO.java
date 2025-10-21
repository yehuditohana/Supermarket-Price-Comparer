package com.example.mystore.dto.api.response;

/**
 * ItemDTO represents a product in the system for frontend display purposes.
 *
 * It is used in two different contexts:
 *
 * 1. General Item Listing:
 *    - Represents general product information across all stores.
 *    - The fields 'lowestPrice' and 'highestPrice' represent the minimum and maximum observed prices
 *      across different stores.
 *
 * 2. Alternatives Listing (Store-Specific Context):
 *    - Represents an alternative item that is available in a specific store.
 *    - In this context, both 'lowestPrice' and 'highestPrice' represent the actual price of the item
 *      in that specific store (and will usually be identical).
 *
 * Fields:
 * - id: The unique identifier of the item.
 * - name: The name of the item.
 * - lowestPrice: Either the lowest price across stores (general listing) or the actual store price (alternatives).
 * - highestPrice: Either the highest price across stores (general listing) or the actual store price (alternatives).
 * - imageUrl: The URL of the item's image.
 * - manufacturerName: The name of the manufacturer.
 * - manufactureCountry: The country where the item was manufactured.
 * - unitQty: Description of the quantity unit
 * - quantity: The amount associated with the unit
 * - isWeighted: Whether the item is sold by weight (true) or by unit (false).
 */
public class ItemDTO {

    private String id;
    private String name;
    private Double lowestPrice;
    private Double highestPrice;
    private String imageUrl;
    private String manufacturerName;
    private String manufactureCountry;
    private String unitQty;
    private Double quantity;
    private Boolean isWeighted;

    public ItemDTO(String id, String name, Double lowestPrice, Double highestPrice, String imageUrl, String manufacturerName, String manufactureCountry, String unitQty, Double quantity, Boolean isWeighted) {
        this.id = id;
        this.name = name;
        this.lowestPrice = lowestPrice;
        this.highestPrice = highestPrice;
        this.imageUrl = imageUrl;
        this.manufacturerName = manufacturerName;
        this.manufactureCountry = manufactureCountry;
        this.unitQty = unitQty;
        this.quantity = quantity;
        this.isWeighted = isWeighted;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getManufactureCountry() {
        return manufactureCountry;
    }

    public void setManufactureCountry(String manufactureCountry) {
        this.manufactureCountry = manufactureCountry;
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

    public Boolean getWeighted() {
        return isWeighted;
    }

    public void setWeighted(Boolean weighted) {
        isWeighted = weighted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(Double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public Double getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(Double highestPrice) {
        this.highestPrice = highestPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }





}
