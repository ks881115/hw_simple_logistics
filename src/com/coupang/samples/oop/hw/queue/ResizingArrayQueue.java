package com.coupang.samples.oop.hw.queue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coupang on 2016. 1. 2..
 */
public class ResizingArrayQueue<T> implements Queue <T> {
    private static int DEFAULT_CAPACITY = 10;

    private Object [] arr;
    private int capacity = DEFAULT_CAPACITY;
    private int head;
    private int tail;

    public ResizingArrayQueue() {
        initialize();
    }

    @Override
    public void enqueue(T input) {
        if(isHalfFull()) {
            doubleSize();
        }
        arr[++tail] = input;
    }

    @Override
    public T dequeue() throws EmptyQueueException {
        if(isEmpty()) {
            throw new EmptyQueueException();
        }
        if(isHalfWasted()) {
            halfSize();
        }
        return (T) arr[head++];
    }

    @Override
    public int size() {
        return (tail + 1) - head;
    }

    @Override
    public boolean isEmpty() {
        return head == (tail + 1);
    }

    @Override
    public List<T> toList() {
        List<T> list = new ArrayList<T>();
        for(int i = head; i <= tail; i++) {
            list.add((T) arr[i]);
        }
        return list;
    }

    @Override
    public void clear() {
        capacity = DEFAULT_CAPACITY;
        initialize();
    }

    private void initialize() {
        arr = new Object [capacity];
        head = 0;
        tail = -1;
    }

    private boolean isHalfFull() {
        int threshold = capacity / 2;
        return !isEmpty() && threshold < size();
    }

    private boolean isHalfWasted() {
        int threshold = capacity / 2;
        return !isEmpty() && DEFAULT_CAPACITY < capacity &&
                (threshold < head || size() < threshold);
    }

    private void doubleSize() {
        int newCapacity = capacity * 2;
        createNewArr(newCapacity);
    }

    private void halfSize() {
        int newCapacity = capacity / 2;
        createNewArr(newCapacity);
    }

    private void createNewArr(int newCapacity) {
        Object [] newArr = new Object[newCapacity];

        int size = size();
        for(int i = 0; i < size; i++) {
            newArr[i] = arr[head + i];
        }
        arr = newArr;
        capacity = newCapacity;
        head = 0;
        tail = size - 1;
    }
}