package fr.insa.colisvif.model;

import fr.insa.colisvif.exception.IdError;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeGood() {
        Node testNode = new Node(2405632, 50, 24);
    }

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
    public void testNodeAddToSuccessorsOK() throws IdError {
        Section testSection = new Section(158, "Rue A 11h30", 241, 240);
        Node testNode = new Node(240, 0, 0);
        testNode.addToSuccessors(testSection);
    }

    @Test(expected = IdError.class)
    public void testNodeAddToSuccessorsNotOK() throws IdError {
        Section testSection = new Section(158, "Rue A 11h30", 240, 241);
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
    public void getSuccessors() {
        Node testNode = new Node(123456, 12.451, 24.64);
        Section section1 = new Section(12, "Truc1", 123456, 123456);
        Section section2 = new Section(12, "Truc2", 123456, 123456);

        try {
            testNode.addToSuccessors(section1);
            testNode.addToSuccessors(section2);
        } catch (IdError idError) {
            idError.printStackTrace();
        }

        List<Section> successors = testNode.getSuccessors();
        assertEquals(successors.get(0), section1);
        assertEquals(successors.get(1), section2);


    }
}
