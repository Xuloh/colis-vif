package fr.insa.colisvif.model;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class DeliveryMapTest {

    @Test
    public void testCreateDeliveryGood() {
        DeliveryMap deliveryMap = new DeliveryMap();
        deliveryMap.createDelivery(100,101,10,10);
    }

    @Test
    public void testCreateDelivery() {
        DeliveryMap deliveryMap = new DeliveryMap();
        deliveryMap.createDelivery(100,101,10,10);
        Delivery delivery = new Delivery(100,101,10,10);
        assertEquals(delivery,deliveryMap.getDeliveryList().get(0));
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

    @Test
    public void getDeliveryList() {
        DeliveryMap deliveryMap= new DeliveryMap();
        deliveryMap.createDelivery(101,100,15,10);
        deliveryMap.createDelivery(105,104,10,20);

        List<Delivery> deliveries = new ArrayList<>();
        deliveries.add(new Delivery(101,100,15,10));
        deliveries.add(new Delivery(105,104,10,20));

        assertEquals(deliveries,deliveryMap.getDeliveryList());
    }

    @Test
    public void getWarehouseNodeId() {
        DeliveryMap deliveryMap= new DeliveryMap();
        deliveryMap.createWarehouse(101,0);
        assertEquals(101,deliveryMap.getWarehouseNodeId());
    }

    @Test
    public void setWarehouseNodeId() {
        DeliveryMap deliveryMap= new DeliveryMap();
        deliveryMap.createWarehouse(101,0);
        deliveryMap.setWarehouseNodeId(102);
        assertEquals(102,deliveryMap.getWarehouseNodeId());
    }

    @Test
    public void getStartDateInSeconds() {
        DeliveryMap deliveryMap= new DeliveryMap();
        deliveryMap.createWarehouse(101,10);
        assertEquals(10,deliveryMap.getStartDateInSeconds());

    }

    @Test
    public void setStartDateInSeconds() {
        DeliveryMap deliveryMap= new DeliveryMap();
        deliveryMap.createWarehouse(101,10);
        deliveryMap.setStartDateInSeconds(100);
        assertEquals(100,deliveryMap.getStartDateInSeconds());
    }

    @Test
    public void createImpossibleDelivery() {
        DeliveryMap deliveryMap = new DeliveryMap();
        deliveryMap.createImpossibleDelivery(100,101,10,10);
        Delivery delivery = new Delivery(100,101,10,10);
        assertEquals(delivery,deliveryMap.getImpossibleDeliveries().get(0));

    }

    @Test
    public void getImpossibleDeliveries() {
        DeliveryMap deliveryMap= new DeliveryMap();
        deliveryMap.createImpossibleDelivery(101,100,15,10);
        deliveryMap.createImpossibleDelivery(105,104,10,20);

        List<Delivery> deliveries = new ArrayList<>();
        deliveries.add(new Delivery(101,100,15,10));
        deliveries.add(new Delivery(105,104,10,20));

        assertEquals(deliveries,deliveryMap.getImpossibleDeliveries());
    }
}
