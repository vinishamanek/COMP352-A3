/**
 * @author Vinisha, Manek
 * COMP352
 * Assignment #3
 * December 2nd, 2023
**/

import java.util.Random;

public class CleverSIDC {
    private int size;
//    private DataStructure dataStructure;
    private Random random;
    private boolean over1000;
    private Node head;
    private Node root;
    
    private static class Node {
    	Node next, left, right; // next for linked list, left right for AVL tree
    	long key;
		int height; // key for unique id, height for AVL tree
    	String value; // value associated with key
    	
    	public Node(long key, String value) {
    		this.key = key;
    		this.value = value;
    		this.height = 1;
    		this.next = this.left = this.right = null;
    	}
    }

    public CleverSIDC() {
    	this.size = 0;
    	setSIDCThreshold(0);	
    }
    
    public CleverSIDC(int size) {
    	this.size = size;
    	setSIDCThreshold (this.size);	
    }


//    public CleverSIDC(int threshold) {
//        this.threshold = threshold;
//        this.random = new Random();
//        if (threshold <= 1000) {
//        	this.dataStructure = new LinkedList();
//        } else {
//        	this.dataStructure = new AVLTree();
//        }
//    }

	public void setSIDCThreshold(int size) {
        if (this.size <= 1000) {
        	this.over1000 = false;
        	this.root = null;
        } else {
        	this.over1000 = true;
        	this.head = null;
        }
//        adaptDataStructure();
    }

    public void generate() {
    	Random random = new Random();
    	int key;
    	boolean keyExists;
    	
    	do {
    		key = random.nextInt(99999999) + 1; // does this accept when its 00000001 or whatever? pls recheck
    		keyExists = existingKey(this, key);
    	} while(keyExists);
    		
    	add(this, key, "");
//    	return dataStructure.generate();
    }
    
    private boolean existingKey(CleverSIDC cleverSIDC, int key) {
    	if (cleverSIDC.over1000) {
    		return searchAVL(cleverSIDC.root, key) != null;
    	} else {
    		Node current = cleverSIDC.head;
    		while (current != null) {
    			if (current.key == key) {
    				return true;
    			}
    			current = current.next;
    		}
    		return false;
    	}
    }
    
    private Node searchAVL(Node root, long key) {
    	if (root == null || root.key == key) {
    		return root;
    	}
    	if (key < root.key) {
    		return searchAVL(root.left, key);
    	} else {
    		return searchAVL(root.right, key);
    	}
    }

    public long[] allKeys(CleverSIDC cleverSIDC) {
    	if (cleverSIDC.over1000) {
    		long[] keys = new long[sizeOfAVLTree(cleverSIDC.root)];
    		int index = 0;
    		inOrderTraversal(cleverSIDC.root, keys, index);
    		return keys;
    	}  else {
    		//implement linked list
    		long[] keys = new long[sizeOfLinkedList(cleverSIDC.root)];
    		int index = 0; // equal to a set value????
    		Node current = cleverSIDC.head;
    		while (current != null) {
    			keys[index++] = current.key;
    			current = current.next;
    		}
    		return keys;
    	}
    	
    }
    
    private int sizeOfLinkedList(Node head) {
    	int size = 0;
    	Node current = head;
    	while (current != null) {
    		size++;
    		current = current.next;
    	}
    	return size;
    }
    
    private int sizeOfAVLTree(Node node) {
    	if (node == null) {
    		return 0;
    	}
    	return 1 + sizeOfAVLTree(node.left) + sizeOfAVLTree(node.right);
    }
    
    
    private void inOrderTraversal(Node node, long[] keys, int index) {
        if (node != null) {
            inOrderTraversal(node.left, keys, index);
            keys[index++] = node.key;
            inOrderTraversal(node.right, keys, index);
        }
    }

    public  void add(CleverSIDC cleverSIDC, long key, String value) {
    	if (cleverSIDC.over1000) {
    		cleverSIDC.root = insert(cleverSIDC.root, key, value);
    	} else {
    		Node newNode = new Node(key, value);
    		
    		if (cleverSIDC.head == null || cleverSIDC.head.key > key) {
    			newNode.next = cleverSIDC.head;
    			cleverSIDC.head = newNode;
    		} else {
    			Node current = cleverSIDC.head;
    			while (current.next != null && current.next.key < key) {
    				current = current.next;
    			}
    			newNode.next = current.next;
    			current.next = newNode;
    		}
    	}
    	cleverSIDC.size++;
//        dataStructure.add(cleverSIDC, key, value);
//        adaptDataStructure();
    }
    
    private Node insert(Node node, long key, String value) {
    	if(node == null) {
    		return new Node(key, value);
    	}
    	
    	if (key < node.key) {
    		node.left = insert(node.left, key, value);
    	} else if (key > node.key) {
    		node.right = insert(node.right, key, value);
    	} else {
    		return node;
    	}
    	
    	node.height = 1 + Math.max(height(node.left), height(node.right));
    	
    	int balance = getBalance(node);
    	
    	// LL
        if(balance > 1 && key < node.left.key){
            return rightRotate(node);
        }

        // RR
        if (balance < -1 && key > node.right.key) {
            return leftRotate(node);
        }

        // LR
        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // RL
        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }
    
    private int height(Node node) {
    	if (node == null) {
    		return 0;
    	}
    	return node.height;
    }
    
    
    private int getBalance(Node node) {
    	if (node == null) {
    		return 0;
    	}
    	return height(node.left) - height(node.right);
    }
    
    private Node rightRotate(Node y) {
    	Node x = y.left;
    	Node z = x.right;
    	
    	x.right = y;
    	y.left = z;
    	
    	  y.height = Math.max(height(y.left), height(y.right)) + 1;
          x.height = Math.max(height(x.left), height(x.right)) + 1;

          return x;
    }
    
    private Node leftRotate(Node x) {
        Node y = x.right;
        Node z = y.left;

        y.left = x;
        x.right = z;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;


        return y;
    }

    public void remove(CleverSIDC cleverSIDC, long key) {
        if(cleverSIDC.over1000) {
        	cleverSIDC.root = deleteNode(cleverSIDC.root, key);
        } else {
        	if (cleverSIDC.head == null) {
        		return;
        	} 
        	if (cleverSIDC.head.key == key) {
        		cleverSIDC.head = cleverSIDC.head.next;
        		cleverSIDC.size--;
        	}
        	
        	Node current = cleverSIDC.head;
        	
        	while (current.next != null && current.next.key != key) {
        		current = current.next;
        	}
        	
        	if (current.next != null) {
        		current.next = current.next.next;
        		cleverSIDC.size--;
        	}
        }
    }

    private Node deleteNode(Node root, long key) {
    	if (root == null) {
    		return root;
    	}
    	
    	if (key < root.key) {
    		root.left = deleteNode(root.left, key);
    	} else if (key > root.key) {
            root.right = deleteNode(root.right, key);
        } else { 
        	if ((root.left == null) || (root.right == null)) {
        		Node temp = (root.left != null) ? root.left : root.right;
        		
        		if (temp == null) {
                    temp = root;
                    root = null;
                } else {

                    // One child case
                    root = temp;
                }
	          } else {
	
	                // Node with two children: Get the inorder successor
	                Node temp = minValueNode(root.right);
	
	                // Copy the inorder successor's data to this node
	                root.key = temp.key;
	                root.value = temp.value;
	
	                // Delete the inorder successor
	                root.right = deleteNode(root.right, temp.key);
	            }
	        }
	
	        // If the tree had only one node then return
	        if (root == null) return root;
	
	        // Update height of the current node
	        root.height = Math.max(height(root.left), height(root.right)) + 1;
	
	        // Balance the tree
	        int balance = getBalance(root);
	
	        // LL
	        if (balance > 1 && getBalance(root.left) >= 0) {
	            return rightRotate(root);
	        }
	
	        // LR
	        if (balance > 1 && getBalance(root.left) < 0) {
	            root.left = leftRotate(root.left);
	            return rightRotate(root);
	        }
	
	        // RR
	        if (balance < -1 && getBalance(root.right) <= 0) {
	            return leftRotate(root);
	        }
	
	        // RL
	        if (balance < -1 && getBalance(root.right) > 0) {
	            root.right = rightRotate(root.right);
	            return leftRotate(root);
	        }
	
	        return root;
	}
	
	private Node minValueNode(Node node) {
		Node current = node;
		while (current.left != null) {
			current = current.left;
		}
		return current;
	}
	
	    
    public String getValues(CleverSIDC cleverSIDC, long key) {
    	if (cleverSIDC.over1000) {
    		Node node = searchAVL(cleverSIDC.root, key);
    		return (node != null) ? node.value : null;
    	} else {
    		Node current = cleverSIDC.head;
    		while (current != null) {
    			
    			if (current.key == key) {
    				return current.value;
    			}
    			current = current.next;
    		}
    		return null;
    	}

    }
    

    public long nextKey(CleverSIDC cleverSIDC, long key) {
    	if (cleverSIDC.over1000) {
    		Node successorNode = findSuccessor(cleverSIDC.root, key);
            return successorNode != null ? successorNode.key : null;
    	} else {
    		Node current = cleverSIDC.head;
    		while (current != null && current.key <= key) {
    			current = current.next;
    		}
    		return current != null ? current.key : null;
    	}
    
    }
    
    private Node findSuccessor(Node root, long key) {
    	   Node successor = null;
           while (root != null) {
               if (key < root.key) {
                   successor = root;
                   root = root.left;
               } else {
                   root = root.right;
               }
           }
           return successor;
    }
   

    public long prevKey(CleverSIDC cleverSIDC, long key) {
    	if (cleverSIDC.over1000) {
    		Node predecessorNode = findPredecessor(cleverSIDC.root, key);
    		return predecessorNode != null ? predecessorNode.key : null;
    	} else {
    		if (cleverSIDC.head == null || cleverSIDC.head.key >= key) {
    			return 0;
    		}
    		
    		 Node current = cleverSIDC.head;
             Node prev = null;
             while(current != null && current.key < key){
                 prev = current;
                 current = current.next;
             }
             return prev != null ? prev.key: 0;
         }
    }

    private Node findPredecessor(Node root, long key) {
 	   Node predecessor = null;
        while (root != null) {
            if (key > root.key) {
            	predecessor = root;
                root = root.right;
            } else {
                root = root.left;
            }
        }
        return predecessor;
 }
    
    public long rangeKey(long key1, long key2) {
    	if (this.over1000) {
    		return countKeysInRange(this.root, key1, key2);
    	} else {
    		int count = 0;
    		Node current = this.head;
    		Boolean keyThere = false;
    		
    		while(current != null && current.key < key1){
                current = current.next;
            }
            while(current != null && current.key <= key2){
                if(current.key  == key2){
                	keyThere = true;
                }
                count++;
                current = current.next;
            }
            if(keyThere){
                return count;
            }
            else{
                return 0;
            }
    		
    	}
    }

    
    private int countKeysInRange(Node node, long key1, long key2) {
    	if (node == null) {
    		return 0;
    	}
    	if (node.key < key1) {
    		return countKeysInRange(node.right, key1, key2);
    	} else if (node.key > key2) {
    		return countKeysInRange(node.left, key1, key2);
    	} else { 
    		return 1 + countKeysInRange(node.left, key1, key2) + countKeysInRange(node.right, key1, key2);
    	}
    }
}
