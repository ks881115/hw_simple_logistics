package com.coupang.samples.oop.hw.order;

import com.coupang.samples.oop.hw.order.persistence.PersistenceLayer;
import com.coupang.samples.oop.hw.order.record.OrderProfile;
import com.coupang.samples.oop.hw.order.record.OrderRecord;

import java.util.List;

/**
 * Created by coupang on 2016. 1. 3..
 */
public class OrderManagerImpl implements OrderManager {
    private PersistenceLayer persistenceLayer;

    public OrderManagerImpl(PersistenceLayer persistenceLayer) {
        this.persistenceLayer = persistenceLayer;
    }

    @Override
    public void saveOrder(OrderRecord order) {
        persistenceLayer.writeOrder(order);
    }

    @Override
    public void saveOrders(List<OrderRecord> orders) {
        persistenceLayer.writeOrders(orders);
    }

    @Override
    public OrderRecord findOrder(long orderId) {
        // Handle any exception here?
        return persistenceLayer.findOrderByOrderId(orderId);
    }

    @Override
    public List<OrderProfile> listOrderProfiles(int limit) {
        // Handle any exception here?
        return persistenceLayer.listOrderProfiles(limit);
    }

    @Override
    public boolean logoOrder(long orderId) {
        return persistenceLayer.updateLogisticInfo(orderId);
    }
}
