package de.bit.pl2.ex07;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.io.EdgeProvider;
import org.jgrapht.io.GmlImporter;
import org.jgrapht.io.ImportException;
import org.jgrapht.io.VertexProvider;

import java.io.File;

/**
 * This class builds a graph from a GML file and returns the connectance and complexity of the graph.
 * The filepath needs to be given as a command line argument.
 * The result is printed to stdout
 *
 * @params args The filepath of GML file
 */
public class SocialRelations {
    public static void main(String[] args) {
        try {
            String inputPath = args[0];

            DefaultDirectedGraph graph = generateGraph(inputPath);
            float connectance = getConnectance(graph);
            float complexity = getComplexity(graph);

            System.out.println("Connectance: " + connectance);
            System.out.println("Complexity: " + complexity);
        } catch(ArrayIndexOutOfBoundsException e) {
            System.err.println("Argument error: Please provide the file path to a gml graph.");
            System.exit(1);
        }
    }

    /**
     * Returns a DefaultDirectedGraph object from a gml file
     *
     * @param filename the name of the gml file containing the graph contents
     * @return graph the DefaultDirectedGraph object from the file
     */
    public static DefaultDirectedGraph<String, DefaultEdge> generateGraph(String filename) {
        final DefaultDirectedGraph<String, DefaultEdge> graph = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);

        VertexProvider<String> vp = (label, attributes) -> label;
        EdgeProvider<String, DefaultEdge> ep =
                (from, to, label, attributes) -> graph.getEdgeSupplier().get();

        try {
            GmlImporter<String, DefaultEdge> importer = new GmlImporter<>(vp, ep);
            importer.importGraph(graph, new File(filename));
        } catch (ImportException e) {
            e.printStackTrace();
        }
        return graph;
    }

    /**
     * Computes the connectance of the graph, defined as ratio of actual edges to possible edges
     *
     * @param graph
     * @return connectance the connectance of the graph
     */
    public static float getConnectance(DefaultDirectedGraph graph) {
        return (float) graph.edgeSet().size() / (float) (graph.vertexSet().size() * graph.vertexSet().size());
    }

    /**
     * Computes the complexity of the graph, defined as the average number of edges per node
     *
     * @param graph
     * @return complexity the complexity of the graph
     */
    public static float getComplexity(DefaultDirectedGraph graph) {
        int complexitySum = 0;
        for (Object vertex : graph.vertexSet()) {
            complexitySum += graph.inDegreeOf(vertex);
            complexitySum += graph.outDegreeOf(vertex);
        }
        return (float) complexitySum / (float) graph.vertexSet().size();
    }
}
