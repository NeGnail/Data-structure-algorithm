package B_plus_tree;

public class Test {
	public static void main(String[] args) throws BPlusTreeException {
		BPlusTree<Integer> tree=new BPlusTree<Integer>();
		for(int i=0;i<1000;i++){
			System.out.println(i);
			tree.insert(i);
		}
		System.out.println(tree.findByRoot(465).keys[0]);
		System.out.println(tree.findByRoot(465).keys[1]);
		System.out.println(tree.findByRoot(465).keys[2]);
		
		System.out.println(tree.findByData(465).keys[0]);
		System.out.println(tree.findByData(465).keys[1]);
		System.out.println(tree.findByData(465).keys[2]);
		
		Node root=tree.getRoot();
	}
}
