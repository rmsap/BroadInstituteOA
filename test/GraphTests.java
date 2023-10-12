import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * A class that contains tests for Graphs.
 */
public class GraphTests {
    private Graph<Route> graph;
    private Route routeOne;
    private Route routeTwo;
    private Route routeThree;
    private Route routeFour;
    private Route routeFive;

    @Test
    public void testConstructInvalidGraph() {
        try {
            graph = new Graph<>(new HashMap<>());
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("There must be edges in the graph", e.getMessage());
        }

        try {
            graph = new Graph<>(null);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("There must be edges in the graph", e.getMessage());
        }
    }

    @Test
    public void testValidBfs() {
        setupGraph();

        final List<Route> oneToFour = graph.bfs(Collections.singletonList(routeOne),
                Collections.singletonList(routeFour));
        Assert.assertEquals(new ArrayList<>(Arrays.asList(routeOne, routeThree, routeFour)), oneToFour);

        final List<Route> oneOrTwoToFour = graph.bfs(Arrays.asList(routeOne, routeTwo),
                Collections.singletonList(routeFour));
        Assert.assertEquals(new ArrayList<>(Arrays.asList(routeOne, routeThree, routeFour)), oneOrTwoToFour);

        final List<Route> twoOrOneToFour = graph.bfs(Arrays.asList(routeTwo, routeOne),
                Collections.singletonList(routeFour));
        Assert.assertEquals(new ArrayList<>(Arrays.asList(routeTwo, routeOne, routeThree, routeFour)), twoOrOneToFour);

        final List<Route> oneToTwoOrFour = graph.bfs(Collections.singletonList(routeOne), Arrays.asList(routeTwo,
                routeFour));
        Assert.assertEquals(new ArrayList<>(Arrays.asList(routeOne, routeTwo)), oneToTwoOrFour);

        final List<Route> oneToFourOrTwo = graph.bfs(Collections.singletonList(routeOne), Arrays.asList(routeFour,
                routeTwo));
        Assert.assertEquals(new ArrayList<>(Arrays.asList(routeOne, routeTwo)), oneToFourOrTwo);

        final List<Route> oneOrTwoToTwoOrFour = graph.bfs(Arrays.asList(routeOne, routeTwo), Arrays.asList(routeTwo,
                routeFour));
        Assert.assertEquals(Collections.singletonList(routeTwo), oneOrTwoToTwoOrFour);

        final List<Route> empty = graph.bfs(Collections.singletonList(routeTwo), new ArrayList<>());
        Assert.assertTrue(empty.isEmpty());

        final List<Route> emptyTwo = graph.bfs(Collections.singletonList(routeOne),
                Collections.singletonList(routeFive));
        Assert.assertTrue(emptyTwo.isEmpty());

        final List<Route> emptyThree = graph.bfs(Collections.singletonList(routeFive),
                Collections.singletonList(routeOne));
        Assert.assertTrue(emptyThree.isEmpty());

        final List<Route> oneToFiveOrTwo = graph.bfs(Collections.singletonList(routeOne),
                Arrays.asList(routeFive, routeTwo));
        Assert.assertEquals(new ArrayList<>(Arrays.asList(routeOne, routeTwo)), oneToFiveOrTwo);

        final List<Route> fiveToFive = graph.bfs(Collections.singletonList(routeFive),
                Collections.singletonList(routeFive));
        Assert.assertEquals(Collections.singletonList(routeFive), fiveToFive);
    }

    @Test
    public void testInvalidBfs() {
        setupGraph();

        try {
            graph.bfs(new ArrayList<>(), new ArrayList<>());
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Must be at least one start node.", e.getMessage());
        }

        try {
            graph.bfs(null, new ArrayList<>());
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Must be at least one start node.", e.getMessage());
        }

        try {
            graph.bfs(null, null);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Must be at least one start node.", e.getMessage());
        }
    }

    /**
     * Set up a Graph to use for tests.
     */
    private void setupGraph() {
        routeOne = new Route();
        routeOne.setLongName("Route One");

        routeTwo = new Route();
        routeTwo.setLongName("Route Two");

        routeThree = new Route();
        routeThree.setLongName("Route Three");

        routeFour = new Route();
        routeFour.setLongName("Route Four");

        // Use LinkedHashSet for predictable iteration order
        final Set<Route> routeOneConnections = new LinkedHashSet<>();
        routeOneConnections.add(routeTwo);
        routeOneConnections.add(routeThree);


        final Set<Route> routeTwoConnections = new LinkedHashSet<>();
        routeTwoConnections.add(routeOne);

        final Set<Route> routeThreeConnections = new LinkedHashSet<>();
        routeThreeConnections.add(routeTwo);
        routeThreeConnections.add(routeFour);

        final Set<Route> routeFourConnections = new LinkedHashSet<>();
        routeFourConnections.add(routeOne);
        routeFourConnections.add(routeTwo);
        routeFourConnections.add(routeThree);

        routeFive = new Route();
        routeFive.setLongName("Route Five");

        final Map<Route, Set<Route>> edges = new HashMap<>();
        edges.put(routeOne, routeOneConnections);
        edges.put(routeTwo, routeTwoConnections);
        edges.put(routeThree, routeThreeConnections);
        edges.put(routeFour, routeFourConnections);

        graph = new Graph<>(edges);
    }
}
