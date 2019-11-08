package fr.insa.colisvif.model;

/*
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
*/
public class StepTest {
/*
    @Test
    public void testStepGood() {
        ArrayList<Section> sections = new ArrayList();
        sections.add(new Section(20,"Rue de la Paix",100,101));
        Step step = new Step(sections, false, 100, 50);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStepNegativeArrival() {
        ArrayList<Section> sections = new ArrayList();
        sections.add(new Section(20,"Rue de la Paix",100,101));
        Step step = new Step(sections, false, -1, 50);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStepNegativeDuration() {
        ArrayList<Section> sections = new ArrayList();
        sections.add(new Section(20,"Rue de la Paix",100,101));
        Step step = new Step(sections, false, 10, -1);    }


    @Test
    public void getSections() {
        ArrayList<Section> sections = new ArrayList();
        sections.add(new Section(20,"Rue de la Paix",100,101));
        sections.add(new Section(40,"Rue de la Péx",40,101));

        Step step = new Step(sections, true, 100, 50);
        assertEquals(sections, step.getSections());

        ArrayList<Section> sections2 = new ArrayList();
        sections2.add(new Section(20,"Rue de la Paix",100,101));
        sections2.add(new Section(40,"Rue de la Péx",41,101));
        assertNotEquals(sections2, step.getSections());

        sections2.add(new Section(40,"Rue de la Péx",40,101));
        assertNotEquals(sections2, step.getSections());
    }

    @Test
    public void getArrivalDateInSeconds() {
        int expected = 100;
        ArrayList<Section> sections = new ArrayList();
        sections.add(new Section(20,"Rue de la Paix",100,101));
        Step step = new Step(sections, false, expected, 50);
        assertEquals(step.getArrivalDateInSeconds(), expected);
    }

    @Test
    public void setArrivalDateInSeconds() {
        int expected_new = 10;
        ArrayList<Section> sections = new ArrayList();
        sections.add(new Section(20,"Rue de la Paix",100,101));
        Step step = new Step(sections, false, 100, 50);
        step.setArrivalDateInSeconds(expected_new);

        assertEquals(expected_new, step.getArrivalDateInSeconds());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setArrivalDateInSecondsNegative() {
        ArrayList<Section> sections = new ArrayList();
        sections.add(new Section(20,"Rue de la Paix",100,101));
        Step step = new Step(sections, false, 100, 50);
        step.setArrivalDateInSeconds(-1);
    }

    @Test
    public void getDurationInSeconds() {
        int expected = 100;
        ArrayList<Section> sections = new ArrayList();
        sections.add(new Section(20,"Rue de la Paix",100,101));
        Step step = new Step(sections, false, 100, expected);
        assertEquals(step.getDurationInSeconds(), expected);
    }

    @Test
    public void setDurationInSeconds() {
        int expected_new = 10;
        ArrayList<Section> sections = new ArrayList();
        sections.add(new Section(20,"Rue de la Paix",100,101));
        Step step = new Step(sections, false, 100, 50);
        step.setDurationInSeconds(expected_new);

        assertEquals(expected_new, step.getDurationInSeconds());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setDurationInSecondsNegative() {
        ArrayList<Section> sections = new ArrayList();
        sections.add(new Section(20,"Rue de la Paix",100,101));
        Step step = new Step(sections, false, 100, 50);
        step.setDurationInSeconds(-1);
    }

    @Test
    public void isPickUp() {
        ArrayList<Section> sections = new ArrayList();
        sections.add(new Section(20,"Rue de la Paix",100,101));
        Step step = new Step(sections, true, 100, 50);
        assertTrue(step.isPickUp());
    }

    @Test
    public void isDropOff() {
        ArrayList<Section> sections = new ArrayList();
        sections.add(new Section(20,"Rue de la Paix",100,101));
        Step step = new Step(sections, false, 100, 50);
        assertFalse(step.isDropOff());
    }*/
}