package com.example.mystore.database.entities;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

// Represents a retail chain entity.
// Mapped to the `CHAIN` table in the database.
@Entity
@Table(name = "CHAIN")
public class Chain {
    // Defines a composite key consisting of the chain ID and the sub-chain ID.
    @EmbeddedId
    private ChainKey chainKey;
    // One-to-many relationship with the `Store` entity (a chain can have multiple stores)
    @OneToMany(mappedBy = "chain")
    private List<Store> stores = new ArrayList<>();
    @Column( name = "chain_name")
    private String chainName;  // Name of the chain
    @Column( name = "sub_chain_name")
    private String subChainName;  // Name of the sub-chain


    public Chain() {}

    public Chain(ChainKey chainID, String chainName) {
        this.chainKey = chainID;
        this.chainName = chainName;
    }

    public ChainKey getChainKey() {
        return chainKey;
    }

    public void setChainKey(ChainKey chainKey) {
        this.chainKey = chainKey;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public String getSubChainName() {
        return subChainName;
    }

    public void setSubChainName(String subChainName) {
        this.subChainName = subChainName;
    }
}

