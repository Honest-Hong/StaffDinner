package com.project.boostcamp.publiclibrary.data;

/**
 * Created by Hong Tae Joon on 2017-08-04.
 */

public interface DataEvent<T> {
    void onClick(T data);
}
