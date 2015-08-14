package com.example.truecallertest.listeners;

/**
 * Created by salil on 13/8/15.
 */
public interface ResponseListener<T> {

    void onResponse(T t);

    void onError(com.example.truecallertest.models.Error error);
}
