import java.util.*;

/**
 * This class represents a Graph of objects of type T.
 */
public class Graph<T> {
    private final Map<T, Set<T>> edges;

    public Graph(final Map<T, Set<T>> edges) {
        if (edges == null || edges.isEmpty()) {
            throw new IllegalArgumentException("There must be edges in the graph");
        }
        this.edges = edges;
    }

    /**
     * Perform a breadth-first search to find a path from one of the given starts to one of the given ends.
     * @param starts List of all potential start nodes in the graph
     * @param ends   List of all potential end nodes in the graph
     * @return       A List representing a path from one of the start nodes to one of the end nodes
     */
    public List<T> bfs(final List<T> starts, final List<T> ends) {
        if (starts == null || starts.isEmpty()) {
            throw new IllegalArgumentException("Must be at least one start node.");
        }

        // If the end contains any of the starts, we are done immediately because that is the path
        for (T start : starts) {
            if (ends.contains(start)) {
                return new ArrayList<>(Collections.singletonList(start));
            }
        }

        final Map<T, Boolean> visited = new HashMap<>();
        final List<T> queue = new LinkedList<>();
        T end = null;
        final Map<T, T> parents = new HashMap<>();
        visited.put(starts.get(0), true);
        queue.add(starts.get(0));

        // Loop through all connected nodes until we reach an end node or have iterated through all connected nodes
        while (!queue.isEmpty()) {
            T node = queue.remove(0);
            if (edges.get(node) != null) {
                for (T t : edges.get(node)) {
                    if (visited.get(t) == null || !visited.get(t)) {
                        visited.put(t, true);
                        queue.add(t);
                        parents.put(t, node);
                        if (ends.contains(t)) {
                            end = t;
                            break;
                        }
                    }
                }
            }
        }
        return backtrace(parents, end);
    }

    /**
     * Given an end object, trace back the path that led to that object based on the Map of parent objects.
     * @param parents Map from an object to the parent that led to that object
     * @param end     The final object that we arrived at
     * @return        A List of objects representing the path from some start to the given end
     */
    private List<T> backtrace(final Map<T, T> parents, final T end) {
        final List<T> path = new ArrayList<>();
        T parent = end;
        while (parent != null) {
            path.add(parent);
            parent = parents.get(parent);
        }

        Collections.reverse(path);
        return path;
    }
}
