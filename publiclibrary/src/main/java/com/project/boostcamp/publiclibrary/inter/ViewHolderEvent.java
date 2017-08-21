package com.project.boostcamp.publiclibrary.inter;

/**
 * Created by Hong Tae Joon on 2017-08-21.
 */

public interface ViewHolderEvent<T> {
    void onClick(T data, int position);
}
