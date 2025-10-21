package com.example.mystore.database.entities;

// Enum representing different process names for the application's workflow.
// Each value corresponds to a specific process or stage in the system.
// These are the names of the processes stored in the `ProcessName` table.
public enum ProcessName {
    INITIAL_LOAD, //Indicates that the entire initial system load has completed successfully.
    PRICEFULL_LOAD, //Indicates that the download of priceFull files (for initial initialization of item and itemPrice) has completed successfully.
    PRICE_LOAD, //Indicates that the download of price files (for updating the itemprice table) has completed successfully.

    //Initializing the tables//
    TABLE_SEEDING,
    STORE_TABLE_INIT,
    CHAIN_TABLE_INIT,
    ITEM_TABLE_INIT,
    PRICE_UPDATE // Marks both for initial boot and daily update

}