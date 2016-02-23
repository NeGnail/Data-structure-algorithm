package B_plus_tree;

public class Node<T>  {
	public Node(){
		
	}
	public Node(T[] keys) {
		this.keys=keys;
	}
	protected InternalNode<T> parent;
	protected T[] keys;
	
	
}
