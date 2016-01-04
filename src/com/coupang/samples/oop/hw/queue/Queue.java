package com.coupang.samples.oop.hw.queue;

import java.util.List;

/**
 * Created by coupang on 2016. 1. 2..
 */
public interface Queue <T> {
    void enqueue(T input);
    T dequeue() throws EmptyQueueException;
    int size();
    boolean isEmpty();
    List<T> toList();
    void clear();
}