package src.core;

public class SearchResult {
    public enum Type {
    	FUNCTION, CLASS, COMMENT, KEYWORD
    }
    private Type type;
    private String file;
    private int line;
    private String snippet;
    private String params;

    public SearchResult(Type type, String file, int line, String snippet, String params) {
        this.type = type;
        this.file = file;
        this.line = line;
        this.snippet = snippet;
        this.params = params;
    }

    public Type getType() { return type; }
    public String getFile() { return file; }
    public int getLine() { return line; }
    public String getSnippet() { return snippet; }
    public String getParams() { return params; }
}
