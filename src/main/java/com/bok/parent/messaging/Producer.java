package com.bok.parent.messaging;

public interface Producer<T> {
    void produce(T t);
}
