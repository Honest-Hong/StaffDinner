package com.project.boostcamp.publiclibrary.dialog;

/**
 * Created by Hong Tae Joon on 2017-07-26.
 * 다이얼로그 버튼 클릭 결과 리스너
 * 예
 * 아니오
 */

public interface DialogResultListener {
    void onPositive();
    void onNegative();
}
