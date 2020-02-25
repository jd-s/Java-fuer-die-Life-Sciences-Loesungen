package de.bit.pl2.ex08;

import org.jgrapht.alg.clique.BronKerboschCliqueFinder;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CliqueFinder {
    public CliqueFinder(SimpleGraph<Document, DefaultEdge> graph) {
        List<Set<Document>> cliques = findCliques(graph);
        printClique(cliques);
        printYearData(cliques);
    }

    private static int getMinimumYear(Iterable<Document> documents) {
        int minYear = Integer.MAX_VALUE;
        for (Document item : documents) {
            if (item.getYear() < minYear) {
                minYear = item.getYear();
            }
        }
        return minYear;
    }

    private static int getMaxYear(Iterable<Document> documents) {
        int maxYear = Integer.MIN_VALUE;
        for (Document item : documents) {
            if (item.getYear() > maxYear) {
                maxYear = item.getYear();
            }
        }
        return maxYear;
    }

    private static int getAverageYear(Iterable<Document> documents) {
        int sum = 0;
        int count = 0;
        for (Document item : documents) {
            sum += item.getYear();
            count++;
        }
        return Math.round((float) sum / (float)count);
    }

    private static List<Set<Document>> findCliques(SimpleGraph<Document, DefaultEdge> graph) {
        List<Set<Document>> cliques = new ArrayList<>();
        BronKerboschCliqueFinder<Document, DefaultEdge> cliqueFinder = new BronKerboschCliqueFinder<>(graph);
        Iterator<Set<Document>> iterator = cliqueFinder.maximumIterator();
        while (iterator.hasNext()) {
            cliques.add(iterator.next());
        }
        return cliques;
    }

    public void printYearData(List<Set<Document>> cliques) {
        for (Set<Document> clique : cliques) {
            String minYear = Integer.toString(getMinimumYear(clique));
            System.out.println("\nMinimal Year: " + minYear);

            String maxYear = Integer.toString(getMaxYear(clique));
            System.out.println("Maximal Year: " + maxYear);

            String avgYear = Integer.toString(getAverageYear(clique));
            System.out.println("Average Year: " + avgYear);
        }
    }

    public void printClique(List<Set<Document>> cliques) {
        for (Set<Document> clique : cliques) {
            System.out.println("Number of documents in the clique: " + clique.size());
            for (Document item : clique) {
                System.out.println(item.getTitle() + "," + item.getJournal() + "," + item.getYear());
            }
        }
    }
}

