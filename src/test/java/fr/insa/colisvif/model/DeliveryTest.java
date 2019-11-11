package fr.insa.colisvif.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DeliveryTest {

    @Test
    public void testDeliveryGood() {
        new Delivery(1, 100, 101, 10, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDuration1() {
        new Delivery(1, 100, 100, -10, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDuration2() {
        new Delivery(1, 100, 100, 10, -10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDuration3() {
        new Delivery(1, 100, 101, -10, -10);
    }

    @Test
    public void testGetPickUpNodeId() {
        Delivery delivery = new Delivery(1, 100, 101, 10, 10);

        assertEquals(100, delivery.getPickUpNodeId());

    }

    @Test
    public void testGetDeliveryNodeId() {
        Delivery delivery = new Delivery(1, 100, 101, 10, 10);

        assertEquals(101, delivery.getDropOffNodeId());
    }

    @Test
    public void testGetPickUpDuration() {
        Delivery delivery = new Delivery(1, 100, 101, 10, 20);

        assertEquals(10, delivery.getPickUpDuration());
    }

    @Test
    public void testSPickUpDuration() {
        Delivery delivery = new Delivery(1, 100, 101, 10, 20);
        delivery.setPickUpDuration(50);
        assertEquals(50, delivery.getPickUpDuration());
    }

    @Test
    public void testGetDeliveryDuration() {
        Delivery delivery = new Delivery(1, 100, 101, 10, 20);

        assertEquals(20, delivery.getDropOffDuration());
    }

    @Test
    public void testSDeliveryDuration() {
        Delivery delivery = new Delivery(1, 100, 101, 10, 20);
        delivery.setDropOffDuration(125);
        assertEquals(125, delivery.getDropOffDuration());
    }

    @Test
    public void testSelfEquals() {
        Delivery delivery = new Delivery(1, 100, 101, 10, 20);
        assertEquals(delivery, delivery);
    }

    @Test
    public void testEquals() {
        Delivery delivery1 = new Delivery(1, 100, 101, 10, 20);
        Delivery delivery2 = new Delivery(1, 100, 101, 10, 20);
        assertEquals(delivery1, delivery2);
    }

    @Test
    public void testEqualsWrong() {
        Delivery delivery1 = new Delivery(1, 100, 101, 10, 20);
        Delivery delivery2 = new Delivery(2, 100, 101, 10, 20);
        assertNotEquals(delivery1, delivery2);
    }

    @Test
    public void testNullEquals() {
        Delivery deliveryNull = null;
        Delivery delivery = new Delivery(1, 100, 101, 10, 20);

        assertNotEquals(delivery, deliveryNull);
    }

    @Test
    public void testToString() {
        Delivery delivery = new Delivery(1, 100, 101, 10, 20);
        String exptected = "id : " + 1 + " | pickUpNodeId : " + 100 + " | deliveryNodeId : " + 101 + " | pickUpDuration : "
                + 10 + " | deliveryDuration : " + 20 + "\n";
        assertEquals(delivery.toString(), exptected);
    }

    @Test
    public void testToStringWrong() {
        Delivery delivery = new Delivery(1, 100, 101, 10, 20);
        String exptected = "id : " + 2 + " | pickUpNodeId : " + 100 + " | deliveryNodeId : " + 101 + " | pickUpDuration : "
                + 10 + " | deliveryDuration : " + 20 + "\n";
        assertNotEquals(delivery.toString(), exptected);
    }


    @Test
    public void testClassEquals() {
        Delivery delivery = new Delivery(1, 100, 101, 10, 20);

        assertNotEquals(delivery, 1);
    }

    @Test
    public void getId() {
        int expected = 1;
        Delivery delivery = new Delivery(expected, 100, 101, 10, 20);
        assertEquals(delivery.getId(), expected);
    }

    @Test
    public void getIdWrong() {
        int expected = 1;
        Delivery delivery = new Delivery(expected + 1, 100, 101, 10, 20);
        assertNotEquals(delivery.getId(), expected);
    }

    @Test
    public void getPickUp() {
        Vertex expected = new Vertex(100, Vertex.PICK_UP, 10);
        Delivery delivery = new Delivery(1, expected.getNodeId(), 10, expected.getDuration(), 10);
        assertEquals(delivery.getPickUp(), expected);
    }

    @Test
    public void getPickUpWrong() {
        Vertex expected = new Vertex(100, Vertex.PICK_UP, 10);
        Delivery delivery = new Delivery(1, expected.getNodeId(), 10, expected.getDuration() + 1, 10);
        assertNotEquals(delivery.getPickUp(), expected);
    }
}
