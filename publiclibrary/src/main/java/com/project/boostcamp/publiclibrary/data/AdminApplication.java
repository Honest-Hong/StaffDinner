package com.project.boostcamp.publiclibrary.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Hong Tae Joon on 2017-08-03.
 * 고객이 사용하는 신청서 클래스
 */

public class AdminApplication implements Parcelable{

    // 신청서 id
    private String id;
    // 작성자 이름
    private String writerName;
    // 신청서 제목
    private String title;
    // 회식 인원
    private int number;
    // 회식 시간
    private long time;
    // 원하는 식당 분위기
    private String style;
    // 원하는 식당 메뉴
    private String menu;
    // 원하는 근처 지역
    private Geo geo;
    // 식당과의 거리
    private double distance;
    // 작성 시간
    private long writedTime;

    public AdminApplication() {
    }

    protected AdminApplication(Parcel in) {
        id = in.readString();
        writerName = in.readString();
        title = in.readString();
        number = in.readInt();
        time = in.readLong();
        style = in.readString();
        menu = in.readString();
        geo = (Geo)in.readSerializable();
        distance = in.readDouble();
        writedTime = in.readLong();
    }

    public static final Creator<AdminApplication> CREATOR = new Creator<AdminApplication>() {
        @Override
        public AdminApplication createFromParcel(Parcel in) {
            return new AdminApplication(in);
        }

        @Override
        public AdminApplication[] newArray(int size) {
            return new AdminApplication[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(writerName);
        parcel.writeString(title);
        parcel.writeInt(number);
        parcel.writeLong(time);
        parcel.writeString(style);
        parcel.writeString(menu);
        parcel.writeSerializable(geo);
        parcel.writeDouble(distance);
        parcel.writeLong(writedTime);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getWritedTime() {
        return writedTime;
    }

    public void setWritedTime(long writedTime) {
        this.writedTime = writedTime;
    }

}
