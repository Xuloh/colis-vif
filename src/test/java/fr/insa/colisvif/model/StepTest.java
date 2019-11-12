package fr.insa.colisvif.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class StepTest {
    @Test
    public void testStepGood() {
        new Step(new Vertex(1, Vertex.PICK_UP, 10), 1);
    }

    @Test
    public void getSections1() {
        LinkedList<Section> sections = new LinkedList<>();
        sections.add(new Section(20, "Rue de la Paix", 100, 101));
        sections.add(new Section(40, "Rue de la Péx", 101, 102));

        Step step = new Step(new Vertex(1, Vertex.PICK_UP, 10), 1);
        step.addSection(sections.get(1));
        step.addSection(sections.get(0));

        LinkedList<Section> sections2 = new LinkedList<>();
        sections2.addFirst(new Section(40, "Rue de la Péx", 101, 102));
        sections2.addFirst(new Section(20, "Rue de la Paix", 100, 101));

        assertEquals(sections2, step.getSections());

        ArrayList<Section> sections3 = new ArrayList<>();
        sections3.add(new Section(40, "Rue de la Péx", 41, 101));
        sections3.add(new Section(20, "Rue de la Paix", 100, 101));
        assertNotEquals(sections3, step.getSections());

        sections3.add(new Section(40, "Rue de la Péx", 40, 101));
        assertNotEquals(sections3, step.getSections());
    }

    @Test (expected = IllegalArgumentException.class)
    public void getSections2() {
        LinkedList<Section> sections = new LinkedList<>();
        sections.add(new Section(20, "Rue de la Paix", 100, 101));
        sections.add(new Section(40, "Rue de la Péx", 102, 103));

        Step step = new Step(new Vertex(1, Vertex.PICK_UP, 10), 1);
        step.addSection(sections.get(0));
        step.addSection(sections.get(1));
    }

    @Test
    public void setArrivalDate() {
        int expected = 10;
        Step step = new Step(new Vertex(1, Vertex.PICK_UP, expected + 1), 1);
        step.setArrivalDate(expected);

        assertEquals(expected, step.getArrivalDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setArrivalDateNegative() {
        int expected = -10;
        Step step = new Step(new Vertex(1, Vertex.PICK_UP, 1), 1);
        step.setArrivalDate(expected);
    }

    @Test
    public void getDuration1() {
        int expected = 100;
        Step step = new Step(new Vertex(1, Vertex.PICK_UP, expected), 1);
        assertEquals(expected, step.getDuration());
    }

    @Test
    public void getDuration2() {
        int expected = 100;
        Step step = new Step(new Vertex(1, Vertex.PICK_UP, expected + 1), 1);
        assertNotEquals(expected, step.getDuration());
    }

    @Test
    public void setDuration() {
        int expected = 10;
        Step step = new Step(new Vertex(1, Vertex.PICK_UP, expected + 1), 1);
        step.setDuration(expected);

        assertEquals(expected, step.getDuration());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setDurationNegative() {
        int expected = -10;
        Step step = new Step(new Vertex(1, Vertex.PICK_UP, 1), 1);
        step.setDuration(expected);
    }

    @Test
    public void isPickUp() {
        Step step = new Step(new Vertex(1, Vertex.PICK_UP, 1), 1);
        assertTrue(step.isPickUp());
    }

    @Test
    public void isDropOff() {
        Step step = new Step(new Vertex(1, Vertex.DROP_OFF, 1), 1);
        assertTrue(step.isDropOff());
    }

    @Test
    public void getDeliveryID1() {
        int expected = 1;
        assertEquals(expected, new Step(new Vertex(1, Vertex.PICK_UP, 10), expected).getDeliveryID());
    }

    @Test
    public void getDeliveryID2() {
        int expected = 1;
        assertNotEquals(expected, new Step(new Vertex(1, Vertex.PICK_UP, 10), expected + 1).getDeliveryID());
    }

    @Test
    public void equals1() {
        Step step = new Step(new Vertex(1, Vertex.PICK_UP, 10), 1);
        assertEquals(step, step);
    }

    @Test
    public void equals2() {
        Step step1 = new Step(new Vertex(1, Vertex.PICK_UP, 10), 1);
        Step step2 = new Step(new Vertex(1, Vertex.PICK_UP, 10), 1);
        assertEquals(step1, step2);
    }

    @Test
    public void equals3() {
        Step step1 = new Step(new Vertex(1, Vertex.PICK_UP, 10), 1);
        Step step2 = new Step(new Vertex(1, Vertex.PICK_UP, 11), 1);
        assertNotEquals(step1, step2);
    }

    @Test
    public void equals4() {
        Step step1 = new Step(new Vertex(1, Vertex.PICK_UP, 10), 1);
        Step step2 = new Step(new Vertex(1, Vertex.DROP_OFF, 10), 1);
        assertNotEquals(step1, step2);
    }

    @Test
    public void equals5() {
        Step step1 = new Step(new Vertex(1, Vertex.PICK_UP, 10), 1);
        step1.addSection(new Section(1, "test", 1, 1));
        Step step2 = new Step(new Vertex(1, Vertex.PICK_UP, 10), 1);
        step2.addSection(new Section(2, "test", 1, 1));

        assertNotEquals(step1, step2);
    }

    @Test
    public void equals6() {
        Step step1 = new Step(new Vertex(1, Vertex.PICK_UP, 10), 1);
        Step step2 = new Step(new Vertex(1, Vertex.PICK_UP, 10), 2);
        assertNotEquals(step1, step2);
    }

    @Test
    public void equals7() {
        Step step1 = new Step(new Vertex(1, Vertex.PICK_UP, 10), 1);
        assertNotEquals(1, step1);
    }

    @Test
    public void getType() {
        Step step1 = new Step(new Vertex(1, Vertex.PICK_UP, 10), 1);
        assertEquals(Vertex.PICK_UP, step1.getType());

        Step step2 = new Step(new Vertex(1, Vertex.DROP_OFF, 10), 1);
        assertEquals(Vertex.DROP_OFF, step2.getType());

        assertNotEquals(Vertex.PICK_UP, step2.getType());
        assertNotEquals(Vertex.DROP_OFF, step1.getType());
    }
}
