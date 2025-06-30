package src.dsa;

public class CustomGraph<T>{
    private CustomHashMap<T, CustomLinkedList<T>> map = new CustomHashMap<>();
    
    public void addNode(T n){
    	if(map.get(n)==null) map.put(n,new CustomLinkedList<>());
    }
    
    public CustomLinkedList<T> getNodes(){
    	CustomLinkedList<T> l=new CustomLinkedList<>();
    	for(T k:map.keySet()) l.add(k);
    	return l;
    }
    
}
