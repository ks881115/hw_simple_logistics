package com.coupang.samples.oop.hw.order;

import com.coupang.samples.oop.hw.order.record.OrderRecord;
import com.coupang.samples.oop.hw.queue.Queue;

import java.util.Date;
import java.util.List;

/**
 * Created by coupang on 2016. 1. 2..
 */
public class OrderReceiverImpl implements OrderReceiver {
    private Queue<OrderRecord> orderQueue;
    private OrderManager orderManager;

    private long orderIdSeq = 0;

    private static boolean DEFAULT_IS_DELIVERED = false;

    public OrderReceiverImpl(Queue<OrderRecord> queue, OrderManager manager) {
        this.orderQueue = queue;
        this.orderManager = manager;
    }

    @Override
    public OrderRecord receiveOrder(long memberId, List<Long> productIds) {
        OrderRecord newOrder = new OrderRecord(
                orderIdSeq++,               // Sequence long number is given as order IDs
                memberId,
                productIds,
                new Date(),                 // Current date-time
                DEFAULT_IS_DELIVERED
        );
        orderQueue.enqueue(newOrder);

        return newOrder;
    }

    @Override
    public void flushOrders() {
        List<OrderRecord> orders = orderQueue.toList();
        orderManager.saveOrders(orders);
        orderQueue.clear();
    }
}
