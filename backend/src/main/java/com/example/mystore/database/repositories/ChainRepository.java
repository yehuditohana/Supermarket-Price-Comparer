package com.example.mystore.database.repositories;

import com.example.mystore.database.entities.Chain;
import com.example.mystore.database.entities.ChainKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChainRepository extends JpaRepository<Chain, ChainKey> {
    Optional<Chain> findByChainKey(ChainKey chainKey);    // Custom query method to find a `Chain` by its composite key (`ChainKey`).


}
