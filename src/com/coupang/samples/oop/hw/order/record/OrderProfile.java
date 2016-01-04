package com.coupang.samples.oop.hw.order.record;

import java.util.Date;

/**
 * Created by coupang on 2016. 1. 3..
 */
public class OrderProfile {
    private long orderId;
    private long memberId;
    private Date regDttm;
    private boolean isDelivered;

    public OrderProfile(long orderId, long memberId, Date regDttm, boolean isDelivered) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.regDttm = regDttm;
        this.isDelivered = isDelivered;
    }

    public String toString() {
        return orderId + ", " + memberId + ", " + regDttm + ", " + isDelivered;
    }
}
