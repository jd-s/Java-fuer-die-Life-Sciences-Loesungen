package de.bit.pl2.ex05;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class App {
    public static void main(String[] args) throws FileNotFoundException {

        if (args.length < 3) {
            System.out.println("Please call with three parameters: <input path> <Protein1> <Protein2>");
            return;
        }

        String inputPath = args[0];
        String inputProtein1 = args[1];
        String inputProtein2 = args[2];

        Pattern pattern = Pattern.compile("activat*|catalyz*|complex*|inhibit*");
        Scanner scanner = new Scanner(new File(inputPath));
        DirectedWeightedMultigraph<String, DefaultWeightedEdge> directedGraph = new DirectedWeightedMultigraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        if (scanner.hasNextLine()) {
            scanner.nextLine(); // skip csv header

            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                String[] splitLine = (nextLine.split("\t"));
                Matcher matcher = pattern.matcher(splitLine[2]);
                if (matcher.find()) {
                    directedGraph.addVertex(splitLine[0]);
                    directedGraph.addVertex(splitLine[1]);
                    if (splitLine[3].matches("[<|\\|]-")) {
                        DefaultWeightedEdge edge = directedGraph.addEdge(splitLine[1], splitLine[0]);
                        directedGraph.setEdgeWeight(edge, Double.parseDouble(splitLine[4]));
                    } else if (splitLine[3].matches("-[>|\\|]")) {
                        DefaultWeightedEdge edge = directedGraph.addEdge(splitLine[0], splitLine[1]);
                        directedGraph.setEdgeWeight(edge, Double.parseDouble(splitLine[4]));
                    } else if (splitLine[3].matches("\\s*-\\s*")) {
                        DefaultWeightedEdge edge1 = directedGraph.addEdge(splitLine[0], splitLine[1]);
                        directedGraph.setEdgeWeight(edge1, Double.parseDouble(splitLine[4]));
                        DefaultWeightedEdge edge2 = directedGraph.addEdge(splitLine[1], splitLine[0]);
                        directedGraph.setEdgeWeight(edge2, Double.parseDouble(splitLine[4]));
                    }
                }
            }
        }
        try {
            int k = directedGraph.vertexSet().size();
            KShortestPaths kShortestPaths = new KShortestPaths(directedGraph, k);
            List<GraphPath> shortestPaths = kShortestPaths.getPaths(inputProtein1, inputProtein2);
            boolean status = true;
            int i = 1;
            while (status) {
                if (shortestPaths.get(i).getWeight() != shortestPaths.get(i - 1).getWeight()) {
                    status = false;
                } else {
                    if (i == shortestPaths.size()-1) {
                        break;
                    } else {
                        i += 1;
                    }
                }
            }
            for (int j = 0; j < i; j++) {
                List<String> outputList = shortestPaths.get(j).getVertexList();
                String output = outputList.get(0);
                for (int m = 1; m < outputList.size(); m++) {
                    output = output + " --> ";
                    output = output + outputList.get(m);
                }
                System.out.println(output);
            }

        } catch (IllegalArgumentException e) {
            System.out.println("No path between " + inputProtein1 + " and " + inputProtein2 + ".");
        }

    }
}
