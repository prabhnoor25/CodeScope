package src.dsa;

import java.util.ArrayList;
import java.util.List;

public class CustomTree<K extends Comparable<K>,V>{
	
    private class Node{
    	K key;
    	V val;
    	Node l,r;
    	
    	Node(K k,V v){
    		key=k;
    		val=v;
    	}
    }
    
    private Node root;
    
    public void put(K k,V v){
    	root=put(root,k,v);
    }
    
    private Node put(Node n,K k,V v){
    	if(n==null)return new Node(k,v);
    	int c=k.compareTo(n.key);
    	if(c<0) n.l=put(n.l,k,v);
    	else if(c>0) n.r=put(n.r,k,v);
    	else n.val=v;
    	return n;
    }
    
    public List<V> inOrderValues(){
    	List<V> l=new ArrayList<>();
    	inOrder(root,l);
    	return l;
    }
    
    private void inOrder(Node n,List<V> l){
    	if(n==null)return;
    	inOrder(n.l,l);
    	l.add(n.val);
    	inOrder(n.r,l);
    }
    
}