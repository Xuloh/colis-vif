package fr.insa.colisvif.xml;

import fr.insa.colisvif.exception.InvalidFilePermissionException;
import fr.insa.colisvif.model.CityMapFactory;
import fr.insa.colisvif.util.Quadruplet;
import fr.insa.colisvif.util.Triplet;
import org.junit.Test;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CityMapFactoryTest {

    @Test(expected = FileNotFoundException.class)
    public void testLoadFileNotExist() throws IOException, ParserConfigurationException, SAXException {
        File file = new File("/nonexistant");
        CityMapFactory cityMapParser = new CityMapFactory();
        cityMapParser.loadFile(file);
    }

    @Test (expected = InvalidFilePermissionException.class)
    public void testInvalidPermissionLoadFile() throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
        File file = new File(getClass().getResource("/notReadable.txt").toURI());

        boolean permissionStatus = file.setReadable(false);

        if(!permissionStatus) {
            // system does not support setting read permission
            throw new InvalidFilePermissionException();
        }

        CityMapFactory cityMapParser = new CityMapFactory();
        cityMapParser.loadFile(file);
    }

    @Test
    public void testValidLoadFile() throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
        File file = new File(getClass().getResource("/validPlan_test.xml").toURI());
        CityMapFactory cityMapParser = new CityMapFactory();
        cityMapParser.loadFile(file);
    }

    @Test
    public void testReadNodes() throws URISyntaxException, IOException, ParserConfigurationException, SAXException {
        File file = new File(getClass().getResource("/validPlan_test.xml").toURI());
        CityMapFactory cityMapParser = new CityMapFactory();
        Element root = cityMapParser.loadFile(file);

        List<Triplet<Long, Double, Double>> readNodes = cityMapParser.readNodes(root);
        List<Triplet<Long, Double, Double>> expectedNodes = new ArrayList<>();
        expectedNodes.add(new Triplet<>(2684668925L,45.775486d,4.888253d));
        expectedNodes.add(new Triplet<>(2509481775L,45.775345d,4.8870163d));
        assertEquals(expectedNodes, readNodes);
    }

    @Test
    public void testReadSections() throws URISyntaxException, ParserConfigurationException, SAXException, IOException {
        File file = new File(getClass().getResource("/validPlan_test.xml").toURI());
        CityMapFactory cityMapParser = new CityMapFactory();
        Element root = cityMapParser.loadFile(file);

        List<Quadruplet<Double, String, Long, Long>> readSections = cityMapParser.readSections(root);
        List<Quadruplet<Double, String, Long, Long>> expectedSections = new ArrayList<>();
        expectedSections.add(new Quadruplet<>(97.249695d, "Rue Ch\u00e2teau-Gaillard", 2684668925L, 2509481775L));
        assertEquals(expectedSections, readSections);
    }
}