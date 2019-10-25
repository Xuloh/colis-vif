package fr.insa.colisvif.model;

import org.junit.Test;

public class DeliveryMapTest {

    @Test
    public void testCreateDeliveryGood() {
        DeliveryMap deliveryMap = new DeliveryMap();
        deliveryMap.createDelivery(100,101,10,10);
    }

    @Test
    public void testCreateWarehouseGood() {
        DeliveryMap deliveryMap = new DeliveryMap();
        deliveryMap.createWarehouse(101,100);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWarehouseNegativeTime() {
        DeliveryMap deliveryMap = new DeliveryMap();
        deliveryMap.createWarehouse(101,-100);

    }

    @Test
    public void testCreateWarehouseZeroTime() {
        DeliveryMap deliveryMap = new DeliveryMap();
        deliveryMap.createWarehouse(101,0);

    }

}
