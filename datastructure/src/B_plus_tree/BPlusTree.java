package B_plus_tree;

import java.lang.reflect.Array;
/**

 * @author hp
 *
 * @param <T>
 */
public class BPlusTree<T> {
	private static final int defaultFactor=5;
	private int factor;
	private int minKeysNumber;
	private int maxKeysNumber;
	
	private Node<T> root;
	private Node<T> data;
	
	public BPlusTree(){
		this(defaultFactor);
	}
	public BPlusTree(int factor){
		this.factor=factor;
		this.minKeysNumber=((Double)Math.ceil(1.0 * factor/2)).intValue();//不能小于此数
		this.maxKeysNumber=factor;//不能大于此数
	}
	
	
	
	public Node<T> getRoot() {
		return root;
	}
	
	/**
	 * 提供给用户的方法，插入k
	 * @param k
	 * @throws BPlusTreeException
	 */
	public void insert(T k) throws BPlusTreeException{
		Node inNode=root;
		Node resultNode=null;
		Node parentNode=null;
		while(inNode instanceof InternalNode){
			int index=getChildNodeIndexForInsert(k, (InternalNode) inNode);
			parentNode=inNode;
			inNode=((InternalNode)inNode).childNode[index];
		}
		resultNode=insertLeafNode(inNode,k);
		if(resultNode.parent==null){
			resultNode.parent=(InternalNode) parentNode;
			if(parentNode==null){
				root= resultNode;
				data=resultNode;
			}
		}
		adjustFromLeafNode(k,resultNode);
	}
	
	/**
	 * 根据k查找某个节点
	 * 查找方式为从根节点向下查找
	 * @param k
	 * @return
	 * @throws BPlusTreeException 
	 */
	public Node findByRoot(T k) throws BPlusTreeException{
		Node nowNode=root;
		while(nowNode instanceof InternalNode){
			int index=getChildNodeIndexForSelect(k, nowNode);
			nowNode=((InternalNode)nowNode).childNode[index];
		}
		return nowNode;
	}
	/**
	 * 根据k查找某个节点
	 * 查找方式为从data引用向后依次查找
	 * @param k
	 * @return
	 */
	public Node findByData(T k){
		Node nowNode=data;
		while(!isContain(nowNode,k)){
			nowNode=((LeafNode)nowNode).next;
		}
		return nowNode;
	}
	
	/**
	 * 删除k值，并调整好树结构。返回包含该k的Node
	 * @param k
	 * @return
	 * @throws BPlusTreeException 
	 */
	public Node remove(T k) throws BPlusTreeException{
		Node nowNode=delete(k);
		adjustForDelete(nowNode,k);
		return data;
		
	}
	
	/**
	 * nowNode已经删除了k，该方法有nowNode进行调整树结构以保证b+树的性质
	 * @param nowNode
	 * @param k
	 */
	private void adjustForDelete(Node nowNode, T k) {
		//①若删除k后的节点满足树的性质则判断
		//若删除的k为该节点中最小的k则需将该节点的所有祖先节点的key做相应调整，否则直接返回
		if(nowNode.keys.length>=minKeysNumber){
			if( ((Comparable)k).compareTo(nowNode.keys[0])>0 ){
				return;
			}else{
				adjustForNewK(k);
				return;
			}
		//②若删除k后的节点不满足树的性质则判断
		//若可以从兄弟节点获取一个k则获取
		//若不可以从兄弟节点获取一个k则进行合并
		}else{
//			if(getKFromBrother(nowNode)){
//				
//			}
		}
	}
	
	/**
	 * 从兄弟节点获取一个k，如果获取之后不会破坏树的性质则获取，返回true
	 * 				    如果获取之后会破坏树的性质则不获取，返回false
	 * @param nowNode
	 * @return
	 * @throws BPlusTreeException 
	 */
	private boolean getKFromBrother(Node nowNode) throws BPlusTreeException {
		Node parentNode=nowNode.parent;
		int index=getChildNodeIndexForSelect((T) nowNode.keys[0], parentNode);
		
		return false;
	}
	/**
	 * 从k所在的节点中删除k值，此操作并不调整数结构。
	 * 返回k所在的节点
	 * @param k
	 * @return
	 * @throws BPlusTreeException 
	 */
	private Node delete(T k) throws BPlusTreeException {
		Node nowNode=findByRoot(k);
		T[] keys=(T[]) nowNode.keys;
		int index=getChildNodeIndexForSelect(k, nowNode);
		T[] newKeys=(T[]) Array.newInstance(keys[0].getClass(),keys.length-1);
		System.arraycopy(keys,0,newKeys,0,index);
		System.arraycopy(keys,index+1,newKeys,index,keys.length-index-1);
		nowNode.keys=newKeys;
		return nowNode;
	}
	/**
	 * 判断nowNode的keys中是否含有k
	 * @param nowNode
	 * @param k
	 * @return
	 */
	private boolean isContain(Node nowNode, T k) {
		boolean isContain=false;
		for(Object nodeK:nowNode.keys){
			if( ((Comparable)k).compareTo(nodeK)==0 ){
				isContain=true;
			}
		}
		return isContain;
	}
	/**
	 * 从该叶子节点开始向上不断分裂，因为第一次分裂的是叶子节点，所以方法名中有LeafNode，并不代表每一次分裂的都是叶子节点。
	 * @param k
	 * @param node
	 * @throws BPlusTreeException
	 */
	private void adjustFromLeafNode(T k, Node node) throws BPlusTreeException {
		Node nowNode=node;
		while(true){
			//算法①
			if(nowNode.parent==null&&nowNode.keys.length<=maxKeysNumber){
				return;
				//算法②
			}else if(nowNode.parent!=null&&nowNode.keys.length<=maxKeysNumber){
				if(k!=nowNode.keys[0]){
					return;
				}else{
					adjustForNewK(k);
					return;
				}
				//算法③
			}else if(nowNode.keys.length>maxKeysNumber){
				Node[] nodes=splitNode(nowNode);
				Node node1=nodes[0];
				Node node2=nodes[1];
				if(nowNode.parent==null){
					coustructRootFromTowNode(node1,node2);
					return;
				}else{
					Node p=null;
					if(node1.keys[0]==k){
						p=adjustForNewK(k);
					}else{
						p=nowNode.parent;
					}
					insertNewK(p,node1,node2);
					nowNode=p;
				}
			
			}
		}
		
	}
	/**
	 * 往指定节点中插入新的key及相应的子节点索引
	 * @param p
	 * @param comparable
	 * @throws BPlusTreeException 
	 */
	private void insertNewK(Node p,Node node1,Node node2) throws BPlusTreeException {
		T k=(T) node2.keys[0];
		T[] keys=(T[]) p.keys;
		T[] newParentKeys=(T[]) Array.newInstance(keys[0].getClass(), keys.length+1);
		int index=getKeysIndex((T) k, p);
		System.arraycopy(keys,0 , newParentKeys, 0, index);
		newParentKeys[index]=(T) k;
		System.arraycopy(keys,index , newParentKeys,index+1 , keys.length-index);
		p.keys=newParentKeys;
		
		Node[] childNode=((InternalNode)p).childNode;
		childNode[index-1]=node1;
		Node[] newChildNode=(Node[]) Array.newInstance(childNode[0].getClass(),childNode.length+1);
		System.arraycopy(childNode, 0, newChildNode, 0,index);
		newChildNode[index]=node2;
		System.arraycopy(childNode, index, newChildNode, index+1, childNode.length-index);
		((InternalNode)p).childNode=newChildNode;
	}
	/**
	 * 由已知的两个节点构造一个父节点(根节点)
	 * 该父节点与两个子节点的关系在此方法中会构造好
	 * @param node1
	 * @param node2
	 */
	private void coustructRootFromTowNode(Node node1, Node node2) {
		T key1=(T) node1.keys[0];
		T key2=(T) node2.keys[0];
		T[] keys=(T[]) Array.newInstance(key1.getClass(), 2);
		keys[0]=key1;
		keys[1]=key2;
		InternalNode newNode=new InternalNode(keys);
		this.root=newNode;
		Node[] childNodes=new Node[2];
		((InternalNode)root).childNode=childNodes;
		((InternalNode)root).childNode[0]=node1;
		((InternalNode)root).childNode[1]=node2;
		node1.parent=node2.parent=(InternalNode) newNode;
		
	}
	
	/**
	 * 将指定节点分裂为两个节点，两个节点的parent和next引用在此方法中会处理好。即两个节点同其子节点的一切关系都处理好，与父节点的关系只处理了两个节点的parent引用，并没有对父节点的子节点引用及父节点的k进行处理。
	 * @param nowNode
	 * @return
	 */
	private Node[] splitNode(Node nowNode) {
		int index=nowNode.keys.length/2-1;//index为分割点的索引，index为前半部分
		T[] keys1=(T[]) Array.newInstance(nowNode.keys[0].getClass(), index+1);
		T[] keys2=(T[]) Array.newInstance(nowNode.keys[0].getClass(), nowNode.keys.length-index-1);
		System.arraycopy(nowNode.keys,0 , keys1, 0, keys1.length);
		System.arraycopy(nowNode.keys,index+1 , keys2, 0, keys2.length);
		
		Node node1=null;
		Node node2=null;
		if(nowNode instanceof LeafNode){
			node1=new LeafNode(keys1);
			node2=new LeafNode(keys2);
			((LeafNode)node1).next=(LeafNode) node2;
			((LeafNode)node2).next=((LeafNode) nowNode).next;
			LeafNode preNode=findPreNode(nowNode);
			if(preNode==null){
				data=node1;
			}else{
				preNode.next=(LeafNode) node1;
			}
		}else if(nowNode instanceof InternalNode){
			node1=new InternalNode(keys1);
			node2=new InternalNode(keys2);
			Node[] childNode1=new Node[index+1];
			Node[] childNode2=new Node[nowNode.keys.length-index-1];
			for(int i=0;i<=index;i++){
				childNode1[i]=((InternalNode) nowNode).childNode[i];
				childNode1[i].parent=(InternalNode) node1;
			}
			for(int i=index+1;i<((InternalNode)nowNode).childNode.length;i++){
				childNode2[i-index-1]=((InternalNode) nowNode).childNode[i];
				childNode2[i-index-1].parent=(InternalNode) node2;
			}
			((InternalNode) node1).childNode=childNode1;
			((InternalNode) node2).childNode=childNode2;
			
		}
		node1.parent=node2.parent=nowNode.parent;
		
		
		return new Node[]{node1,node2};
	}
	
	/**
	 * 查找指定节点前驱节点
	 * 如果指定节点是data，那么返回空。
	 * @param nowNode
	 * @return
	 */
	private LeafNode findPreNode(Node node) {
		LeafNode nowNode=(LeafNode) data;
		if(nowNode==node){
			return null;
		}
		while(nowNode.next!=node){
			nowNode=nowNode.next;
		}
		
		return nowNode;
	}
	/**
	 * 往叶子节点中插入k
	 * 返回该叶子节点
	 * @param inNode
	 * @param k
	 * @return
	 * @throws BPlusTreeException 
	 */
	private LeafNode insertLeafNode(Node inNode, T k) throws BPlusTreeException {
		if(inNode==null){//此时树为空
			T[] keys=(T[]) Array.newInstance(k.getClass(), 1);
			keys[0]=k;
			LeafNode newLeafNode=new LeafNode<T>(keys);
			return newLeafNode;
		}
		
		T[] keys=(T[]) inNode.keys;
		int index=getKeysIndex(k, inNode);
		T[] newKeys=(T[]) Array.newInstance(k.getClass(), keys.length+1);
		System.arraycopy(keys, 0, newKeys,0 , index);
		newKeys[index]=k;
		System.arraycopy(keys, index, newKeys,index+1 , keys.length-index);
		inNode.keys=newKeys;
		return (LeafNode) inNode;
	}
	/**
	 * 得到在指定叶子节点的keys中,k应该插入的位置
	 * @param k
	 * @param inNode
	 * @throws BPlusTreeException 
	 */
	private int getKeysIndex(T k, Node inNode) throws BPlusTreeException {
		T[] keys=(T[]) inNode.keys;
		int low=0;
		int high=keys.length-1;
		while(low<high){
			int mid=(high+low)/2;
			if(((Comparable)k).compareTo(keys[mid])==0){
				throw new BPlusTreeException(k.toString()+"已经存在");
			}else if(((Comparable)k).compareTo(keys[mid])>0){
				low=mid+1;
			}else{
				high=mid-1;
			}
		}
		//此时low==high,k的位置与low相临
		if(((Comparable)k).compareTo(keys[low])>0){
			return low+1;
		}else{
			return low;
		}
		
	}
	/**
	 * 插入算法④
	 * 插入的k如果为插入的节点中最小的，则需要从根节点向下一直修改该k的祖先节点的keys的相应k
	 * 返回被插入k的节点的父节点
	 * 也用于删除之后的调整，若删除的k为节点中最小的k，也使用该方法。
	 * @param k 
	 * @return
	 */
	private Node adjustForNewK(T k) {
		InternalNode nowNode=(InternalNode) root;
		while(nowNode.childNode[0].keys[0]!=k){
			nowNode.keys[0]=k;
			nowNode=(InternalNode) nowNode.childNode[0];
		}
		nowNode.keys[0]=k;
		return nowNode;
	}
	/**
	 * 用于插入
	 * 在一个节点中找到所插入k应该进入的子节点索引
	 * @param node
	 * @return
	 * @throws BPlusTreeException 
	 */
	private int getChildNodeIndexForInsert(T k,Node node) throws BPlusTreeException{
		int low=0;
		int high=((InternalNode)node).childNode.length-1;
		while(low<high){
			int mid=(high+low)/2;
			if(((Comparable)k).compareTo(node.keys[mid])==0){
				throw new BPlusTreeException(k.toString()+"已经存在");
			}else if(((Comparable)k).compareTo(node.keys[mid])>0){
				low=mid+1;
			}else{
				high=mid-1;
			}
		}
		//此时low==high,k的位置与low相临
		if(((Comparable)k).compareTo(node.keys[low])==0){
			throw new BPlusTreeException(k.toString()+"已经存在");
		}else if(((Comparable)k).compareTo(node.keys[low])>0){
			return low;
		}else{
			if(low-1<0){
				return 0;
			}else{
				return low-1;				
			}
		}
	}
	
	/**
	 * 用于查找
	 * 在一个节点中找到k所在的子节点索引或者k的数组索引
	 * @param k
	 * @param nowNode
	 * @return
	 * @throws BPlusTreeException
	 */
	private int getChildNodeIndexForSelect(T k, Node nowNode) throws BPlusTreeException {
		int low=0;
		int high=((InternalNode)nowNode).childNode.length-1;
		while(low<high){
			int mid=(high+low)/2;
			if(((Comparable)k).compareTo(nowNode.keys[mid])==0){
				return mid;
			}else if(((Comparable)k).compareTo(nowNode.keys[mid])>0){
				low=mid+1;
			}else{
				high=mid-1;
			}
		}
		//此时low==high,k的位置与low相临
		if(((Comparable)k).compareTo(nowNode.keys[low])>=0){
			return low;
		}else{
			if(low-1<0){
				throw new BPlusTreeException(k+"不存在");
			}else{
				return low-1;				
			}
		}
	}
	
}
