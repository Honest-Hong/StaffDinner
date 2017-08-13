package com.project.boostcamp.publiclibrary.data;

/**
 * Created by Hong Tae Joon on 2017-08-10.
 * startActivityForResult 또는 onActivityResult에서 사용하는 requestCode를 모아둔다.
 */

public class RequestType {
    public static final int REQUEST_LOCATION = 0x100;
    public static final int REQUEST_CAMERA = 0x101;
    public static final int REQUEST_PICUTRE = 0x102;
    public static final int REQUEST_READ_PERMISSION = 0x103;
    public static final int REQUEST_PERMISSIONS = 0x104;
    public static final int REQUEST_CONTACT = 0x105;
    public static final int REQUEST_REVIEW = 0x106;
}
