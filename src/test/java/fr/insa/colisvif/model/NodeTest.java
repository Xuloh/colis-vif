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

    @Test
    public void testNodeToString() {
        Node testNode = new Node(1242021, 12.451, -21.7856);
        String expected ="ID : 1242021 | Latitude : 12.451 | Longitude : -21.7856\n";
        assertEquals("Node.toString() is functionnal", expected, testNode.toString());
    }
}
