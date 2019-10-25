package fr.insa.colisvif.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

    /*
    public void createWarehouse(long positionId, int startDateInSeconds){
        this.warehouseNodeId = positionId;
        this.startDateInSeconds = startDateInSeconds;
    }
     */


}
