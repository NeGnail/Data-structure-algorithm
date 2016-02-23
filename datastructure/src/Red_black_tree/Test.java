package Red_black_tree;

import java.util.HashSet;

public class Test {
	public static void main(String[] args) {
		RbTree tree=new RbTree();
		for(int i=1;i<1000;i++){
			tree.insert(i);	
			System.out.println(i);
		}
		for(int i=1;i<1000;i=i+4){
			tree.delete(i);
			System.out.println(i);
		}
		
	}
}
