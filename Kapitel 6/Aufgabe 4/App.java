package de.bit.pl2.ex08;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.File;
import java.util.ArrayList;

public class App {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Please call with one parameter: <path-to-csv-input-file>");
            return;
        }
        String inputPath = args[0];

        // read documents into list
        FileParser fileParser = new FileParser();
        ArrayList<Document> documentList = fileParser.readDocumentsFromFile(inputPath);

        // build and write similarity graph
        GraphBuilder graphBuilder = new GraphBuilder();
        SimpleGraph<Document, DefaultEdge> graph = graphBuilder.buildGraph(documentList);

        // find largest clique and print clique and calculations
        CliqueFinder cliqueFinder = new CliqueFinder(graph);

    }
}
