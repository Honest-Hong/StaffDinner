package com.project.boostcamp.publiclibrary.util;

/**
 * Created by Hong Tae Joon on 2017-08-10.
 */

public class SQLData {
    public static final String QUERY_CREATE_CONTACT_TABLE
            = "CREATE TABLE contacts (" +
            "id TEXT PRIMARY KEY," +
            "appTitle TEXT," +
            "appNumber INTEGER," +
            "appTime INTEGER," +
            "appStyle TEXT," +
            "appMenu TEXT," +
            "appLatitude REAL," +
            "appLongitude REAL," +
            "clientName TEXT," +
            "clientPhone TEXT," +
            "adminName TEXT," +
            "adminPhone TEXT," +
            "adminLatitude REAL," +
            "adminLongitude REAL," +
            "estimateMessage TEXT," +
            "estimateTime INTEGER," +
            "contactTime INTEGER)";

    public static final String QUERY_INSERT_CONTACT
            = "INSERT INTO contacts VALUES (" +
            "?, ?, ?, ?, ?, " +
            "?, ?, ?, ?, ?, " +
            "?, ?, ?, ?, ?, " +
            "?, ?)";

    public static final String QUERY_SELECT_CONTACT
            = "SELECT * FROM contacts ORDER BY contactTime DESC";

    public static final String QUERY_SELECT_CONTACT_ID
            = "SELECT id FROM contacts ORDER BY contactTime DESC LIMIT 1";
}
