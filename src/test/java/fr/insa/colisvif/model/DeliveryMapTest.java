package fr.insa.colisvif.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DeliveryMapTest {

    @Test
    public void testCreateDeliveryGood() {
        DeliveryMap deliveryMap = new DeliveryMap();
        deliveryMap.createDelivery(1, 100, 101, 10, 10);
    }

    @Test
    public void testCreateDelivery() {
        DeliveryMap deliveryMap = new DeliveryMap();
        deliveryMap.createDelivery(1, 100, 101, 10, 10);
        Delivery delivery = new Delivery(1, 100, 101, 10, 10);
        assertEquals(delivery, deliveryMap.getDeliveryList().get(0));
    }

    @Test
    public void testCreateWarehouseGood() {
        DeliveryMap deliveryMap = new DeliveryMap();
        deliveryMap.createWarehouse(101, 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWarehouseNegativeTime() {
        DeliveryMap deliveryMap = new DeliveryMap();
        deliveryMap.createWarehouse(101, -100);

    }

    @Test
    public void testCreateWarehouseZeroTime() {
        DeliveryMap deliveryMap = new DeliveryMap();
        deliveryMap.createWarehouse(101, 0);

    }

    @Test
    public void getDeliveryList() {
        DeliveryMap deliveryMap = new DeliveryMap();
        deliveryMap.createDelivery(1, 101, 100, 15, 10);
        deliveryMap.createDelivery(2, 105, 104, 10, 20);

        List<Delivery> deliveries = new ArrayList<>();
        deliveries.add(new Delivery(1, 101, 100, 15, 10));
        deliveries.add(new Delivery(2, 105, 104, 10, 20));

        assertEquals(deliveries, deliveryMap.getDeliveryList());
    }

    @Test
    public void getWarehouseNodeId() {
        DeliveryMap deliveryMap = new DeliveryMap();
        deliveryMap.createWarehouse(101, 0);
        assertEquals(101, deliveryMap.getWarehouseNodeId());
    }

    @Test
    public void getStartDateInSeconds() {
        DeliveryMap deliveryMap = new DeliveryMap();
        deliveryMap.createWarehouse(101, 10);
        assertEquals(10, deliveryMap.getStartDateInSeconds());

    }
}
