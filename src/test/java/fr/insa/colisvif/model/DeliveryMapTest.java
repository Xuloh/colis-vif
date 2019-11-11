package fr.insa.colisvif.model;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class DeliveryMapTest {
    public static <T> Object getAttribute(Class<T> clazz, T targetObject, String attributeName) {
        try {
            Field field = clazz.getDeclaredField(attributeName);
            field.setAccessible(true);
            return field.get(targetObject);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> void setAttrbiute(Class<T> clazz, T targetObject, String attributeName, Object value) {
        try {
            Field field = clazz.getDeclaredField(attributeName);
            field.setAccessible(true);
            field.set(targetObject, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testCreateDeliveryGood() {
        DeliveryMap deliveryMap = new DeliveryMap();
        deliveryMap.createDelivery(100, 101, 10, 10);
    }

    @Test
    public void testCreateDelivery() {
        DeliveryMap deliveryMap = new DeliveryMap();
        deliveryMap.createDelivery(100, 101, 10, 10);
        int id = (int) getAttribute(DeliveryMap.class, deliveryMap, "cptId");
        Delivery delivery = new Delivery(id, 100, 101, 10, 10);
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
        deliveryMap.createDelivery(101, 100, 15, 10);
        int cptId1 = (int) getAttribute(DeliveryMap.class, deliveryMap, "cptId");
        deliveryMap.createDelivery(105, 104, 10, 20);
        int cptId2 = (int) getAttribute(DeliveryMap.class, deliveryMap, "cptId");

        List<Delivery> deliveries = new ArrayList<>();

        deliveries.add(new Delivery(cptId1, 101, 100, 15, 10));
        deliveries.add(new Delivery(cptId2, 105, 104, 10, 20));

        assertEquals(deliveryMap.getDeliveryList(), deliveries);
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
        deliveryMap1.createDelivery(10, 11, 11, 10);

        DeliveryMap deliveryMap2 = new DeliveryMap();
        deliveryMap2.createWarehouse(1, 10);
        deliveryMap2.createDelivery(10, 11, 11, 10);
        int cptId1 = (int) getAttribute(DeliveryMap.class, deliveryMap1, "cptId");
        int cptId2 = (int) getAttribute(DeliveryMap.class, deliveryMap2, "cptId");

        assertEquals(cptId1, cptId2);
        assertEquals(deliveryMap1, deliveryMap2);
    }


    @Test
    public void equals3() {
        DeliveryMap deliveryMap1 = new DeliveryMap();
        deliveryMap1.createWarehouse(1, 10);
        deliveryMap1.createDelivery(1, 10, 11, 10);
        int cpt = (int) getAttribute(DeliveryMap.class, deliveryMap1, "cptId"); // Equals to the first delivery id + 1

        DeliveryMap deliveryMap2 = new DeliveryMap();
        deliveryMap2.createWarehouse(1, 10);
        setAttrbiute(DeliveryMap.class, deliveryMap2, "cptId", cpt);
        deliveryMap2.createDelivery(10, 11, 10, 10);
        assertNotEquals(deliveryMap1, deliveryMap2);
    }


    @Test
    public void equals4() {
        DeliveryMap deliveryMap1 = new DeliveryMap();
        deliveryMap1.createWarehouse(1, 10);
        deliveryMap1.createDelivery(10, 11, 10, 10);
        assertNotEquals(1, deliveryMap1);
    }


    @Test
    public void getSize() {
        DeliveryMap deliveryMap1 = new DeliveryMap();
        deliveryMap1.createWarehouse(1, 10);
        deliveryMap1.createDelivery(10, 11, 10, 10);
        deliveryMap1.createDelivery(10, 11, 10, 10);
        assertEquals(2, deliveryMap1.getSize());
    }


    @Test
    public void getDelivery1() {
        Delivery expected = new Delivery(1, 10, 11, 10, 10);
        DeliveryMap deliveryMap1 = new DeliveryMap();
        setAttrbiute(DeliveryMap.class, deliveryMap1, "cptId", expected.getId() - 1);
        deliveryMap1.createDelivery(expected.getPickUpNodeId(), expected.getDropOffNodeId(), expected.getPickUpDuration(), expected.getDropOffDuration());
        assertEquals(deliveryMap1.getDelivery(0), expected);
    }


    @Test (expected = IndexOutOfBoundsException.class)
    public void getDelivery2() {
        DeliveryMap deliveryMap1 = new DeliveryMap();
        deliveryMap1.createDelivery(10, 11, 10, 10);
        deliveryMap1.getDelivery(2);
    }


    @Test (expected = IndexOutOfBoundsException.class)
    public void getDelivery3() {
        DeliveryMap deliveryMap1 = new DeliveryMap();
        deliveryMap1.createDelivery(10, 11, 10, 10);
        deliveryMap1.getDelivery(-1);
    }

    @Test
    public void toString1() {
        DeliveryMap deliveryMap1 = new DeliveryMap();
        deliveryMap1.createWarehouse(1, 2);
        deliveryMap1.createDelivery(4, 5, 6, 7);
        int cpt = (int) getAttribute(DeliveryMap.class, deliveryMap1, "cptId");

        String expected = "DeliveryMap{"
                + "deliveryRequests="
                + "[id : " + cpt + " | pickUpNodeId : " + 4 + " | deliveryNodeId : " + 5 + " | pickUpDuration : "
                + 6 + " | deliveryDuration : " + 7 + "\n]"
                + ", warehouseNodeId="
                + 1
                + ", startDateInSeconds="
                + 2
                + '}';
        assertEquals(expected, deliveryMap1.toString());
    }
}
