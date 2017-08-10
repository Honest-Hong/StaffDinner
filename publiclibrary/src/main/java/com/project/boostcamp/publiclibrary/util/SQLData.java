package com.project.boostcamp.publiclibrary.util;

/**
 * Created by Hong Tae Joon on 2017-08-10.
 * SQL에서 사용하는 문자열을 모아둔 클래스입니다.
 */

public class SQLData {
    /**
     * 계약서 테이블 생성 쿼리
     */
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

    /**
     * 계약서 추가 쿼리
     */
    public static final String QUERY_INSERT_CONTACT
            = "INSERT INTO contacts VALUES (" +
            "?, ?, ?, ?, ?, " +
            "?, ?, ?, ?, ?, " +
            "?, ?, ?, ?, ?, " +
            "?, ?)";

    /**
     * 모든 계약서 불러오기 쿼리
     */
    public static final String QUERY_SELECT_CONTACT
            = "SELECT * FROM contacts ORDER BY contactTime DESC";

    /**
     * 가장 최근 계약서 불러오기 쿼리
     */
    public static final String QUERY_SELECT_CONTACT_LASTEST_ID
            = "SELECT id FROM contacts ORDER BY contactTime DESC LIMIT 1";
}
