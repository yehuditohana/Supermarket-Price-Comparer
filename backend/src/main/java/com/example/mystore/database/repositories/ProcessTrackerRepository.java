package com.example.mystore.database.repositories;

import com.example.mystore.database.entities.ProcessName;
import com.example.mystore.database.entities.ProcessTracker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProcessTrackerRepository extends JpaRepository<ProcessTracker, Long> {
    // Finds a `ProcessTracker` by its associated `ProcessName` enum.
    Optional<ProcessTracker> findByProcessName(ProcessName processName);

    // Checks if a `ProcessTracker` with the given `ProcessName` exists.
    boolean existsByProcessName(ProcessName processName);

}
