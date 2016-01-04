package com.coupang.samples.oop.hw.order.persistence;

import com.coupang.samples.oop.hw.order.record.OrderProfile;
import com.coupang.samples.oop.hw.order.record.OrderRecord;

import java.util.List;

/**
 * Created by coupang on 2016. 1. 2..
 */
public interface PersistenceLayer {
    void writeOrder(OrderRecord order);
    void writeOrders(List<OrderRecord> orders);
    OrderRecord findOrderByOrderId(long orderId);
    List<OrderProfile> listOrderProfiles(int limit);
    boolean updateLogisticInfo(long orderId);
    void clear();
}