package de.bit.pl2.ex08;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;

public class GraphBuilder {
    public SimpleGraph<Document, DefaultEdge> buildGraph(ArrayList<Document> documentList) {
        SimpleGraph<Document, DefaultEdge> graph = new SimpleGraph<Document, DefaultEdge>(DefaultEdge.class);
        for (Document document : documentList) {
            graph.addVertex(document);
        }
        for (Document document1 : graph.vertexSet()) {
            for (Document document2 : graph.vertexSet()) {
                if (document1 != document2) {
                    boolean isSameJournal = document1.getJournal().equalsIgnoreCase(document2.getJournal());
                    if (isSameJournal) {
                        graph.addEdge(document1, document2);
                    }
                }
            }
        }
        return graph;
    }
}
