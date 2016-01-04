package com.coupang.samples.oop.hw.order.persistence;

import com.coupang.samples.oop.hw.order.record.OrderProfile;
import com.coupang.samples.oop.hw.order.record.OrderRecord;

import java.io.*;
import java.util.*;

/**
 * Created by coupang on 2016. 1. 3..
 */
public class FilePersistenceLayer implements PersistenceLayer {
    private static final String CSV_FIELD_SEP = ",";
    private static final String CSV_LINE_SEP = "\n";
    private static final int SIZE_LONG = 8;

    private BufferedWriter writerProfiles;
    private DataOutputStream writerProducts;

    private RandomAccessFile rafProfiles;
    private RandomAccessFile rafProducts;

    // An in-memory index maps an orderId to its corresponding filePosition
    private Map<Long, Long> orderProfileIndex;

    private long curPosProfiles = 0;
    private long curPosProducts = 0;

    public FilePersistenceLayer(String profilePath, String productsPath) {
        try {
            writerProfiles = new BufferedWriter(new FileWriter(profilePath, true));
            writerProducts = new DataOutputStream(new FileOutputStream(productsPath, true));

            rafProfiles = new RandomAccessFile(profilePath, "rwd");
            rafProducts = new RandomAccessFile(productsPath, "rwd");

            // Initialize curPosProducts;
            BufferedReader readerProfiles = new BufferedReader(new FileReader(profilePath));
            buildIndex(readerProfiles);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeOrder(OrderRecord order) {
        try {
            writeSingleOrder(order);
            writerProfiles.flush();
            writerProducts.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeOrders(List<OrderRecord> orders) {
        try {
            for(OrderRecord order : orders) {
                writeSingleOrder(order);
            }
            writerProfiles.flush();
            writerProducts.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public OrderRecord findOrderByOrderId(long orderId) {
        OrderRecord orderRecord = null;

        Long posOrderProfile = orderProfileIndex.get(orderId);

        if(posOrderProfile != null) {
            try {
                rafProfiles.seek(posOrderProfile);
                String line = rafProfiles.readLine();
                ProfileFileRecord fileRecord = new ProfileFileRecord(line);

                long posProducts = fileRecord.getPosProducts();
                rafProducts.seek(posProducts * SIZE_LONG);

                List<Long> productIds = new ArrayList<Long>();
                int numProducts = fileRecord.getNumProducts();
                for (int i = 0; i < numProducts; i++) {
                    productIds.add(rafProducts.readLong());
                }

                orderRecord = new OrderRecord(fileRecord.getOrderId(), fileRecord.getMemberId(), productIds,
                        new Date(fileRecord.getRegDttm()), fileRecord.getIsDelivered() == 'T' ? true : false);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return orderRecord;
    }

    @Override
    public List<OrderProfile> listOrderProfiles(int limit) {
        List<OrderProfile> orderProfiles = new ArrayList<OrderProfile>();
        try {
            rafProfiles.seek(0);
            String line = null;
            int i = 0;
            while((line = rafProfiles.readLine()) != null && i++ < limit) {
                ProfileFileRecord fileRecord = new ProfileFileRecord(line);
                orderProfiles.add(new OrderProfile(fileRecord.getOrderId(), fileRecord.getMemberId(),
                        new Date(fileRecord.getRegDttm()), fileRecord.getIsDelivered() == 'T' ? true : false));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return orderProfiles;
    }

    @Override
    public boolean updateLogisticInfo(long orderId) {
        Long posOrderProfile = orderProfileIndex.get(orderId);
        if(posOrderProfile != null) {
            try {
                rafProfiles.seek(posOrderProfile);
                String line = rafProfiles.readLine();
                ProfileFileRecord fileRecord = new ProfileFileRecord(line);

                // Set isDelivered 'true'
                fileRecord.setIsDeliveredTrue();

                // Rewrite the updated record
                rafProfiles.seek(posOrderProfile);
                rafProfiles.writeBytes(fileRecord.toCsvString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }

        return false;
    }

    @Override
    public void clear() {
        try {
            writerProducts.close();
            writerProfiles.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeSingleOrder(OrderRecord order) throws IOException {
        List<Long> productIds = order.getProductIds();
        int numProducts = productIds.size();
        ProfileFileRecord fileRecord = new ProfileFileRecord(
                order.getOrderId(), order.getMemberId(), order.getRegDttm().getTime(),
                curPosProducts, numProducts, order.isDelivered() ? 'T' : 'F'
        );

        String csvLineString = fileRecord.toCsvString();
        writerProfiles.write(csvLineString);
        for(Long productId : productIds) {
            writerProducts.writeLong(productId);
        }

        orderProfileIndex.put(order.getOrderId(), curPosProfiles);
        curPosProfiles += csvLineString.length();
        curPosProducts += numProducts;
    }

    private void buildIndex(BufferedReader readerProfiles) throws IOException {
        orderProfileIndex = new TreeMap<Long, Long>();

        long curPos = 0;
        String line = null;
        while((line = readerProfiles.readLine()) != null) {
            // Parse into order profile
            ProfileFileRecord record = new ProfileFileRecord(line);

            // add index attribute
            orderProfileIndex.put(record.getOrderId(), curPos);

            curPos += line.length() + 1; // plus 1 for line feed
        }
        curPosProfiles = curPos;
    }

    private class ProfileFileRecord {
        private long orderId;
        private long memberId;
        private long regDttm;
        private long posProducts;
        private int numProducts;
        private char isDelivered;

        public ProfileFileRecord(long orderId, long memberId, long regDttm, long posProducts, int numProducts, char isDelivered) {
            this.orderId = orderId;
            this.memberId = memberId;
            this.regDttm = regDttm;
            this.posProducts = posProducts;
            this.numProducts = numProducts;
            this.isDelivered = isDelivered;
        }

        public ProfileFileRecord(String line) {
            String [] tokens = line.split(CSV_FIELD_SEP);
            this.orderId = Long.parseLong(tokens[0]);
            this.memberId = Long.parseLong(tokens[1]);
            this.regDttm = Long.parseLong(tokens[2]);
            this.posProducts = Long.parseLong(tokens[3]);
            this.numProducts = Integer.parseInt(tokens[4]);
            this.isDelivered = tokens[5].charAt(0);
        }

        public String toCsvString() {
            return orderId + CSV_FIELD_SEP + memberId + CSV_FIELD_SEP + regDttm + CSV_FIELD_SEP + posProducts
                    + CSV_FIELD_SEP + numProducts + CSV_FIELD_SEP + isDelivered + CSV_LINE_SEP;
        }

        public int getIsDeliveredOffset() {
            return (orderId + CSV_FIELD_SEP + memberId + CSV_FIELD_SEP + regDttm + CSV_FIELD_SEP + posProducts
                    + CSV_FIELD_SEP + numProducts + CSV_FIELD_SEP).length();
        }

        public long getOrderId() {
            return orderId;
        }

        public long getMemberId() {
            return memberId;
        }

        public long getRegDttm() {
            return regDttm;
        }

        public long getPosProducts() {
            return posProducts;
        }

        public int getNumProducts() {
            return numProducts;
        }

        public char getIsDelivered() {
            return isDelivered;
        }

        public void setIsDeliveredTrue() {
            this.isDelivered = 'T';
        }
    }
}
