package com.project.boostcamp.publiclibrary.data;

/**
 * Created by Hong Tae Joon on 2017-08-08.
 * 견적서 상태 종류 모음
 * 0 : 대기중
 * 1 : 계약됨
 * 2 : 취소됨
 */

public class EstimateStateType {
    public static final int STATE_WATING = 0x0;
    public static final int STATE_CONTACTED = 0x1;
    public static final int STATE_CANCELED = 0x2;
}
