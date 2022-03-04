package com.example.MoneyManager.dbmanagment;

import android.content.Context;


// TODO Change the current budget every month

public class DatabaseController {
    static private DatabaseManager databaseManager;

    public DatabaseController(Context context) {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager(context);
        }
    }

    public static DatabaseManager getDatabaseManager() {

        return databaseManager;
    }
}
