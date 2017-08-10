package com.project.boostcamp.publiclibrary.data;

/**
 * Created by Hong Tae Joon on 2017-08-03.
 * 신청서의 상태 종류 클래스
 * 0 : 작성중
 * 1 : 신청함
 * 2 : 취소됨
 */

public class ApplicationStateType {
    public static final int STATE_EDITING = 0x0;
    public static final int STATE_APPLIED = 0x1;
    public static final int STATE_CANCELED = 0x2;
}
