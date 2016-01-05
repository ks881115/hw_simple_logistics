package com.coupang.samples.oop.hw.order.persistence;

import com.coupang.samples.oop.hw.order.record.OrderProfile;
import com.coupang.samples.oop.hw.order.record.OrderRecord;

import java.util.List;

/**
 * Created by coupang on 2016. 1. 2..
 *
 * 주문 정보를 persistence layer에 저장하고, 저장된 정보를 조회할 수 있게 해준다.
 * DB 또는 파일로 구현될 수 있다 본 과제에서는 파일을 이용해 구현하였다.
 *
 */
public interface PersistenceLayer {
    void writeOrder(OrderRecord order);
    void writeOrders(List<OrderRecord> orders);
    OrderRecord findOrderByOrderId(long orderId);
    List<OrderProfile> listOrderProfiles(int limit);
    boolean updateLogisticInfo(long orderId);
    void clear();
}