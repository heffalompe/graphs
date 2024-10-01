import java.util.*;

public class Vektetgraf {

    public static class Graph {
        private Set<String> verticies; //noder
        private Map<String, Set<String>> edges; //kanter;
        private Map<String, Integer> weights; //vekter
        private Map<String, Integer> indegree; //graden til en node

        public Graph(Set<String> verticies, Map<String, Set<String>> edges, Map<String, Integer> weights, Map<String, Integer> indegree) {
            this.verticies = verticies;
            this.edges = edges;
            this.weights = weights;
            this.indegree = indegree;
        }

        public Set<String> getVerticies() {
            return verticies;
        }

        public Set<String> getNeighbors(String node) {
            return edges.getOrDefault(node, new HashSet<>());
        }

        public Map<String, Set<String>> getEdges() {
            return edges;
        }

        public Map<String, Integer> getWeights() {
            return weights;
        }

        //Hjelpe metode for dijkstra
        public int getWeight(String u, String v) {
            return weights.getOrDefault(u + "-" + v, Integer.MAX_VALUE);
        }

        public int getIndegree(String node) {
            return indegree.getOrDefault(node, 0);
        }

        public void decreaseIndegree(String node) {
            indegree.put(node, indegree.get(node) - 1);
        }
    }

    public static Graph buildGraph(Scanner scanner) {
        Set<String> verticies = new HashSet<>();
        Map<String, Set<String>> edges = new HashMap<>();
        Map<String, Integer> weights = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();

        System.out.println("Enter graph edges in the format 'u v' (enter END to finish):");

        while (scanner.hasNextLine()) {
            String linje = scanner.nextLine();
            if (linje.trim().equalsIgnoreCase("END")) {
                break;
            }
            String[] biter = linje.split(" ");
            if (biter.length != 3) {
                throw new IllegalArgumentException("Line format must be 'u v weight'");
            }

            String u = biter[0];
            String v = biter[1];
            int weight = Integer.parseInt(biter[2]);

            verticies.add(u);
            verticies.add(v);

            edges.putIfAbsent(u, new HashSet<>());
            edges.putIfAbsent(v, new HashSet<>());

            edges.get(u).add(v);
            edges.get(v).add(u);

            weights.put(u + "-" + v, weight);
            weights.put(v + "-" + u, weight);

            indegree.putIfAbsent(v, 0);
            indegree.put(v, indegree.get(v) + 1);
            indegree.putIfAbsent(u, 0);

        }
        return new Graph(verticies, edges, weights, indegree);
    }

    public static void DFSVisit(Graph G, String s, Set<String> visited) {
        System.out.println("Visited: " + s);
        visited.add(s);

        for (String v : G.getNeighbors(s)) {
            if (! visited.contains(v)) {
                DFSVisit(G, v, visited);
            }
        }
    }

    public static void DFSFull(Graph G) {
        Set<String> visited = new HashSet<>();
        for (String v : G.getVerticies()) {
            if (! visited.contains(v)) {
                DFSVisit(G, v, visited);
            }
        }
    }

    public static void BFSVisit(Graph G, String s, Set<String> visited) {
        System.out.println("Visited: " + s);
        visited.add(s);
        Queue<String> queue = new LinkedList<>();
        queue.add(s);

        while (! queue.isEmpty()) {
            String u = queue.poll();
            for (String v : G.getNeighbors(u)) {
                if (! visited.contains(v)) {
                    System.out.println("Visited: " + v);
                    visited.add(v);
                    queue.add(v);
                }
            }
        }
    }

    public static void BFSFull(Graph G) {
        Set<String> visited = new HashSet<>();
        for (String v : G.getVerticies()) {
            if (! visited.contains(v)) {
                BFSVisit(G, v, visited);
            }
        }
    }

    public static List<String> TOPSort(Graph G) {
        Deque<String> stack = new ArrayDeque<>();
        List<String> output = new ArrayList<>();

        for (String v : G.getVerticies()) {
            if (G.getIndegree(v) == 0) {
                stack.push(v);
            }
        }
        while (! stack.isEmpty()) {
            String u = stack.pop();
            output.add(u);

            for (String v : G.getNeighbors(u)) {
                G.decreaseIndegree(v);
                if (G.getIndegree(v) == 0) {
                    stack.push(v);
                }
            }
        }
        if (output.size() < G.getVerticies().size()) {
            throw new IllegalArgumentException("Graph contains a cycle and cannot be topologically ordered");
        }
        return output;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Graph graph = buildGraph(scanner);

        System.out.println("Verticies: " + graph.getVerticies());
        System.out.println("Edges: " + graph.getEdges());
        System.out.println("Weights: " + graph.getWeights());

        System.out.println("Starting DFS traversal: ");
        DFSFull(graph);

        System.out.println("Starting BFS traversal: ");
        BFSFull(graph);

       

        scanner.close();
    
    }
}