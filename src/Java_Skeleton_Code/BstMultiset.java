package Java_Skeleton_Code;

import java.io.PrintStream;

public class BstMultiset<T extends Comparable> extends Multiset<T> {
	/** Reference to head of list. */
	protected Node<T> root;

	public BstMultiset() {
		root = null;
	} // end of BstMultiset()

	public void add(T item) {
		Node<T> newNode = new Node<T>(item);
		// Implement me!
		if (root == null) {
			root = newNode;
		} else {
			Node<T> currentNode = root;
			boolean done = false;

			while (!done) {
				if (item.equals(currentNode.getValue())) {
					currentNode.addLeaf();
					done = true;
				} else if (item.compareTo(currentNode.getValue()) < 0) {
					if (currentNode.getLeftChild() == null) {
						currentNode.setLeftChild(newNode);
						done = true;
					} else {
						currentNode = currentNode.getLeftChild();
						done = false;
					}
				} else if (item.compareTo(currentNode.getValue()) > 0) {
					if (currentNode.getRightChild() == null) {
						currentNode.setRightChild(newNode);
						done = true;
					} else {
						currentNode = currentNode.getRightChild();
						done = false;
					}
				}
			}
		}
	} // end of add()

	public int search(T item) {
		Node<T> currentNode = root;
		boolean done = false;
		int found = 0;

		while (!done) {
			if (item.compareTo(currentNode.getValue()) == 0) {
				found = currentNode.getCount();
				done = true;
			} else if (item.compareTo(currentNode.getValue()) < 0) {
				if (currentNode.getLeftChild() == null) {
					done = true;
					found = 0;
				} else {
					currentNode = currentNode.getLeftChild();
				}
			} else if (item.compareTo(currentNode.getValue()) > 0) {
				if (currentNode.getRightChild() == null) {
					done = true;
				} else {
					currentNode = currentNode.getRightChild();
				}
			}
		}

		return found;
	} // end of search()

	public void removeOne(T item) {
		Node<T> currentNode = root;
		boolean done = false;

		while (!done) {
			if (item.compareTo(currentNode.getValue()) == 0) {
				int count = currentNode.removeLeaf();
				if (count == 0) {
					removeAll(item); // not a very efficient solution but avoids
										// writing the code twice
				}
				done = true;
			} else if (item.compareTo(currentNode.getValue()) < 0) {
				if (currentNode.getLeftChild() == null) {
					done = true;
				} else {
					currentNode = currentNode.getLeftChild();
				}
			} else if (item.compareTo(currentNode.getValue()) > 0) {
				if (currentNode.getRightChild() == null) {
					done = true;
				} else {
					currentNode = currentNode.getRightChild();
				}
			}
		}
	} // end of removeOne()

	public void removeAll(T item) {
//		Node<T> currentNode = root;
//		Node<T> parentNode = null;
//		boolean done = false;
//
//		while (!done) {
//			if (item.compareTo(currentNode.getValue()) == 0) {
//				if (currentNode.getLeftChild() == null & currentNode.getRightChild() == null) {
//					// ok to delete
//				}
//
//				done = true;
//			} else if (item.compareTo(currentNode.getValue()) < 0) {
//				if (currentNode.getLeftChild() == null) { // not found
//					done = true;
//				} else {
//					currentNode = currentNode.getLeftChild();
//				}
//			} else if (item.compareTo(currentNode.getValue()) > 0) {
//				if (currentNode.getRightChild() == null) {// not found
//					done = true;
//				} else {
//					currentNode = currentNode.getRightChild();
//				}
//			}
//		}
		
		root = myDelete(root, item);
		
		
	} // end of removeAll()
	
	private Node<T> myDelete(Node<T> point, T item){
		// Case: Job already done
		if (point == null) {
			return null;
		}
		
		// Case: Node with the required value is to the left or right of point
		// Move to next node
		if (item.compareTo(point.getValue()) < 0) {
			point.setLeftChild(myDelete(point.getLeftChild(), item));
		} else if (item.compareTo(point.getValue()) > 0) {
			point.setRightChild(myDelete(point.getRightChild(), item));
		} 
		
		else { // Case: This is the node with the required value
			// No children
			if(point.getLeftChild() == null && point.getRightChild() == null) {
				return null; // delete reference to point
			} else if (point.getLeftChild() == null) { 
				// One child to the right
				// join point's parent to point's right child
				return point.getRightChild(); 
			} else if (point.getRightChild() == null) { 
				// One child to the left
				// join point's parent to point's right child
				return point.getLeftChild(); 
			} else { 
				// Both children exist 
				// Find node with min value
				Node<T> min = minNode(point.getRightChild());
				point.setValue(min.getValue());
				point.setCount(min.getCount());
				
				// Delete the node 
				point.setRightChild(myDelete(point.getRightChild(), min.getValue()));
			}
		}
		return point;
	}

	private Node<T> minNode(Node<T> point) {
		T minValue = point.getValue();
		
		while (point.getLeftChild() != null) {
			minValue = point.getLeftChild().getValue();
			point = point.getLeftChild();
		}
			
		return point;
	}



	private T minValue(Node<T> point) {
		T minValue = point.getValue();
		
		while (point.getLeftChild() != null) {
			minValue = point.getLeftChild().getValue();
			point = point.getLeftChild();
		}
			
		return minValue;
	}

	public void print(PrintStream out) {
		travelPrint(root, out);
	} // end of print()

	private void travelPrint(Node top, PrintStream out) {
		if (top != null) {
			travelPrint(top.getLeftChild(), out);
			out.println(top.getValue() + printDelim + top.getCount());
			travelPrint(top.getRightChild(), out);
		}
	}

	/**
	 * Node type, inner private class.
	 */
	private class Node<T extends Comparable> {
		/** Stored value of node. */
		private T mValue;
		private int count = 0;
		/** Reference to next node. */
		private Node<T> leftChild;
		private Node<T> rightChild;

		public Node(T value) {
			mValue = value;
			leftChild = null;
			rightChild = null;
			count = 1;
		}

		public int addLeaf() {
			count++;
			return count;
		}

		public int removeLeaf() {
			count--;
			return count;
		}

		public T getValue() {
			return mValue;
		}

		public void setValue(T value) {
			mValue = value;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public int getCount() {
			return count;
		}

		public Node<T> getLeftChild() {
			return leftChild;
		}

		public void setLeftChild(Node<T> left) {
			leftChild = left;
		}

		public Node<T> getRightChild() {
			return rightChild;
		}

		public void setRightChild(Node<T> right) {
			rightChild = right;
		}
	} // end of inner class Node

} // end of class BstMultiset
