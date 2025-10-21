package com.example.mystore.database.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
// This class represents a table that stores all the processes in the application.
// Each record corresponds to a specific process and its status at any given time.
@Entity
@Table(name = "PROCESS_TRACKER")
public class ProcessTracker {

    // Unique ID for the process tracker
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING) //Saved the value as String
    // Name of the process we are tracking
    @Column(name = "process_name", nullable = false, unique = true)
    private ProcessName processName;

    // True if the process has been completed at least once
    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false;

    // Last time the process was updated
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    public ProcessTracker() {
    }

    public Long getId() {
        return id;
    }

    public ProcessName getProcessName() {
        return processName;
    }

    public void setProcessName(ProcessName processName) {
        this.processName = processName;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}