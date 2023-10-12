import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main class to handle program flow.
 */
public class Main {
    private static final String ROUTE_URL = "https://api-v3.mbta.com/routes";
    private static final String STOPS_URL_PREFIX = "https://api-v3.mbta.com/stops?filter[route]=";

    /**
     * Main method to run all necessary functions.
     * @param args Command line arguments
     * @throws IOException if we fail to parse JSON
     */
    public static void main(String[] args) throws IOException {
        final List<Route> allRoutes =
                ParsingUtils.parse(ParsingUtils.fetchData(new URL(ROUTE_URL)), Route.class,
                        new RouteAdapterFactory());
        // Filter all routes to only get the "subway" routes
        final List<Route> subwayRoutes =
                allRoutes.stream().filter(r -> r.getTypeInt() == 0 || r.getTypeInt() == 1).collect(Collectors.toList());

        // Set Stops on each Route in subwayRoutes (note that we do not set Stops on all Routes because it surpasses
        // the MBTA's per minute limit on requests, so we only set Stops on relevant Routes)
        for (Route r : subwayRoutes) {
            r.setStops(ParsingUtils.parse(ParsingUtils.fetchData(new URL(STOPS_URL_PREFIX + r.getId())),
                    Stop.class, new StopAdapterFactory()));
        }

        Set<Stop> allStops = getAllStopsOnRoutes(subwayRoutes);

        int maxStops = 0;
        String maxRoute = "";
        int minStops = Integer.MAX_VALUE;
        String minRoute = "";

        // Determine max/min number of Stops
        for (Route r : subwayRoutes) {
            if (r.getStops().size() > maxStops) {
                maxStops = r.getStops().size();
                maxRoute = r.getId();
            }

            if (r.getStops().size() < minStops) {
                minStops = r.getStops().size();
                minRoute = r.getId();
            }
        }

        final Map<Stop, Set<Route>> stopsToRoutes = stopsToRoutes(subwayRoutes);

        // Handle control flow of the program
        System.out.println("See README.md with questions for how to use this program!");
        final Scanner scan = new Scanner(System.in);
        boolean quit = false;
        while (!quit) {
            System.out.println("Which question would you like to run?");
            switch (scan.next()) {
                case "1":
                    System.out.println("Subway routes:");
                    for (Route r : subwayRoutes) {
                        System.out.println(r.getLongName());
                    }
                    break;
                case "2":
                    System.out.println("Most Stops: " + maxRoute + ", " + maxStops);
                    System.out.println("Fewest Stops: " + minRoute + ", " + minStops);
                    System.out.println("List of stops connecting routes:");
                    for (Stop s : stopsToRoutes.keySet()) {
                        if (stopsToRoutes.get(s).size() > 1) {
                            System.out.print(s.getName() + ": ");
                            final Set<Route> stopOnRoutes = stopsToRoutes.get(s);
                            int count = 0;
                            for (Route r : stopOnRoutes) {
                                System.out.print(r.getLongName());
                                count++;
                                if (count != stopOnRoutes.size()) {
                                    System.out.print(", ");
                                }
                            }
                            System.out.println();
                        }
                    }
                    break;
                case "3":
                    final String firstStop = scan.next();
                    final String secondStop = scan.next();
                    try {
                        final List<Route> pathBetween = findPathBetween(firstStop, secondStop, stopsToRoutes, allStops);
                        System.out.println("Path between " + firstStop + " and " + secondStop + ":");
                        for (int i = 0; i < pathBetween.size(); i++) {
                            System.out.print(pathBetween.get(i).getLongName());
                            if (i != pathBetween.size() - 1) {
                                System.out.print(" -> ");
                            } else {
                                System.out.println();
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Entered invalid stop name");
                    }
                    break;
                case "q":
                    quit = true;
                    break;
                default:
                    System.out.println("Valid input is \"1\", \"2\", \"3\", or \"q\".");
                    break;
            }
        }
        scan.close();
        System.out.println("Goodbye!");
    }

    /**
     * Return all Stops on the given list of Routes.
     * @param routes Routes from which we want to get all Stops
     * @return       a Set of all Stops on the given Routes
     */
    private static Set<Stop> getAllStopsOnRoutes(final List<Route> routes) {
        final Set<Stop> allStops = new HashSet<>();
        for (Route r : routes) {
            allStops.addAll(r.getStops());
        }
        return allStops;
    }

    /**
     * Take the given list of Routes and map all Stops to all Routes that they are on.
     * @param routes list of Routes to map
     * @return       map of Stops to all Routes that they are on
     */
    private static Map<Stop, Set<Route>> stopsToRoutes(final List<Route> routes) {
        final Map<Stop, Set<Route>> stopsToRoutes = new HashMap<>();
        for (Route r : routes) {
            for (Stop s : r.getStops()) {
                stopsToRoutes.putIfAbsent(s, new HashSet<>());
                stopsToRoutes.get(s).add(r);
            }
        }

        return stopsToRoutes;
    }

    /**
     * Find a path that connects the Stops with the given names from the given list of connections.
     * @param startName   Name of Stop that is our starting point
     * @param endName     Name of Stop that is our destination
     * @param connections Map of Stops to all Routes that they connect to
     * @param allStops    Set of all stops
     * @return            A list of Routes that connect the given Stops
     */
    private static List<Route> findPathBetween(final String startName, final String endName,
                                               final Map<Stop, Set<Route>> connections, final Set<Stop> allStops) {
        Stop start = null;
        Stop end = null;

        // Get start and end Stops
        for (Stop s : allStops) {
            if (s.getName().equals(startName)) {
                start = s;
            }
            if (s.getName().equals(endName)) {
                end = s;
            }
        }

        if (start == null | end == null) {
            throw new IllegalArgumentException("Invalid stop name.");
        }

        final Set<Route> startConnections = connections.get(start);
        final Set<Route> endConnections = connections.get(end);

        final Map<Route, Set<Route>> routeToAccessibleRoutes = new HashMap<>();

        // Create graph of routes
        for (Stop s : connections.keySet()) {
            Set<Route> routes = connections.get(s);
            for (Route r : routes) {
                routeToAccessibleRoutes.computeIfAbsent(r, k -> new HashSet<>());
                routeToAccessibleRoutes.get(r).addAll(routes);
            }
        }

        final Graph<Route> routesGraph = new Graph<>(routeToAccessibleRoutes);
        return routesGraph.bfs(new ArrayList<>(startConnections), new ArrayList<>(endConnections));
    }
}