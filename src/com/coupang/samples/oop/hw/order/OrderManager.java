package com.coupang.samples.oop.hw.order;

import com.coupang.samples.oop.hw.order.record.OrderProfile;
import com.coupang.samples.oop.hw.order.record.OrderRecord;

import java.util.List;

/**
 * Created by coupang on 2016. 1. 3..
 *
 *
 *
 */
public interface OrderManager {
    void saveOrder(OrderRecord order);
    void saveOrders(List<OrderRecord> orders);
    OrderRecord findOrder(long orderId);
    List<OrderProfile> listOrderProfiles(int limit);
    boolean logoOrder(long orderId);
}