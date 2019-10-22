package fr.insa.colisvif.model;

import fr.insa.colisvif.model.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test(expected = IllegalArgumentException.class)
    public void testLatitudeLowBound() {
        Node testNode = new Node(2405632, -120, 24);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLatitudeHighBound() {
        Node testNode = new Node(2405632, 178, 24);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLongitudeLowBound() {
        Node testNode = new Node(2405632, -10, -248);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLongitudeHighBound() {
        Node testNode = new Node(2405632, -10, 245);
    }
}
