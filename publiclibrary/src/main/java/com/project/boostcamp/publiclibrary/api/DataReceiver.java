package com.project.boostcamp.publiclibrary.api;

/**
 * Created by Hong Tae Joon on 2017-08-04.
 */

public interface DataReceiver<T> {
    void onReceive(T data);
    void onFail();
}
