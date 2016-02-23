package Binary_tree;

public class Node<T> {
	public int iData;
	public T dData;
	public Node leftChild;
	public Node rightChild;
	
	public void displayNode(){
		System.out.print("{");
		System.out.print(iData);
		System.out.print(",");
		System.out.print(dData);
		System.out.print("}");
	}
}
