package Red_black_tree;

public class RbTree<T extends Comparable> {
	private Node<T> root;
	
//	private static Node nullNode=new Node();//不使用一个代替null的节点，因为会多产生一倍不必要的节点
	
	public Node<T> getRoot() {
		return root;
	}

	
	private void leftRotate(Node node){
		if(node.parent!=null){
			if(node.parent.leftChild==node){
				node.parent.leftChild=node.rightChild;
				node.rightChild.parent=node.parent;
			}else{
				node.parent.rightChild=node.rightChild;
				node.rightChild.parent=node.parent;
			}			
		}else{
			node.rightChild.parent=null;
		}
		
		Node r=node.rightChild;
		
		if(r.leftChild!=null){
			node.rightChild=r.leftChild;
			r.leftChild.parent=node;			
		}
		
		r.leftChild=node;
		node.parent=r;
	}

	
	private void rightRotate(Node node){
		if(node.parent!=null){
			if(node.parent.leftChild==node){
				node.parent.leftChild=node.leftChild;
				node.leftChild.parent=node.parent;
			}else{
				node.parent.rightChild=node.leftChild;
				node.leftChild.parent=node.parent;
			}
		}else{
			node.leftChild.parent=null;
		}
		Node l=node.leftChild;
		
		if(l.rightChild!=null){
			node.leftChild=l.rightChild;
			l.rightChild.parent=node;			
		}
		
		l.rightChild=node;
		node.parent=l;
	}
	
	private Node getMin(Node node){
		while(node.leftChild!=null){
			node=node.leftChild;
		}
		return node;
	}
	/**
	 * 得到node树的最大节点
	 * @param node
	 * @return
	 */
	private Node getMax(Node node){
		while(node.rightChild!=null){
			node=node.rightChild;
		}
		return node;
	}
	/**
	 * 插入节点
	 * @param newNode
	 * @throws RbTreeException 
	 */
	private void insert(Node newNode) throws RbTreeException{
		if(root==null){
			root=newNode;
			root.parent=null;
			root.color=true;
			return;
		}
		Node current=root;
		while(true){
			if(newNode.data.compareTo(current.data)>0){
				if(current.rightChild==null){
					current.rightChild=newNode;
					newNode.parent=current;
					return;
				}
				current=current.rightChild;
			}else if(newNode.data.compareTo(current.data)<0){
				if(current.leftChild==null){
					current.leftChild=newNode;
					newNode.parent=current;
					return;
				}
				current=current.leftChild;
			}else{
				throw new RbTreeException(newNode.data+"已经存在");
			}	
		}
	}
	/**
	 * 调整平衡
	 * @param node
	 */
	private void insertBalance(Node node){
		if(node==root){
			return;
		}
		while(node.parent!=null&&node.parent.color==false){
				if(node.parent==node.parent.parent.leftChild){
					Node u=node.parent.parent.rightChild;
					if(u!=null&&u.color==false){
						u.color=true;
						node.parent.color=false;
						node.parent.parent.color=true;
						node=node.parent.parent;
						if(node.parent==null){
							root=node;
							root.color=true;
							return;
						}
					}else{
						if(node==node.parent.rightChild){
							node=node.parent;
							leftRotate(node);
						}
						node.parent.color=true;
						node.parent.parent.color=false;
						rightRotate(node.parent.parent);
						if(node.parent.parent==null){
							root=node.parent;
							return;
						}
					}
				
				}else{
					Node u=node.parent.parent.leftChild;
					if(u!=null&&u.color==false){
						node.parent.color=true;
						u.color=true;
						node.parent.parent.color=false;
						node=node.parent.parent;
						if(node.parent==null){
							root=node;
							root.color=true;
							return;
						}
					}else{
						if(node==node.parent.leftChild){
							node=node.parent;
							rightRotate(node);
						}
						node.parent.color=true;
						node.parent.parent.color=false;
						leftRotate(node.parent.parent);
						if(node.parent.parent==null){
							root=node.parent;
							return;
						}
					}
				}	
				
			}
	}
	
	
	/**
	 * 插入data,客户端
	 * @param data
	 */
	public void insert(T data){
		Node node=new Node();
		node.data=data;
		node.color=false;
		node.leftChild=null;
		node.rightChild=null;
		try {
			insert(node);
			insertBalance(node);
		} catch (RbTreeException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 删除值为data的节点
	 * 如果没有值为data的节点则返回null
	 * @param data
	 */
	public Node delete(T data){
		Node node=serch(data);
		if(node==null){
			return null;
		}else{
			return delete(node);
		}
	}
	
	/**
	 * 删除节点node
	 * @param node
	 * @return
	 */
	private Node delete(Node node) {
		if(node==root){
			if(node.leftChild!=null&&node.rightChild==null){
				node.leftChild.parent=null;
				root=node.leftChild;
			}else if(node.leftChild==null&&node.rightChild!=null){
				node.rightChild.parent=null;
				root=node.rightChild;
			}else if(node.leftChild==null&&node.rightChild==null){
				root=null;
			}else{
				Node successor=getSuccessor(node);
				node.data=successor.data;
				delete(successor);
			}
			if(root!=null){
				root.color=true;	
			}
			return root;
		}
		else if(node==node.parent.leftChild){//node为左孩子
			Node newNode=null;
			if(node.leftChild!=null&&node.rightChild==null){
				node.leftChild.parent=node.parent;
				node.parent.leftChild=node.leftChild;
				newNode=node.leftChild;
			}else if(node.leftChild==null&&node.rightChild!=null){
				node.rightChild.parent=node.parent;
				node.parent.leftChild=node.rightChild;
				newNode=node.rightChild;
			}else if(node.leftChild==null&&node.rightChild==null){
				node.parent.leftChild=null;
				newNode=null;
			}else{
				Node successor=getSuccessor(node);
				node.data=successor.data;
				delete(successor);
			}
			if(node.color==true){
				deleteBalance(newNode);
			}
			return node;
		}else{//node为右孩子
			Node newNode=null;
			if(node.leftChild!=null&&node.rightChild==null){
				node.leftChild.parent=node.parent;
				node.parent.rightChild=node.leftChild;
				newNode=node.leftChild;
			}else if(node.leftChild==null&&node.rightChild!=null){
				node.rightChild.parent=node.parent;
				node.parent.rightChild=node.rightChild;
				newNode=node.rightChild;
			}else if(node.leftChild==null&&node.rightChild==null){
				node.parent.rightChild=null;
				newNode=null;
			}else{
				Node successor=getSuccessor(node);
				node.data=successor.data;
				delete(successor);
			}
			if(node.color==true){
				deleteBalance(newNode);
			}
			return node;
		}
		
	}
	/**
	 * (双黑节点)平衡
	 * @param node
	 */
    private void deleteBalance(Node node) {
		if(node==null){
			return;
		}
		if(node==node.parent.leftChild){//node为左孩子
			Node bNode=node.parent.rightChild;
			//情况一
			if(bNode!=null&&bNode.color==true&&bNode.rightChild!=null&&bNode.rightChild.color==true&&bNode.leftChild!=null&&bNode.leftChild.color==true){
				node=node.parent;
				if(node.color==false){
					node.color=true;
					bNode.color=false;
					return;
				}else{
					node.color=true;
					bNode.color=false;
					deleteBalance(node);
					//重新进入算法
				}
			//情况二
			}else if(bNode!=null&&bNode.color==false){
				leftRotate(node.parent);
				bNode=node.parent.rightChild;
				//进入情况三或四
			//情况三
			}else if(bNode!=null&&bNode.color==true&&bNode.leftChild!=null&&bNode.leftChild.color==false&&bNode.rightChild!=null&&bNode.rightChild.color==true){
				rightRotate(bNode);
				bNode=bNode.leftChild;
				//进入情况四
			}else if(bNode!=null&&bNode.color==true&&bNode.leftChild!=null&&bNode.leftChild.color==true&&bNode.rightChild!=null&&bNode.rightChild.color==false){
				bNode.color=node.parent.color;
				node.parent.color=true;
				bNode.rightChild.color=true;
				leftRotate(node.parent);
			}
		}else{//node为右孩子
			Node bNode=node.parent.leftChild;
			//情况一
			if(bNode!=null&&bNode.color==true&&bNode.rightChild!=null&&bNode.rightChild.color==true&&bNode.leftChild!=null&&bNode.leftChild.color==true){
				node=node.parent;
				if(node.color==false){
					node.color=true;
					bNode.color=false;
					return;
				}else{
					node.color=true;
					bNode.color=false;
					deleteBalance(node);
					//重新进入算法
				}
			//情况二
			}else if(bNode!=null&&bNode.color==false){
				rightRotate(node.parent);
				bNode=node.parent.leftChild;
				//进入情况三或四
			//情况三
			}else if(bNode!=null&&bNode.color==true&&bNode.leftChild!=null&&bNode.leftChild.color==true&&bNode.rightChild!=null&&bNode.rightChild.color==false){
				leftRotate(bNode);
				bNode=bNode.rightChild;
				//进入情况四
			//情况四
			}else if(bNode!=null&&bNode.color==true&&bNode.leftChild!=null&&bNode.leftChild.color==false&&bNode.rightChild!=null&&bNode.rightChild.color==true){
				bNode.color=node.parent.color;
				node.parent.color=true;
				bNode.leftChild.color=true;
				rightRotate(node.parent);
			}
		}
	}

	/**
     * 得到node的后继节点
     * @param node
     * @return
     */
	private Node getSuccessor(Node node){
		return getMin(node.rightChild);
	}
	/**
	 * 查找值为data的节点
	 * 若无此data返回null
	 * @param data
	 * @return
	 */
	private Node serch(T data) {
		if(root==null){
			return null;
		}
		Node current=root;
		while(current.data.compareTo(data)!=0){//不能用data！=data
			if(data.compareTo(current.data)>0){
				current=current.rightChild;
			}else{
				current=current.leftChild;
			}
			if(current==null){
				return null;
			}
		}
		return current;
	}
	
}
