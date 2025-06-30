package src.core;

import src.dsa.CustomLinkedList;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;

public class FileParser {
	
	public CustomLinkedList<SearchResult> parseDirectory(String dirPath) {
	    CustomLinkedList<SearchResult> list = new CustomLinkedList<>();
	    File dir = new File(dirPath);
	    if (!dir.isDirectory()) return list;
	
	    File[] entries = dir.listFiles();
	    if (entries == null) return list;
	
	    for (File entry : entries) {
	        if (entry.isDirectory()) {
	            CustomLinkedList<SearchResult> sub = parseDirectory(entry.getPath());
	            for (SearchResult r : sub) list.add(r);
	        }
	    }
	
	    List<File> collected = new ArrayList<>();
	    for (File entry : entries) {
	        if (entry.isFile()) {
	            String name = entry.getName().toLowerCase();
	            if (name.endsWith(".java") ||
	                name.endsWith(".c")    ||
	                name.endsWith(".cpp")  ||
	                name.endsWith(".py")) {
	                collected.add(entry);
	            }
	        }
	    }
	
	    Collections.sort(collected, new Comparator<File>() {
	        @Override
	        public int compare(File f1, File f2) {
	            return Integer.compare(priority(f1), priority(f2));
	        }
	        private int priority(File f) {
	            String n = f.getName().toLowerCase();
	            if (n.endsWith(".java")) return 0;
	            if (n.endsWith(".c"))    return 1;
	            if (n.endsWith(".cpp"))  return 2;
	            return 3;
	        }
	    });
	
	    for (File f : collected) {
	        parseFile(f, list);
	    }
	
	    return list;
	}

    private void parseFile(File file, CustomLinkedList<SearchResult> list) {
    	
        boolean isJava = file.getName().endsWith(".java");
        boolean isC = file.getName().endsWith(".c") || file.getName().endsWith(".cpp");
        boolean isPy = file.getName().endsWith(".py");
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        	
            String line;
            int ln = 0;
            
            while ((line = br.readLine()) != null) {
                ln++;
                String trim = line.trim();
                
                // Comments
                if (handleCComments(isJava, isC, isPy, list, file, ln, trim)) continue;
                
                // Classes
                if (handleClassDefinitions(isJava, isC, isPy, list, file, ln, trim)) continue;
                
                // Java/C functions
                if (isJava || isC) {
                    if (handleJavaCFunctions(list, file, ln, trim)) continue;
                }
                
                // Python
                if (isPy) {
                    if (handlePythonComments(list, file, ln, trim)) continue;
                }
                
                // KEYWORD
                list.add(new SearchResult(
                    SearchResult.Type.KEYWORD,
                    file.getPath(),
                    ln,
                    trim,
                    null
                ));
                
            }
            
        }
        catch (IOException e) {
	        System.err.println("IO Exception Occured ... ");
        }
        
    }
    
    private boolean handleCComments(boolean isJava, boolean isC, boolean isPy, CustomLinkedList<SearchResult> list, File file, int ln, String trim) {

    	if ((isJava || isPy || isC) && trim.startsWith("//")) {
			list.add(new SearchResult(SearchResult.Type.COMMENT, file.getPath(),ln, trim.substring(2).trim(), null));
			return true;
		}
		
		if (isPy && trim.startsWith("#")) {
			list.add(new SearchResult(SearchResult.Type.COMMENT, file.getPath(), ln, trim.substring(1).trim(), null));
			return true;
		}
		
		return false;
    }

    private boolean handleClassDefinitions(boolean isJava, boolean isC, boolean isPy, CustomLinkedList<SearchResult> list, File file, int ln, String trim) {
    	
        if ((isJava || isPy || isC) && trim.contains("class")) {
            String snippet = trim.replace("{", "")
                                .replace(":", "");
            list.add(new SearchResult(SearchResult.Type.CLASS, file.getPath(), ln, snippet, null));
            return true;
        }
        
        return false;
        
    }

    private boolean handleJavaCFunctions(CustomLinkedList<SearchResult> list, File file, int ln, String trim) {
    	
        if (trim.contains("(") && trim.contains(")")) {
            int parenStart = trim.indexOf('(');
            int parenEnd = trim.indexOf(')');
            String params = trim.substring(parenStart + 1, parenEnd).trim();
            String snippet = trim.replace("{", "").trim();
            
            list.add(new SearchResult(SearchResult.Type.FUNCTION, file.getPath(), 
                    ln, snippet, params));
            return true;
        }
        
        return false;
        
    }

    private boolean handlePythonComments(CustomLinkedList<SearchResult> list, File file, int ln, String trim) {
        
        // Function
        if (trim.startsWith("def ")) {
            String snippet = trim.replace(":", "").trim();
            int parenStart = snippet.indexOf('(');
            String params = snippet.substring(parenStart + 1, snippet.indexOf(')')).trim();
            list.add(new SearchResult(SearchResult.Type.FUNCTION, file.getPath(), ln, snippet, params));
            return true;
        }

        return false;
        
    }
    
}