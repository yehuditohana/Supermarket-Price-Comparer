package com.example.mystore.dto.xml;
/**
 * ItemXmlDTO is used for parsing and processing item details from Item XML files.
 */
public class ItemXmlDTO {
    private String  itemID;
    private String  itemName;
    private String  manufacturerName;
    private String  manufactureCountry;
    private String  manufacturerItemDescription;
    private String  unitQty;
    private Double  quantity;
    private String  unitOfMeasure;
    private Boolean isWeighted;
    private String  qtyInPackage;


    public ItemXmlDTO() {}

    // getters + setters

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

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }
    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public Boolean getWeighted() {
        return isWeighted;
    }

    public void setWeighted(Boolean weighted) {
        isWeighted = weighted;
    }

    public String getQtyInPackage() {
        return qtyInPackage;
    }
    public void setQtyInPackage(String qtyInPackage) {
        this.qtyInPackage = qtyInPackage;
    }

}