package com.example.mystore.database.entities;

import jakarta.persistence.*;

import java.util.List;

// This class represents the `STORE` table in the database.
// The `Store` entity has a unique identifier (auto-generated) for each store.
// Each store is associated with a specific chain and sub-chain,
// and the combination of `chain_id`, `sub_chain_id`, and `store_number` is unique for every store.


@Entity
@Table(name = "STORE" ,
        uniqueConstraints = @UniqueConstraint(columnNames = {"chain_id", "sub_chain_id", "store_number"}),
        indexes = @Index(name = "idx_chain_sub", columnList = "chain_id , sub_chain_id")

)
public class Store {
    // Unique identifier for the store (Primary key)
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long storeID;

    // Many-to-one relationship with `Chain` entity.
    // Each store belongs to a specific chain and sub-chain (referenced by `chain_id` and `sub_chain_id`).
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "chain_id" , referencedColumnName = "chain_id" ),
            @JoinColumn(name = "sub_chain_id" , referencedColumnName = "sub_chain_id")
    })
    private Chain chain;
    @Column(name = "store_number")
    private Long storeNumber; //Store number as it is stored in the chain and sub-chain

    // One-to-many relationship with `ItemPrice`. Each store can have multiple price entries for different items.
    @OneToMany(mappedBy = "store")
    private List<ItemPrice> itemPrices;
    @Column(name = "store_name")
    private String storeName;  // Name of the store
    @Column(name = "store_city")
    private String storeCity;  // City of the store
    @Column(name = "store_address")
    private String storeAddress;  // Address of the store
    @Column(name = "store_zip_code")
    private Long storeZipCode;

    public Store() {}

    public Store( Chain chain, Long storeNumber, String storeName, String storeCity, String storeAddress, Long storeZipCode) {
        this.chain = chain;
        this.storeNumber = storeNumber;
        this.storeName = storeName;
        this.storeCity = storeCity;
        this.storeAddress = storeAddress;
        this.storeZipCode = storeZipCode;
    }

    public Long getStoreID() {
        return storeID;
    }

    public void setStoreID(Long storeID) {
        this.storeID = storeID;
    }

    public Chain getChain() {
        return chain;
    }

    public void setChain(Chain chain) {
        this.chain = chain;
    }

    public Long getStoreNumber() {
        return storeNumber;
    }

    public void setStoreNumber(Long storeNumber) {
        this.storeNumber = storeNumber;
    }

    public List<ItemPrice> getItemPrices() {
        return itemPrices;
    }

    public void setItemPrices(List<ItemPrice> itemPrices) {
        this.itemPrices = itemPrices;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreCity() {
        return storeCity;
    }

    public void setStoreCity(String storeCity) {
        this.storeCity = storeCity;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public long getStoreZipCode() {
        return storeZipCode;
    }

    public void setStoreZipCode(long storeZipCode) {
        this.storeZipCode = storeZipCode;
    }
}


