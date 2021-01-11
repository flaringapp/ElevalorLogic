package com.flaringapp.utils.observable;

public interface Observer<T> {

    void onNewValue(T value);

}
