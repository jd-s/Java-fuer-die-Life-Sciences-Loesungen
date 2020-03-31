package de.bit.pl2.ex07;

import java.util.HashSet;
import java.util.Set;
//import org.jgrapht.DirectedGraph; // not working with latest JGrapht version (version1.3.0)
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.SingleSourcePaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

public class App
{
	private static Set<String> getBaseNodesAsSet(DefaultDirectedGraph<String, DefaultEdge> graph) 
	{
		Set<String> base = new HashSet<String>();
		for (String vertex : graph.vertexSet()) 
		{
			if (graph.inDegreeOf(vertex) == 0) 
			{
				base.add(vertex);
			}
		}
		return base;
	}

	private static Set<String> getTopNodesAsSet(DefaultDirectedGraph<String, DefaultEdge> graph)
	{
		Set<String> top = new HashSet<String>();
		for (String vertex : graph.vertexSet())
		{
			if (graph.outDegreeOf(vertex) == 0) 
			{
				top.add(vertex);
			}
		}
		return top;
	}

	private static int heightOfNode(DefaultDirectedGraph<String, DefaultEdge> graph, String node)
	{
		Set<String> baseNodes = getBaseNodesAsSet(graph);
		DijkstraShortestPath<String, DefaultEdge> dijkstra = new DijkstraShortestPath<>(graph);
		// Initialize height with the largest possible value
		int height = Integer.MAX_VALUE;
		for (String i : baseNodes)
		{
			SingleSourcePaths<String, DefaultEdge> paths = dijkstra.getPaths(i);
			if (paths.getPath(node) != null) 
			{
				if (paths.getPath(node).getLength() < height)
				{
					height = paths.getPath(node).getLength();
				}
			}
		}
		return height;
	}

	public static void main(String[] args) 
	{
		DefaultDirectedGraph<String, DefaultEdge> foodGraph = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);

		foodGraph.addVertex("Zooplankton");
		foodGraph.addVertex("Phytoplankton");
		foodGraph.addVertex("Submerged Aquatic Vegetation (SAV)");
		foodGraph.addVertex("Vegetation");

		foodGraph.addVertex("Benthic Invertebrates");
		foodGraph.addVertex("Herbivorous Ducks");
		foodGraph.addVertex("Geese and Mute Swans");

		foodGraph.addVertex("Small Planktivorous Fish");
		foodGraph.addVertex("Bivalves");
		foodGraph.addVertex("Tundra Swan");

		foodGraph.addVertex("Large Piscivorous Fish");
		foodGraph.addVertex("Sea Ducks");

		foodGraph.addVertex("Gulls and Terns");
		foodGraph.addVertex("Wading Birds");
		foodGraph.addVertex("Bald Eagle");
		foodGraph.addVertex("Osprey");

		foodGraph.addEdge("Phytoplankton", "Benthic Invertebrates");
		foodGraph.addEdge("Phytoplankton", "Bivalves");
		foodGraph.addEdge("Phytoplankton", "Small Planktivorous Fish");

		foodGraph.addEdge("Bivalves", "Sea Ducks");
		foodGraph.addEdge("Bivalves", "Herbivorous Ducks");
		foodGraph.addEdge("Bivalves", "Tundra Swan");

		foodGraph.addEdge("Vegetation", "Tundra Swan");
		foodGraph.addEdge("Vegetation", "Geese and Mute Swans");

		foodGraph.addEdge("Zooplankton", "Small Planktivorous Fish");
		foodGraph.addEdge("Zooplankton", "Bivalves");

		foodGraph.addEdge("Small Planktivorous Fish", "Wading Birds");
		foodGraph.addEdge("Small Planktivorous Fish", "Large Piscivorous Fish");
		foodGraph.addEdge("Small Planktivorous Fish", "Gulls and Terns");

		foodGraph.addEdge("Large Piscivorous Fish", "Osprey");
		foodGraph.addEdge("Large Piscivorous Fish", "Bald Eagle");

		foodGraph.addEdge("Submerged Aquatic Vegetation (SAV)", "Herbivorous Ducks");
		foodGraph.addEdge("Benthic Invertebrates", "Sea Ducks");
		foodGraph.addEdge("Sea Ducks", "Bald Eagle");

		Set<String> foodChainBaseNodes = getBaseNodesAsSet(foodGraph);
		System.out.println("Base of the food chain organisms: " + foodChainBaseNodes.toString());

		System.out.println();

		Set<String> foodChainTopNodes = getTopNodesAsSet(foodGraph);
		System.out.println("Apex predators: " + foodChainTopNodes.toString());
		System.out.println();

		System.out.println("The height of the node, 'Bald eagle' is: " + heightOfNode(foodGraph, "Bald Eagle"));
		System.out.println();

		for (String node : foodGraph.vertexSet()) 
		{
			System.out.println(node + " height = " + heightOfNode(foodGraph, node));
		}
	}
}
