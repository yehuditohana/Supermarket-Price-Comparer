package com.example.mystore.services;

import com.example.mystore.database.entities.ProcessName;
import com.example.mystore.database.entities.ProcessTracker;
import com.example.mystore.database.repositories.ProcessTrackerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProcessTrackerService {

    private final ProcessTrackerRepository processTrackerRepository;

    public ProcessTrackerService(ProcessTrackerRepository processTrackerRepository) {
        this.processTrackerRepository = processTrackerRepository;
    }

    // Check if a process is already completed
    public boolean isProcessCompleted(ProcessName processName) {
        Optional<ProcessTracker> tracker = processTrackerRepository.findByProcessName(processName);
        if (tracker.isPresent())
            return tracker.get().getIsCompleted();
        return false;
    }

    /**
     * Marks a specific process as completed.
     *
     * @param processName the name of the process
     */
    public void markProcessCompleted(ProcessName processName) {
        ProcessTracker tracker = findOrCreateTracker(processName);
        tracker.setIsCompleted(true);
        tracker.setUpdatedAt(LocalDateTime.now());
        processTrackerRepository.save(tracker);
    }
    /**
     * Marks a specific process as uncompleted.
     *
     * @param processName the name of the process
     */
    public void markProcessUnCompleted(ProcessName processName) {
        ProcessTracker tracker = findOrCreateTracker(processName);
        tracker.setIsCompleted(false);
        tracker.setUpdatedAt(LocalDateTime.now());
        processTrackerRepository.save(tracker);
    }

    private ProcessTracker findOrCreateTracker(ProcessName processName) {
        Optional<ProcessTracker> optionalTracker = processTrackerRepository.findByProcessName(processName);
        if (optionalTracker.isPresent()) {
            return optionalTracker.get();
        } else {
            ProcessTracker tracker = new ProcessTracker();
            tracker.setProcessName(processName);
            tracker.setUpdatedAt(LocalDateTime.now());
            return tracker;
        }
    }
}