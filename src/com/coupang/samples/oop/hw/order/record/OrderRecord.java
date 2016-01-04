package com.coupang.samples.oop.hw.order.record;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by coupang on 2016. 1. 2..
 */
public class OrderRecord {
    private long orderId;
    private long memberId;
    private List<Long> productIds;
    private Date regDttm;
    private boolean isDelivered;

    public OrderRecord(long orderId, long memberId, List<Long> productIds, Date regDttm, boolean isDelivered) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.productIds = productIds;
        this.regDttm = regDttm;
        this.isDelivered = isDelivered;
    }

    public OrderProfile toOrderProfile() {
        return new OrderProfile(orderId, memberId, regDttm, isDelivered);
    }

    public long getOrderId() {
        return orderId;
    }

    public long getMemberId() {
        return memberId;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public Date getRegDttm() {
        return regDttm;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public String toString() {
        return orderId + ", " + memberId + ", " + Arrays.toString(productIds.toArray()) + ", " + regDttm + ", " + isDelivered;
    }
}