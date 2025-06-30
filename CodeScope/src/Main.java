package src;

import src.core.FileParser;
import src.core.SearchEngine;
import src.core.SearchResult;
import src.dsa.CustomLinkedList;

public class Main {
    public static void main(String[] args) {
        FileParser parser = new FileParser();
        CustomLinkedList<SearchResult> results = parser.parseDirectory("src/Files");
        SearchEngine engine = new SearchEngine(results);
        engine.processQueries("queries.txt", "results.txt");
        System.out.println("Processing complete. Check results.txt");
    }
}
