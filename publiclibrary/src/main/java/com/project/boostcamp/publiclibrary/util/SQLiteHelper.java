package com.project.boostcamp.publiclibrary.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.HandlerThread;
import android.util.Log;

import com.project.boostcamp.publiclibrary.domain.ContactDTO;
import com.project.boostcamp.publiclibrary.domain.GeoDTO;

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
        db.execSQL(SQLData.QUERY_CREATE_CONTACT_TABLE);
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
        Cursor cursor = getReadableDatabase().rawQuery(SQLData.QUERY_SELECT_CONTACT_ID, null);
        String topId = null;
        while(cursor.moveToNext()) {
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
}
