package fr.insa.colisvif.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class CityMapTest {

    @Test
    public void testUpdateLatMax() {
        CityMap citymap = new CityMap();
        citymap.createNode(2405632, -60, 24);

        int expected = -60;
        assert(expected == citymap.getLatMax());
    }

    @Test
    public void testUpdateLongMin() {
        CityMap citymap = new CityMap();
        citymap.createNode(2405632, -90, 120);

        int expected = 120;
        assert(expected == citymap.getLongMin());
    }

    @Test
    public void testUpdateLatMaxLongMin() {
        CityMap citymap = new CityMap();
        citymap.createNode(2405632, -60, 120);
        assert(-60 == citymap.getLatMax() && citymap.getLongMin() == 120) ;
    }

}
