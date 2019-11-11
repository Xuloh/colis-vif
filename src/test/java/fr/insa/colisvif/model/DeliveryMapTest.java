package fr.insa.colisvif.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


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

    @Test
    public void equals1() {
        DeliveryMap deliveryMap = new DeliveryMap();
        assertEquals(deliveryMap, deliveryMap);
    }

    @Test
    public void equals2() {
        DeliveryMap deliveryMap1 = new DeliveryMap();
        deliveryMap1.createWarehouse(1, 10);
        deliveryMap1.createDelivery(1, 10, 11, 11, 10);
        DeliveryMap deliveryMap2 = new DeliveryMap();
        deliveryMap2.createWarehouse(1, 10);
        deliveryMap2.createDelivery(1, 10, 11, 11, 10);
        assertEquals(deliveryMap1, deliveryMap2);
    }

    @Test
    public void equals3() {
        DeliveryMap deliveryMap1 = new DeliveryMap();
        deliveryMap1.createWarehouse(1, 10);
        deliveryMap1.createDelivery(1, 10, 11, 10, 10);
        DeliveryMap deliveryMap2 = new DeliveryMap();
        deliveryMap2.createWarehouse(1, 10);
        deliveryMap2.createDelivery(2, 10, 11, 10, 10);
        assertNotEquals(deliveryMap1, deliveryMap2);
    }

    @Test
    public void equals4() {
        DeliveryMap deliveryMap1 = new DeliveryMap();
        deliveryMap1.createWarehouse(1, 10);
        deliveryMap1.createDelivery(1, 10, 11, 10, 10);
        assertNotEquals(deliveryMap1, 1);
    }

    @Test
    public void getSize() {
        DeliveryMap deliveryMap1 = new DeliveryMap();
        deliveryMap1.createWarehouse(1, 10);
        deliveryMap1.createDelivery(1, 10, 11, 10, 10);
        deliveryMap1.createDelivery(1, 10, 11, 10, 10);
        assertEquals(deliveryMap1.getSize(), 2);
    }

    @Test
    public void getDelivery1() {
        Delivery expected = new Delivery(1, 10, 11, 10, 10);
        DeliveryMap deliveryMap1 = new DeliveryMap();
        deliveryMap1.createDelivery(expected.getId(), expected.getPickUpNodeId(), expected.getDropOffNodeId(), expected.getPickUpDuration(), expected.getDropOffDuration());
        assertEquals(deliveryMap1.getDelivery(0), expected);
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void getDelivery2() {
        DeliveryMap deliveryMap1 = new DeliveryMap();
        deliveryMap1.createDelivery(1, 10, 11, 10, 10);
        deliveryMap1.getDelivery(2);
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void getDelivery3() {
        DeliveryMap deliveryMap1 = new DeliveryMap();
        deliveryMap1.createDelivery(1, 10, 11, 10, 10);
        deliveryMap1.getDelivery(-1);
    }

    @Test
    public void toString1() {
        DeliveryMap deliveryMap1 = new DeliveryMap();
        deliveryMap1.createWarehouse(1, 2);
        deliveryMap1.createDelivery(3, 4, 5, 6, 7);
        String expected = "DeliveryMap{"
                + "deliveryRequests="
                + "[id : " + 3 + "pickUpNodeId : " + 4 + " | deliveryNodeId : " + 5 + " | pickUpDuration : "
                + 6 + " | deliveryDuration : " + 7 + "\n]"
                + ", warehouseNodeId="
                + 1
                + ", startDateInSeconds="
                + 2
                + '}';
        assertEquals(deliveryMap1.toString(), expected);
    }
}
