package com.coupang.samples.oop.hw.order;

import com.coupang.samples.oop.hw.order.record.OrderRecord;

import java.util.List;

/**
 * Created by coupang on 2016. 1. 2..
 *
 * Temporary buffer stores newly arrived orders.
 * Orders stored here are neither searched nor persisted.
 *
 */
public interface OrderReceiver {
    OrderRecord receiveOrder(long memberId, List<Long> productId);
    void flushOrders();
}
