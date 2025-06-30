package src.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import src.dsa.CustomLinkedList;
import src.dsa.CustomTree;
import src.dsa.CustomGraph;

public class SearchEngine {
	
	private CustomLinkedList<SearchResult> all;
	
	public SearchEngine(CustomLinkedList<SearchResult> all) {
	  this.all = all;
	}
	
	public void processQueries(String qf, String rf) {
	    try (
	        BufferedReader br = new BufferedReader(new FileReader(qf));
	        BufferedWriter bw = new BufferedWriter(new FileWriter(rf))
	    ) {
	        String q;
	        while ((q = br.readLine()) != null) {
	            bw.write("# Query: " + q); bw.newLine();
	            bw.write("==================================="); bw.newLine(); bw.newLine();
	
	            CustomLinkedList<SearchResult> filtered = new CustomLinkedList<>();
	            
	            for (SearchResult r : all) {
	                if (q.equals("//") && r.getType() == SearchResult.Type.COMMENT && (r.getFile().endsWith(".java") || r.getFile().endsWith(".c") || r.getFile().endsWith(".cpp") )) {
	                    filtered.add(r);
	                }
	                else if (q.equals("#") && r.getType() == SearchResult.Type.COMMENT && r.getFile().endsWith(".py")) {
	                    filtered.add(r);
	                }
	                else {
	                    String snippet = r.getSnippet();
	                    if (snippet.contains(q)) {
	                        if (r.getType() == SearchResult.Type.CLASS || r.getType() == SearchResult.Type.FUNCTION) {
	                            filtered.add(r);
	                        }
	                        else {
	                        	// KEYWORD
	                        	filtered.add(r);
	                        }
	                    }
	                }
	            }
	
	            CustomTree<String, SearchResult> tree = new CustomTree<>();
	            for (SearchResult r : filtered) {
	            	String key = String.format("%s|%s:%d", r.getSnippet(), r.getFile(), r.getLine());
	            	tree.put(key, r);
	            }
	
	            CustomGraph<String> graph = new CustomGraph<>();
	            for (SearchResult r : filtered) {
	                graph.addNode(r.getFile());
	            }
	            int nodeCount = graph.getNodes().size();
	
	            for (SearchResult r : tree.inOrderValues()) {
	                bw.write("File: " + r.getFile()); bw.newLine();
	                bw.write("Type: " + r.getType()); bw.newLine();
	                if (r.getType() == SearchResult.Type.FUNCTION) {
	                    bw.write("Parameters: " + r.getParams()); bw.newLine();
	                }
	                bw.write("Line: " + r.getLine()); bw.newLine();
	                bw.write("Snippet:"); bw.newLine();
	                bw.write(r.getSnippet()); bw.newLine(); bw.newLine();
	            }
	
	            bw.write("Graph Node Count: " + nodeCount); bw.newLine(); bw.newLine();
	        }
	    } catch (IOException e) {
	        System.err.println("IO Exception Occured ... ");
	    }
	}
}