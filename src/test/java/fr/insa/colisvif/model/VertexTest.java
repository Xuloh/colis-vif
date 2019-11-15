package fr.insa.colisvif.model;

import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;

public class VertexTest {

    @Test
    public void testNodeGood() {
        new Vertex(1, Vertex.PICK_UP, 10);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNodeWrong() {
        new Vertex(1, Vertex.PICK_UP, -1);
    }

    @Test
    public void getDuration() {
        int expected = 10;
        assertEquals(expected, new Vertex(1, Vertex.PICK_UP, expected).getDuration());
    }

    @Test
    public void setDuration() {
        int expected = 11;
        Vertex vertex = new Vertex(1, Vertex.PICK_UP, 10);
        vertex.setDuration(expected);
        assertEquals(expected, vertex.getDuration());
    }

    @Test
    public void setDurationWrong() {
        int expected = 12;
        Vertex vertex = new Vertex(1, Vertex.PICK_UP, 10);
        vertex.setDuration(expected + 1);
        assertNotEquals(expected, vertex.getDuration());
    }

    @Test (expected = IllegalArgumentException.class)
    public void setDurationIllegal() {
        int expected = -1;
        Vertex vertex = new Vertex(1, Vertex.PICK_UP, 10);
        vertex.setDuration(expected);
    }

    @Test
    public void getNodeId() {
        int expected = 10;
        assertEquals(expected, new Vertex(expected, Vertex.PICK_UP, 10).getNodeId());
    }

    @Test
    public void isPickUp() {
        boolean expected = Vertex.PICK_UP;
        assertTrue(new Vertex(1, expected, 10).isPickUp());
    }

    @Test
    public void isDropOff() {
        boolean expected = Vertex.DROP_OFF;
        assertTrue(new Vertex(1, expected, 10).isDropOff());
    }

    @Test
    public void equals1() {
        Vertex vertex = new Vertex(1, Vertex.PICK_UP, 10);
        assertEquals(vertex, vertex);
    }

    @Test
    public void equals2() {
        Vertex vertex1 = new Vertex(1, Vertex.PICK_UP, 10);
        Vertex vertex2 = new Vertex(1, Vertex.PICK_UP, 10);
        assertEquals(vertex1, vertex2);
    }

    @Test
    public void equals3() {
        Vertex vertex1 = new Vertex(1, Vertex.PICK_UP, 10);
        Vertex vertex2 = new Vertex(2, Vertex.PICK_UP, 10);
        assertNotEquals(vertex1, vertex2);
    }

    @Test
    public void equals4() {
        Vertex vertex1 = new Vertex(1, Vertex.PICK_UP, 10);
        Vertex vertex2 = new Vertex(1, Vertex.DROP_OFF, 10);
        assertNotEquals(vertex1, vertex2);
    }

    @Test
    public void equals5() {
        Vertex vertex = new Vertex(1, Vertex.PICK_UP, 10);
        assertNotEquals(null, vertex);
    }

    @Test
    public void toString1() {
        Vertex vertex = new Vertex(1, Vertex.PICK_UP, 10);
        String expected = "Vertex{"
                + "id=" + 1
                + ", type=" + Vertex.PICK_UP
                + ", durationInSeconds=" + 10
                + '}';
        assertEquals(expected, vertex.toString());
    }

}
