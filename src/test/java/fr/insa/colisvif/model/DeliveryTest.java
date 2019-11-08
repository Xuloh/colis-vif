package fr.insa.colisvif.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DeliveryTest {

    @Test
    public void testDeliveryGood() {
        Delivery delivery = new Delivery(100, 101, 10, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOriginEqualsDestination() {
        Delivery delivery = new Delivery(100, 100, 10, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDuration1() {
        Delivery delivery = new Delivery(100, 100, -10, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDuration2() {
        Delivery delivery = new Delivery(100, 100, 10, -10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDuration3() {
        Delivery delivery = new Delivery(100, 101, -10, -10);
    }

    @Test
    public void testGetPickUpNodeId() {
        Delivery delivery = new Delivery(100, 101, 10, 10);

        assertEquals(100, delivery.getPickUpNodeId());

    }

    @Test
    public void testSetPickUpNodeId() {
        Delivery delivery = new Delivery(100, 101, 10, 10);
        delivery.setPickUpNodeId(200);
        assertEquals(200, delivery.getPickUpNodeId());

    }

    @Test
    public void testGetDeliveryNodeId() {
        Delivery delivery = new Delivery(100, 101, 10, 10);

        assertEquals(101, delivery.getDropOffNodeId());
    }

    @Test
    public void testSDeliveryNodeId() {
        Delivery delivery = new Delivery(100, 101, 10, 10);
        delivery.setDropOffNodeId(200);
        assertEquals(200, delivery.getDropOffNodeId());
    }

    @Test
    public void testGetPickUpDuration() {
        Delivery delivery = new Delivery(100, 101, 10, 20);

        assertEquals(10, delivery.getPickUpDuration());
    }

    @Test
    public void testSPickUpDuration() {
        Delivery delivery = new Delivery(100, 101, 10, 20);
        delivery.setPickUpDuration(50);
        assertEquals(50, delivery.getPickUpDuration());
    }

    @Test
    public void testGetDeliveryDuration() {
        Delivery delivery = new Delivery(100, 101, 10, 20);

        assertEquals(20, delivery.getDropOffDuration());
    }

    @Test
    public void testSDeliveryDuration() {
        Delivery delivery = new Delivery(100, 101, 10, 20);
        delivery.setDropOffDuration(125);
        assertEquals(125, delivery.getDropOffDuration());
    }

    /*
    @Test
    public void testIsPicked() {
        Delivery delivery = new Delivery(100, 101, 10,20);
        assertEquals(false,delivery.isPicked());
    }

    @Test
    public void testSetToPickedUp() {
        Delivery delivery = new Delivery(100, 101, 10,20);
        delivery.setToPickedUp(true);
        assertEquals(true,delivery.isPicked());
    }
*/
    /*
    @Test
    public void testToString() {
        Delivery delivery = new Delivery(100, 101, 10,20);
        assertEquals("pickUpNodeId : 100 | deliveryNodeId : 101 | pickUpDuration : 10 | deliveryDuration : 20 | isPicked : false\n"
                ,delivery.toString());
    }
    */

}
