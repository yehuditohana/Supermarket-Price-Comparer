package com.example.mystore.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
// Represents a composite primary key for the `Chain` entity.
// The key is composed of a chain ID and a sub-chain I
@Embeddable
public class ChainKey implements Serializable {
    @Column( name = "chain_id")
    private Long chainID;  // Unique identifier for the chain
    @Column( name = "sub_chain_id")
    private Long subChainID;  // Sub-chain identifier
    public ChainKey() {}

    public ChainKey(Long chainID, Long subChainID) {
        this.chainID = chainID;
        this.subChainID = subChainID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chainID , subChainID);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        return Objects.equals(this.chainID, ((ChainKey) obj).chainID)
                && Objects.equals(this.subChainID , ((ChainKey) obj).subChainID);
    }

    public Long getChainID() {
        return chainID;
    }

    public void setChainID(Long chainID) {
        this.chainID = chainID;
    }

    public Long getSubChainID() {
        return subChainID;
    }

    public void setSubChainID(Long subChainID) {
        this.subChainID = subChainID;
    }

}