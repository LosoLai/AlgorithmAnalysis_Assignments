/**Implement Sorted Linked List
 * 
 * The operations included
 * - add
 * - search
 * - removeOne
 * - removeAll
 * - print
 * 
 * Author : LosoLai(s3579161)
 */
import java.io.PrintStream;
import java.util.*;

public class SortedLinkedListMultiset<T> extends Multiset<T>
{
	/** Reference to head of list. */
    protected Node<T> mHead;
    /** Reference to tail of list. */
    protected Node<T> mTail;
    /** Length of list. */
    protected int mLength;
    
	public SortedLinkedListMultiset() {
		mHead = null; 
        mTail = null;
        mLength = 0;
	} // end of SortedLinkedListMultiset()
	
	/**
     * Add a new value to the start of the list.
     * (AddLast) 
     * @param newValue Value to add to list.
     */
	public void add(T item) {
		Node<T> newNode = new Node<T>(item);
		
		// If head is empty, then list is empty and 
		// head and tail references need to be initialized.
        if (mHead == null) {
            mHead = newNode;
            mTail = newNode;
        }
        // otherwise, iterator each node 
        // find the specific index to add the node  
        else {
            
        }
	} // end of add()
	
	/**
     * Returns the times that the item value is founded in list.
     * 
     * @param item Value to search for.
     * @return the found variable which present how many times the value is found.
     */
	public int search(T item) {
		int found = 0;
		Node<T> currNode = mHead;
        for (int i = 0; i < mLength; ++i) {
        	if (currNode.getValue() == item) {
        		found++;
        	}
            currNode = currNode.getNext();
        }
		
        // return the found variable
		return found;
	} // end of add()
	
	
	public void removeOne(T item) {
		// Implement me!
	} // end of removeOne()
	
	
	public void removeAll(T item) {
		// Implement me!
	} // end of removeAll()
	
	
	public void print(PrintStream out) {
		out.println(toString());
	} // end of print()
	
	/**
     * @return String representation of the list.
     */
    public String toString() {
        Node<T> currNode = mHead;

        StringBuffer str = new StringBuffer();

        while (currNode != null) {
            str.append(currNode.getValue() + " ");
            currNode = currNode.getNext();
        }

        return str.toString();
    } // end of toString();
    
	/**
     * Node type, inner private class.
     */
    private class Node<T>
    {
        /** Stored value of node. */
        private T mValue;
        /** Reference to next node. */
        private Node<T> mNext;
        /** Reference to previous node. */
        private Node<T> mPrev;

        public Node(T value) {
            mValue = value;
            mNext = null;
            mPrev = null;
        }

        public T getValue() {
            return mValue;
        }


        public Node<T> getNext() {
            return mNext;
        }
        
        
        public Node<T> getPrev() {
            return mPrev;
        }


        public void setValue(T value) {
            mValue = value;
        }


        public void setNext(Node<T> next) {
            mNext = next;
        }
        
        public void setPrev(Node<T> prev) {
            mPrev = prev;
        }
    } // end of inner class Node
	
} // end of class SortedLinkedListMultiset