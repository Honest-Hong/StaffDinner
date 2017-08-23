package com.project.boostcamp.publiclibrary.sqlite;

/**
 * Created by Hong Tae Joon on 2017-08-10.
 * SQL에서 사용하는 문자열을 모아둔 클래스입니다.
 */

public class SQLData {
    /**
     * 계약서 테이블 생성 쿼리
     */
    public static final String QUERY_CREATE_TABLE_CONTACT
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

    /**
     * 계약 목록 비우기 쿼리
     */
    public static final String QUERY_DELETE_CONTACTS
            = "DELETE FROM contacts";

    /**
     * 근접한 식당 테이블 생성
     */
    public static final String QUERY_CREATE_TABLE_NEAR_ADMINS
            = "CREATE TABLE nearAdmins (" +
            "id TEXT," +
            "type INTEGER," +
            "name TEXT," +
            "distance REAL," +
            "style TEXT," +
            "PRIMARY KEY ( id, type) )";

    /**
     * 근접한 식당 테이블 비우기
     */
    public static final String QUERY_DELETE_NEAR_ADMINS
            = "DELETE FROM nearAdmins";

    /**
     * 근접한 식당 추가
     */
    public static final String QUERY_INSERT_NEAR_ADMINS
            = "INSERT INTO nearAdmins VALUES (?, ?, ?, ?, ?)";

    /**
     * 근접한 식당 불러오기
     */
    public static final String QUERY_SELECT_NEAR_ADMINS
            = "SELECT * FROM nearAdmins";

    /**
     * 이벤트 테이블 생성
     */
    public static final String QUERY_CREATE_TABLE_EVENTS
            = "CREATE TABLE events (" +
            "number INTEGER PRIMARY KEY," +
            "title TEXT," +
            "content TEXT)";

    /**
     * 이벤트 테이블 삭제
     */
    public static final String QUERY_DELETE_EVENTS
            = "DELETE FROM events";

    /**
     * 이벤트 테이블 추가
     */
    public static final String QUERY_INSERT_EVENT
            = "INSERT INTO events VALUES (?, ?, ?)";

    /**
     * 이벤트 목록 불러오기
     */
    public static final String QUERY_SELECT_EVENTS
            = "SELECT * FROM events";

    /**
     * 새로운 식당 테이블 생성
     */
    public static final String QUERY_CREATE_TABLE_NEW_ADMINS
            = "CREATE TABLE newAdmins (" +
            "id TEXT," +
            "type INTEGER," +
            "name TEXT," +
            "style TEXT," +
            "PRIMARY KEY ( id, type) )";

    /**
     * 새로운 식당 테이블 비우기
     */
    public static final String QUERY_DELETE_NEW_ADMINS
            = "DELETE FROM newAdmins";

    /**
     * 새로운 식당 추가
     */
    public static final String QUERY_INSERT_NEW_ADMINS
            = "INSERT INTO newAdmins VALUES (?, ?, ?, ?)";

    /**
     * 새로운 식당 불러오기
     */
    public static final String QUERY_SELECT_NEW_ADMINS
            = "SELECT * FROM newAdmins";

    /**
     * 근접한 리뷰 테이블 생성
     */
    public static final String QUERY_CREATE_TABLE_NEAR_REVIEWS
            = "CREATE TABLE nearReviews (" +
            "writer TEXT," +
            "receiver TEXT," +
            "receiverId TEXT," +
            "receiverType INTEGER," +
            "content TEXT," +
            "rating REAL," +
            "writedTime INTEGER)";

    /**
     * 근접한 리뷰 테이블 비우기
     */
    public static final String QUERY_DELETE_NEAR_REVIEWS
            = "DELETE FROM nearReviews";

    /**
     * 근접한 리뷰 추가
     */
    public static final String QUERY_INSERT_NEAR_REVIEWS
            = "INSERT INTO nearReviews VALUES (?, ?, ?, ?, ?, ?, ?)";

    /**
     * 근접한 리뷰 불러오기
     */
    public static final String QUERY_SELECT_NEAR_REVIEWS
            = "SELECT * FROM nearReviews";

    /**
     * 식당 테이블 생성
     */
    public static final String QUERY_CREATE_ADMINS
            = "CREATE TABLE admins (" +
            "id TEXT," +
            "type INTEGER," +
            "name TEXT," +
            "phone TEXT," +
            "style TEXT," +
            "menu TEXT," +
            "cost INTEGER," +
            "address TEXT," +
            "lat REAL," +
            "lng REAL," +
            "bonusImageCount INTEGER," +
            "PRIMARY KEY (id, type))";

    /**
     * 식당 추가
     */
    public static final String QUERY_INSERT_ADMIN
            = "INSERT INTO admins VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * 식당 업데이트
     */
    public static final String QUERY_UPDATE_ADMIN
            = "UPDATE admins" +
            " SET name=?, phone=?, style=?, menu=?, cost=?, address=?, lat=?, lng=?, bonusImageCount=?" +
            " WHERE id=? AND type=?";

    /**
     * 식당 찾기
     */
    public static final String QUERY_SELECT_ADMIN
            = "SELECT * FROM admins WHERE id=? AND type=?";
}
