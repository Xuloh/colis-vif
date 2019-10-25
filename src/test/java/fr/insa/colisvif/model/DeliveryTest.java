package fr.insa.colisvif.model;

import org.junit.Test;

public class DeliveryTest {

    @Test
    public void testDeliveryGood() {
        Delivery delivery = new Delivery(100, 101, 10,10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOriginEqualsDestination() {
        Delivery delivery = new Delivery(100, 100, 10,10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDuration1() {
        Delivery delivery = new Delivery(100, 100, -10,10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDuration2() {
        Delivery delivery = new Delivery(100, 100, 10,-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDuration3() {
        Delivery delivery = new Delivery(100, 100, -10,-10);
    }
}
