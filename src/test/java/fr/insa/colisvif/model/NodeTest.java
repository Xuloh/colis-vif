package fr.insa.colisvif.model;

import fr.insa.colisvif.exception.IdException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class NodeTest {

    @Test
    public void testNodeGood() {
        new Node(2405632, 50, 24);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLatitudeLowBound() {
        new Node(2405632, -120, 24);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLatitudeHighBound() {
        new Node(2405632, 178, 24);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLongitudeLowBound() {
        new Node(2405632, -10, -248);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLongitudeHighBound() {
        new Node(2405632, -10, 245);
    }

    @Test
    public void testNodeAddToSuccessorsOK() throws IdException {
        Section testSection = new Section(158, "Rue A 11h30", 240, 241);
        Node testNode = new Node(240, 0, 0);
        testNode.addToSuccessors(testSection);
    }

    @Test(expected = IdException.class)
    public void testNodeAddToSuccessorsNotOK() throws IdException {
        Section testSection = new Section(158, "Rue A 11h30", 241, 240);
        Node testNode = new Node(240, 0, 0);
        testNode.addToSuccessors(testSection);
    }

    @Test
    public void testNodeToString() {
        Node testNode = new Node(1242021, 12.451, -21.7856);
        String expected = "ID : 1242021 | Latitude : 12.451 | Longitude : -21.7856\n";
        assertEquals("Node.toString() is functionnal", expected, testNode.toString());
    }

    @Test
    public void getLongitude() {
        double expected = -21.7856;
        Node testNode = new Node(1242021, 12.451, expected);
        assertEquals(expected, testNode.getLongitude(), 0.001);
    }

    @Test
    public void getLatitude() {
        double expected = 12.451;
        Node testNode = new Node(1242021, expected, 12.451);
        assertEquals(expected, testNode.getLatitude(), 0.001);
    }

    @Test
    public void getId() {
        long expected = 1242021;
        Node testNode = new Node(expected, 12.451, 12.451);
        assertEquals(expected, testNode.getId());
    }

    @Test
    public void getSuccessors() throws IdException {
        Node testNode = new Node(123456, 12.451, 24.64);
        Section section1 = new Section(12, "Truc1", 123456, 123456);
        Section section2 = new Section(12, "Truc2", 123456, 123456);


        testNode.addToSuccessors(section1);
        testNode.addToSuccessors(section2);

        List<Section> successors = testNode.getSuccessors();
        assertEquals(successors.get(0), section1);
        assertEquals(successors.get(1), section2);
    }

    @Test
    public void equals1() {
        Node node1 = new Node(1, 10, 10);
        Node node2 = new Node(1, 10, 10);

        assertEquals(node1, node2);
    }

    @Test
    public void equals2() {
        Node node1 = new Node(1, 10, 10);

        assertEquals(node1, node1);
    }

    @Test
    public void equals3() {
        Node node1 = new Node(1, 10, 10);

        assertNotEquals(node1, 1);
    }

    @Test
    public void equals4() {
        Node node1 = new Node(1, 10, 10);
        Node node2 = new Node(2, 10, 10);

        assertNotEquals(node1, node2);
    }
}
