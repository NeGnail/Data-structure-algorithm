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
		this.minKeysNumber=((Double)Math.ceil(1.0 * factor/2)).intValue();//����С�ڴ���
		this.maxKeysNumber=factor;//���ܴ��ڴ���
	}
	
	
	
	public Node<T> getRoot() {
		return root;
	}
	
	/**
	 * �ṩ���û��ķ���������k
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
	 * ����k����ĳ���ڵ�
	 * ���ҷ�ʽΪ�Ӹ��ڵ����²���
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
	 * ����k����ĳ���ڵ�
	 * ���ҷ�ʽΪ��data����������β���
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
	 * ɾ��kֵ�������������ṹ�����ذ�����k��Node
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
	 * nowNode�Ѿ�ɾ����k���÷�����nowNode���е������ṹ�Ա�֤b+��������
	 * @param nowNode
	 * @param k
	 */
	private void adjustForDelete(Node nowNode, T k) {
		//����ɾ��k��Ľڵ����������������ж�
		//��ɾ����kΪ�ýڵ�����С��k���轫�ýڵ���������Ƚڵ��key����Ӧ����������ֱ�ӷ���
		if(nowNode.keys.length>=minKeysNumber){
			if( ((Comparable)k).compareTo(nowNode.keys[0])>0 ){
				return;
			}else{
				adjustForNewK(k);
				return;
			}
		//����ɾ��k��Ľڵ㲻���������������ж�
		//�����Դ��ֵܽڵ��ȡһ��k���ȡ
		//�������Դ��ֵܽڵ��ȡһ��k����кϲ�
		}else{
//			if(getKFromBrother(nowNode)){
//				
//			}
		}
	}
	
	/**
	 * ���ֵܽڵ��ȡһ��k�������ȡ֮�󲻻��ƻ������������ȡ������true
	 * 				    �����ȡ֮����ƻ����������򲻻�ȡ������false
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
	 * ��k���ڵĽڵ���ɾ��kֵ���˲��������������ṹ��
	 * ����k���ڵĽڵ�
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
	 * �ж�nowNode��keys���Ƿ���k
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
	 * �Ӹ�Ҷ�ӽڵ㿪ʼ���ϲ��Ϸ��ѣ���Ϊ��һ�η��ѵ���Ҷ�ӽڵ㣬���Է���������LeafNode����������ÿһ�η��ѵĶ���Ҷ�ӽڵ㡣
	 * @param k
	 * @param node
	 * @throws BPlusTreeException
	 */
	private void adjustFromLeafNode(T k, Node node) throws BPlusTreeException {
		Node nowNode=node;
		while(true){
			//�㷨��
			if(nowNode.parent==null&&nowNode.keys.length<=maxKeysNumber){
				return;
				//�㷨��
			}else if(nowNode.parent!=null&&nowNode.keys.length<=maxKeysNumber){
				if(k!=nowNode.keys[0]){
					return;
				}else{
					adjustForNewK(k);
					return;
				}
				//�㷨��
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
	 * ��ָ���ڵ��в����µ�key����Ӧ���ӽڵ�����
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
	 * ����֪�������ڵ㹹��һ�����ڵ�(���ڵ�)
	 * �ø��ڵ��������ӽڵ�Ĺ�ϵ�ڴ˷����лṹ���
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
	 * ��ָ���ڵ����Ϊ�����ڵ㣬�����ڵ��parent��next�����ڴ˷����лᴦ��á��������ڵ�ͬ���ӽڵ��һ�й�ϵ������ã��븸�ڵ�Ĺ�ϵֻ�����������ڵ��parent���ã���û�жԸ��ڵ���ӽڵ����ü����ڵ��k���д���
	 * @param nowNode
	 * @return
	 */
	private Node[] splitNode(Node nowNode) {
		int index=nowNode.keys.length/2-1;//indexΪ�ָ���������indexΪǰ�벿��
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
	 * ����ָ���ڵ�ǰ���ڵ�
	 * ���ָ���ڵ���data����ô���ؿա�
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
	 * ��Ҷ�ӽڵ��в���k
	 * ���ظ�Ҷ�ӽڵ�
	 * @param inNode
	 * @param k
	 * @return
	 * @throws BPlusTreeException 
	 */
	private LeafNode insertLeafNode(Node inNode, T k) throws BPlusTreeException {
		if(inNode==null){//��ʱ��Ϊ��
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
	 * �õ���ָ��Ҷ�ӽڵ��keys��,kӦ�ò����λ��
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
				throw new BPlusTreeException(k.toString()+"�Ѿ�����");
			}else if(((Comparable)k).compareTo(keys[mid])>0){
				low=mid+1;
			}else{
				high=mid-1;
			}
		}
		//��ʱlow==high,k��λ����low����
		if(((Comparable)k).compareTo(keys[low])>0){
			return low+1;
		}else{
			return low;
		}
		
	}
	/**
	 * �����㷨��
	 * �����k���Ϊ����Ľڵ�����С�ģ�����Ҫ�Ӹ��ڵ�����һֱ�޸ĸ�k�����Ƚڵ��keys����Ӧk
	 * ���ر�����k�Ľڵ�ĸ��ڵ�
	 * Ҳ����ɾ��֮��ĵ�������ɾ����kΪ�ڵ�����С��k��Ҳʹ�ø÷�����
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
	 * ���ڲ���
	 * ��һ���ڵ����ҵ�������kӦ�ý�����ӽڵ�����
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
				throw new BPlusTreeException(k.toString()+"�Ѿ�����");
			}else if(((Comparable)k).compareTo(node.keys[mid])>0){
				low=mid+1;
			}else{
				high=mid-1;
			}
		}
		//��ʱlow==high,k��λ����low����
		if(((Comparable)k).compareTo(node.keys[low])==0){
			throw new BPlusTreeException(k.toString()+"�Ѿ�����");
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
	 * ���ڲ���
	 * ��һ���ڵ����ҵ�k���ڵ��ӽڵ���������k����������
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
		//��ʱlow==high,k��λ����low����
		if(((Comparable)k).compareTo(nowNode.keys[low])>=0){
			return low;
		}else{
			if(low-1<0){
				throw new BPlusTreeException(k+"������");
			}else{
				return low-1;				
			}
		}
	}
	
}
