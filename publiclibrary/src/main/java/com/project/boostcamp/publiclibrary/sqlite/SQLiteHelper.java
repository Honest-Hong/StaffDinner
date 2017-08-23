package com.project.boostcamp.publiclibrary.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.boostcamp.publiclibrary.domain.ContactDTO;
import com.project.boostcamp.publiclibrary.domain.EventDTO;
import com.project.boostcamp.publiclibrary.domain.GeoDTO;
import com.project.boostcamp.publiclibrary.domain.NearAdminDTO;
import com.project.boostcamp.publiclibrary.domain.NewAdminDTO;
import com.project.boostcamp.publiclibrary.domain.ReviewDTO;

import java.util.ArrayList;

/**
 * Created by Hong Tae Joon on 2017-08-10.
 * 싱글톤으로 제작된 SQLite 도구입니다.
 */

public class SQLiteHelper extends SQLiteOpenHelper {
    private static SQLiteHelper instance;
    private static final String name = "StaffDinner.db";
    private static final int version = 1;

    public static SQLiteHelper getInstance(Context context) {
        if(instance == null) {
            instance = new SQLiteHelper(context, name, null, version);
        }
        return instance;
    }

    private SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLData.QUERY_CREATE_TABLE_CONTACT);
        db.execSQL(SQLData.QUERY_CREATE_TABLE_EVENTS);
        db.execSQL(SQLData.QUERY_CREATE_TABLE_NEAR_ADMINS);
        db.execSQL(SQLData.QUERY_CREATE_TABLE_NEAR_REVIEWS);
        db.execSQL(SQLData.QUERY_CREATE_TABLE_NEW_ADMINS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }

    /**
     * 계약서를 로컬에 추가하는 함수
     * @param contact
     */
    synchronized public void insertContact(ContactDTO contact) {
        Object[] params = new Object[] {
                contact.get_id(),
                contact.getAppTitle(),
                contact.getAppNumber(),
                contact.getAppTime(),
                contact.getAppStyle(),
                contact.getAppMenu(),
                contact.getAppGeo().getCoordinates()[1],
                contact.getAppGeo().getCoordinates()[0],
                contact.getClientName(),
                contact.getClientPhone(),
                contact.getAdminName(),
                contact.getAdminPhone(),
                contact.getAdminGeo().getCoordinates()[1],
                contact.getAdminGeo().getCoordinates()[0],
                contact.getEstimateMessage(),
                contact.getEstimateTime(),
                contact.getContactTime()
        };
        getWritableDatabase().execSQL(SQLData.QUERY_INSERT_CONTACT, params);
    }

    /**
     * 로컬에 저장되어있는 계약서를 모두 불러오는 함수
     * @return
     */
    public ArrayList<ContactDTO> selectContact() {
        ArrayList<ContactDTO> arr = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery(SQLData.QUERY_SELECT_CONTACT, null);
        while (cursor.moveToNext()) {
            ContactDTO contact = new ContactDTO();
            contact.set_id(cursor.getString(0));
            contact.setAppTitle(cursor.getString(1));
            contact.setAppNumber(cursor.getInt(2));
            contact.setAppTime(cursor.getLong(3));
            contact.setAppStyle(cursor.getString(4));
            contact.setAppMenu(cursor.getString(5));
            GeoDTO geoApp = new GeoDTO();
            geoApp.setCoordinates(new double[] { cursor.getDouble(7), cursor.getDouble(6)});
            contact.setAppGeo(geoApp);
            contact.setClientName(cursor.getString(8));
            contact.setClientPhone(cursor.getString(9));
            contact.setAdminName(cursor.getString(10));
            contact.setAdminPhone(cursor.getString(11));
            GeoDTO geoContact = new GeoDTO();
            geoContact.setCoordinates(new double[] { cursor.getDouble(13), cursor.getDouble(12)});
            contact.setAdminGeo(geoContact);
            contact.setEstimateMessage(cursor.getString(14));
            contact.setEstimateTime(cursor.getLong(15));
            contact.setContactTime(cursor.getLong(16));
            arr.add(contact);
        }
        cursor.close();
        return arr;
    }

    /**
     * 새롭게 추가된 계약서만 저장하는 함수
     * 여러번 동시에 실행되는 것을 막아야 한다.
     * @param contacts
     */
    synchronized public void refreshContact(ArrayList<ContactDTO> contacts) {
        Cursor cursor = getReadableDatabase().rawQuery(SQLData.QUERY_SELECT_CONTACT_LASTEST_ID, null);
        String topId = null;
        if(cursor.moveToNext()) {
            topId = cursor.getString(0);
        }
        for(int i=0; i<contacts.size(); i++) {
            if(topId != null && contacts.get(i).get_id().equals(topId)) {
                break;
            }
            insertContact(contacts.get(i));
        }
        cursor.close();
    }

    /**
     * 계약 목록 비우기
     */
    public void removeContacts() {
        getWritableDatabase().execSQL(SQLData.QUERY_DELETE_CONTACTS);
    }

    /**
     * 근처 식당 목록 업데이트
     * @param nearAdmins
     */
    public void refreshNearAdmins(ArrayList<NearAdminDTO> nearAdmins) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQLData.QUERY_DELETE_NEAR_ADMINS);
        for(NearAdminDTO nearAdmin : nearAdmins) {
            db.execSQL(SQLData.QUERY_INSERT_NEAR_ADMINS, new Object[] {
                    nearAdmin.getAdminId(),
                    nearAdmin.getAdminType(),
                    nearAdmin.getName(),
                    nearAdmin.getDistance(),
                    nearAdmin.getStyle()
            });
        }
    }

    /**
     * 근처 식당 목록 불러오기
     * @return 근처 식당 목록
     */
    public ArrayList<NearAdminDTO> getNearAdmins() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SQLData.QUERY_SELECT_NEAR_ADMINS, null);
        ArrayList<NearAdminDTO> ret = new ArrayList<>();
        while(cursor.moveToNext()) {
            NearAdminDTO nearAdmin = new NearAdminDTO();
            nearAdmin.setAdminId(cursor.getString(0));
            nearAdmin.setAdminType(cursor.getInt(1));
            nearAdmin.setName(cursor.getString(2));
            nearAdmin.setDistance(cursor.getDouble(3));
            nearAdmin.setStyle(cursor.getString(4));
            ret.add(nearAdmin);
        }
        cursor.close();
        return ret;
    }

    /**
     * 이벤트 목록 최신화
     */
    public void refreshEvents(ArrayList<EventDTO> events) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQLData.QUERY_DELETE_EVENTS);
        for(EventDTO event : events) {
            db.execSQL(SQLData.QUERY_INSERT_EVENT, new Object[] {
                    event.getNumber(),
                    event.getTitle(),
                    event.getContent()
            });
        }
    }

    public ArrayList<EventDTO> getEvents() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SQLData.QUERY_SELECT_EVENTS, null);
        ArrayList<EventDTO> ret = new ArrayList<>();
        while(cursor.moveToNext()) {
            EventDTO event = new EventDTO();
            event.setNumber(cursor.getInt(0));
            event.setTitle(cursor.getString(1));
            event.setContent(cursor.getString(2));
            ret.add(event);
        }
        cursor.close();
        return ret;
    }

    /**
     * 근처 리뷰 목록 업데이트
     * @param reviews
     */
    public void refreshNearReviews(ArrayList<ReviewDTO> reviews) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQLData.QUERY_DELETE_NEAR_REVIEWS);
        for(ReviewDTO review : reviews) {
            db.execSQL(SQLData.QUERY_INSERT_NEAR_REVIEWS, new Object[] {
                    review.getWriter(),
                    review.getReceiver(),
                    review.getReceiverId(),
                    review.getReceiverType(),
                    review.getContent(),
                    review.getRating(),
                    review.getWritedTime()
            });
        }
    }

    /**
     * 근처 리뷰 목록 불러오기
     * @return 근처 리뷰 목록
     */
    public ArrayList<ReviewDTO> getNearReviews() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SQLData.QUERY_SELECT_NEAR_REVIEWS, null);
        ArrayList<ReviewDTO> ret = new ArrayList<>();
        while(cursor.moveToNext()) {
            ReviewDTO review = new ReviewDTO();
            review.setWriter(cursor.getString(0));
            review.setReceiver(cursor.getString(1));
            review.setReceiverId(cursor.getString(2));
            review.setReceiverType(cursor.getInt(3));
            review.setContent(cursor.getString(4));
            review.setRating(cursor.getFloat(5));
            review.setWritedTime(cursor.getLong(6));
            ret.add(review);
        }
        cursor.close();
        return ret;
    }

    /**
     * 새로운 식당 목록 업데이트
     * @param newAdmins 새로운 식당 목록
     */
    public void refreshNewAdmins(ArrayList<NewAdminDTO> newAdmins) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQLData.QUERY_DELETE_NEW_ADMINS);
        for(NewAdminDTO newAdmin : newAdmins) {
            db.execSQL(SQLData.QUERY_INSERT_NEW_ADMINS, new Object[] {
                    newAdmin.getId(),
                    newAdmin.getType(),
                    newAdmin.getName(),
                    newAdmin.getStyle()
            });
        }
    }

    /**
     * 새로운 식당 목록 불러오기
     * @return 새로운 식당 목록
     */
    public ArrayList<NewAdminDTO> getNewAdmins() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SQLData.QUERY_SELECT_NEW_ADMINS, null);
        ArrayList<NewAdminDTO> ret = new ArrayList<>();
        while(cursor.moveToNext()) {
            NewAdminDTO newAdmin = new NewAdminDTO();
            newAdmin.setId(cursor.getString(0));
            newAdmin.setType(cursor.getInt(1));
            newAdmin.setName(cursor.getString(2));
            newAdmin.setStyle(cursor.getString(3));
            ret.add(newAdmin);
        }
        cursor.close();
        return ret;
    }
}
