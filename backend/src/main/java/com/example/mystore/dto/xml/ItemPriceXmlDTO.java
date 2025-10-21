package com.example.mystore.dto.xml;
/**
 * ItemPriceXmlDTO is used for parsing and processing item price data from Price XML files.
 * Helps in importing and updating item pricing information from prices XML files.
 */
import java.time.LocalDate;

public class ItemPriceXmlDTO {
    private String itemID;
    private Boolean status;
    private Double price;
    private LocalDate priceDate;

    public String getItemID()       { return itemID; }
    public void   setItemID(String itemId) { this.itemID = itemId; }

    public Boolean getStatus()      { return status; }
    public void    setStatus(Boolean status) { this.status = status; }

    public Double getPrice()        { return price; }
    public void   setPrice(Double price) { this.price = price; }

    public LocalDate getPriceDate() { return priceDate; }
    public void      setPriceDate(LocalDate priceDate) { this.priceDate = priceDate; }
}