package fr.insa.colisvif.model;

import fr.insa.colisvif.exception.IdException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Contains the representation of the city, with min max latitude and
 * a map of {@link Node} and {@link Section}. It has a method to calculate
 * all the shortest paths between a {@link Node} and every other {@link Node}
 * CityMap also contains shortestRound that calculates the shortest round
 * from a {@link DeliveryMap} on the CityMap.
 */
public class CityMap {

    private static final Logger LOGGER = LogManager.getLogger(CityMap.class);

    /*
    public static void main(String args[]) throws SAXException,
    IdError, ParserConfigurationException, IOException {
        File file = new File("/C:/Users/F\u00e9lix/Desktop/INSA/4IF/PLD agile/
        fichiersXML2019/grandPlan.xml");
        CityMap map = new CityMapFactory().createCityMapFromXMLFile(file);
        file = new File("/C:/Users/F\u00e9lix/Desktop/INSA/4IF/PLD agile/
        fichiersXML2019/demandeGrand7.xml");
        DeliveryMap deliveries = new DeliveryMapFactory()
        .createDeliveryMapFromXML(file, map);

        Round round = map.naiveRound(deliveries);
    }*/

    private static final int LNG_MIN = -180;

    private static final int LNG_MAX = 180;

    private static final int LAT_MIN = -90;

    private static final int LAT_MAX = 90;

    private double lngMin;

    private double lngMax;

    private double latMin;

    private double latMax;

    private Map<Long, Node> mapNode;

    private Map<String, List<Section>> mapSection;

    private HashMap<Long, PathsFromVertex> pathsFromVertices;

    /**
     * Constructor of CityMap. Initialize the min/max latitudes and min/max
     * longitudes.
     * It also initialises the map of {@link Node}, {@link Section}
     * and {@link PathsFromVertex}.
     */
    public CityMap() {
        this.latMin = LAT_MAX;
        this.latMax = LAT_MIN;
        this.lngMin = LNG_MAX;
        this.lngMax = LNG_MIN;
        this.mapNode = new HashMap<>();
        this.mapSection = new HashMap<>();
        this.pathsFromVertices = new HashMap<>();
    }

    /**
     * Creates a Node from an id, a latitude and longitude and ads it
     * to the map of {@link Node}.
     *
     * @param id        the id of the Node.
     * @param latitude  the latitude of the Node.
     * @param longitude the longitude of the Node.
     * @throws IllegalArgumentException when the latitude or longitude
     *                                  is out of bounds.
     */
    public void createNode(long id, double latitude, double longitude) throws IllegalArgumentException {

        Node newNode = new Node(id, latitude, longitude);

        this.mapNode.put(id, newNode);

        if (latitude < this.latMin) {
            this.latMin = latitude;
        }

        if (latitude > this.latMax) {
            this.latMax = latitude;
        }

        if (longitude < this.lngMin) {
            this.lngMin = longitude;
        }

        if (longitude > this.lngMax) {
            this.lngMax = longitude;
        }
    }

    /**
     * Creates a {@link Section} and adds it to the successors of the {@link Node}
     * that has the same origin.
     *
     * @param length      the length of the section.
     * @param roadName    the road name of the section.
     * @param destination the destination of the section.
     * @param origin      the origin of the section.
     * @throws IllegalArgumentException if the origin of the new {@link Section}
     *                                  does not match any {@link Node}
     */
    public void createSection(double length, String roadName, long destination, long origin) throws IllegalArgumentException {
        Section newSection = new Section(length, roadName, destination, origin);

        if (this.mapSection.get(roadName) == null) {
            List<Section> newSections = new ArrayList<>();
            newSections.add(newSection);
            this.mapSection.put(roadName, newSections);
        } else {
            this.mapSection.get(roadName).add(newSection);
        }

        if (!this.mapNode.containsKey(origin)) {
            throw new IllegalArgumentException("The origin of the Section does not match any Nodes in the map of Nodes");
        } else {
            try {
                this.mapNode.get(origin).addToSuccessors(newSection);
            } catch (IdException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

    }

    /**
     * Returns the minimum longitude of the CityMap.
     *
     * @return the minimum longitude of the CityMap.
     */
    public double getLngMin() {
        return this.lngMin;
    }

    /**
     * Returns the maximum longitude of the CityMap.
     *
     * @return the maximum longitude of the CityMap.
     */
    public double getLngMax() {
        return this.lngMax;
    }

    /**
     * Returns the minimum latitude of the CityMap.
     *
     * @return the minimum latitude of the CityMap.
     */
    public double getLatMin() {
        return this.latMin;
    }

    /**
     * Returns the maximum latitude of the CityMap.
     *
     * @return the maximum latitude of the CityMap.
     */
    public double getLatMax() {
        return this.latMax;
    }

    /**
     * Returns the map of {@link Node} (idOfTheNode, {@link Node}).
     *
     * @return the map of {@link Node} (idOfTheNode, {@link Node}).
     */
    public Map<Long, Node> getMapNode() {
        return this.mapNode;
    }

    /**
     * Returns the map of {@link Section} (Road name, List of {@link Section}).
     *
     * @return the map of {@link Section} (Road name, List of {@link Section}).
     */
    public Map<String, List<Section>> getMapSection() {
        return this.mapSection;
    }

    /**
     * Returns the minimum length between two coordinates.
     *
     * @param start  the starting point.
     * @param finish the ending point.
     * @return the minimum length between two coordinates.
     */
    public double getLength(long start, long finish) {
        return pathsFromVertices.get(start).getLength(finish);
    }

    /**
     * Returns the {@link Section} between start and finish.
     *
     * @param start  the starting point.
     * @param finish the ending point.
     * @return the {@link Section} between start and finish.
     * @throws IllegalArgumentException when it does not find a {@link Section}
     *                                  between start and finish.
     */
    public Section getSection(long start, long finish) throws IllegalArgumentException {
        for (Section section : getMapNode().get(start).getSuccessors()) {
            if (section.getDestination() == finish) {
                return section;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Returns a {@link String} representation of this {@link CityMap}.
     *
     * @return a {@link String} representation of this {@link CityMap}.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Nodes : \n");

        Set<Long> nodeKeys = this.mapNode.keySet();
        for (long nodeKey : nodeKeys) {
            Node node = this.mapNode.get(nodeKey);
            result.append(node.toString());
        }

        result.append("\nSections : \n");

        Set<String> sectionKeys = this.mapSection.keySet();
        for (String sectionKey : sectionKeys) {
            List<Section> sections = this.mapSection.get(sectionKey);
            for (Section s : sections) {
                result.append(s.toString());
            }
        }

        return result.toString();
    }

    /**
     * Perform a dijkstra between the point at coordinate start and all
     * the {@link Node} stored in the map of {@link Node}.
     *
     * @param start the coordinate of the starting point.
     */
    private void dijkstra(long start) {
        // TODO @Felix : tester si start est une bonne coordonnée??

        PathsFromVertex pathsFromStart = new PathsFromVertex();
        pathsFromStart.setLength(start, 0D);
        pathsFromStart.setPrev(start, null);

        Comparator<Long> cmp = Comparator.comparingDouble(pathsFromStart::getLength);
        PriorityQueue<Long> priorityQueue = new PriorityQueue<>(cmp);
        priorityQueue.add(start);

        while (!priorityQueue.isEmpty()) {
            Long node = priorityQueue.poll();
            double length = pathsFromStart.getLength(node);
            for (Section section : getMapNode().get(node).getSuccessors()) {
                long next = section.getDestination();
                double destinationLength = pathsFromStart.getLength(next);
                if (destinationLength == -1 || length + section.getLength() < destinationLength) {
                    pathsFromStart.setLength(next, length + section.getLength());
                    pathsFromStart.setPrev(next, section);
                    priorityQueue.remove(next);
                    priorityQueue.add(next);
                }
            }
        }
        pathsFromVertices.put(start, pathsFromStart);
    }

    /**
     * Creates a {@link Round} object from a {@link DeliveryMap} that contains
     * the best path to take from the warehouse going though all the pickup and dropoff
     * {@link Node} respecting the order (pick up node x is always before drop off node x).
     *
     * @param deliveries a delivery object containing all the pickup and dropoff nodes and their information.
     * @return a {@link Round} object from a {@link DeliveryMap} that contains.
     * the best path.
     */
    public Round shortestRound(DeliveryMap deliveries) {
        var debut = System.nanoTime();
        dijkstra(deliveries.getWarehouseNodeId());
        for (Delivery delivery : deliveries.getDeliveryList()) {
            dijkstra(delivery.getPickUpNodeId());
            dijkstra(delivery.getDropOffNodeId());
        }
        var fin = System.nanoTime();
        System.out.print("Dijkstra time : ");
        System.out.println((fin - debut) * 0.000000001);
        VerticesGraph verticesGraph = new VerticesGraph(deliveries, pathsFromVertices);
        return verticesGraph.shortestRound();
    }

    /**
     * Determines if the given {@link Object} is "equal"
     * to this {@link CityMap}.
     * Only other {@link CityMap} are considered for comparison.
     * The method compares the min/max latitude and longitude, the
     * map of {@link Node} and the map of {@link Section}
     *
     * @param o the {@link Object} to compare this {@link CityMap} to
     * @return <code>true</code> if o is a {@link CityMap} whose values are
     * "equal" to those of this {@link CityMap}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CityMap cityMap = (CityMap) o;
        return Double.compare(cityMap.lngMin, lngMin) == 0
                && Double.compare(cityMap.lngMax, lngMax) == 0
                && Double.compare(cityMap.latMin, latMin) == 0
                && Double.compare(cityMap.latMax, latMax) == 0
                && Objects.equals(mapNode, cityMap.mapNode)
                && Objects.equals(mapSection, cityMap.mapSection);
    }

}


