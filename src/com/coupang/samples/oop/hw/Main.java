package com.coupang.samples.oop.hw;

import com.coupang.samples.oop.hw.order.OrderManager;
import com.coupang.samples.oop.hw.order.OrderManagerImpl;
import com.coupang.samples.oop.hw.order.OrderReceiver;
import com.coupang.samples.oop.hw.order.OrderReceiverImpl;
import com.coupang.samples.oop.hw.order.persistence.FilePersistenceLayer;
import com.coupang.samples.oop.hw.order.persistence.PersistenceLayer;
import com.coupang.samples.oop.hw.order.record.OrderProfile;
import com.coupang.samples.oop.hw.order.record.OrderRecord;
import com.coupang.samples.oop.hw.queue.ResizingArrayQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by coupang on 2015. 12. 28..
 */
public class Main {
    public static final String FILEPATH_ORDER_PROFILES = "order_profiles.txt";
    public static final String FILEPATH_PRODUCTS = "products.txt";

    public static void printMenu() {
        System.out.println("<< Simple logistic system >>");
        System.out.println(
                "1. Enqueue order \n" +
                "2. Dequeue orders\n" +
                "3. Find order\n" +
                "4. List orders\n" +
                "5. Logo order\n" +
                "6. exit"
        );
    }

    public static void enqueueOrder(Scanner scanner, OrderReceiver orderReceiver) {
        System.out.println("고객ID(Long type)를 입력해 주세요:");
        System.out.print(">>");
        long memberId = scanner.nextLong();

        List<Long> productIds = new ArrayList<Long>();
        long productId = 0;
        while(true) {
            System.out.println("제품ID(Long type)를 입력해 주세요: (음수 입력시, escape)");
            System.out.print(">>");
            productId = scanner.nextLong();

            if(productId < 0) break;

            productIds.add(productId);
        }

        OrderRecord receivedOrder = orderReceiver.receiveOrder(memberId, productIds);
        System.out.println("주문ID: " + receivedOrder.getOrderId() + " 접수 완료.");
    }

    public static void dequeueOrders(OrderReceiver orderReceiver) {
        System.out.println("접수된 주문들을 모두 저장합니다:");
        orderReceiver.flushOrders();
        System.out.println("저장 완료");
    }

    public static void findOrder(Scanner scanner, OrderManager orderManager) {
        System.out.println("주문ID(Long type)를 입력해 주세요:");
        System.out.print(">>");
        long orderId = scanner.nextLong();

        OrderRecord foundOrder = orderManager.findOrder(orderId);
        if(null != foundOrder) {
            System.out.println(foundOrder);
        }
        else {
            System.out.println("주문ID=" + orderId + " 인 주문을 찾지 못했습니다.");
        }
    }

    public static void listOrders(OrderManager orderManager) {
        // List maximum 100 orders
        List<OrderProfile> orderProfiles = orderManager.listOrderProfiles(100);
        System.out.println("\nOrders list::");
        for(OrderProfile profile : orderProfiles) {
            System.out.println(profile);
        }
        System.out.println();
    }

    public static void logoOrder(Scanner scanner, OrderManager orderManager) {
        System.out.println("처리할 주문ID(Long type)를 입력해 주세요:");
        System.out.print(">>");
        long orderId = scanner.nextLong();

        if(orderManager.logoOrder(orderId)) {
            System.out.println("주문ID=" + orderId + " 인 주문 처리 완료.");
        }
        else {
            System.out.println("주문ID=" + orderId + " 인 주문을 찾지 못했습니다.");
        }
    }

    public static void main(String [] args) {
        Scanner scanner = new Scanner(System.in);

        // Dependency injection
        PersistenceLayer persistenceLayer = new FilePersistenceLayer(FILEPATH_ORDER_PROFILES, FILEPATH_PRODUCTS);
        OrderManager orderManager = new OrderManagerImpl(persistenceLayer);
        ResizingArrayQueue<OrderRecord> orderQueue = new ResizingArrayQueue<OrderRecord>();
        OrderReceiver orderReceiver = new OrderReceiverImpl(orderQueue, orderManager);

        int menu = 0;
        while(menu != 6) {
            printMenu();
            System.out.print(">>");
            menu = scanner.nextInt();

            switch(menu) {
                case 1:
                    enqueueOrder(scanner, orderReceiver);
                    break;
                case 2:
                    dequeueOrders(orderReceiver);
                    break;
                case 3:
                    findOrder(scanner, orderManager);
                    break;
                case 4:
                    listOrders(orderManager);
                    break;
                case 5:
                    logoOrder(scanner, orderManager);
                    break;
                case 6:
                    System.out.println("Exit the program ...");
                    break;
                default:
                    System.out.println("[ERROR] Wrong command!!");
            }
        }

        persistenceLayer.clear();
    }
}