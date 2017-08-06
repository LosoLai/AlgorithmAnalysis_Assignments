
import java.io.PrintStream;
import java.util.*;


public class LinkedListMultiset<T> extends Multiset<T>
{
	/** Reference to head of list. */
    protected Node<T> mHead;
    /** Reference to tail of list. */
    protected Node<T> mTail;
    /** Length of list. */
    protected int mLength;
    
	public LinkedListMultiset() {
		// Implement by LosoLai
		mHead = null; 
        mTail = null;
        mLength = 0;
	} // end of LinkedListMultiset()
	
	/**
     * Add a new value to the start of the list.
     * 
     * @param newValue Value to add to list.
     */
	public void add(T item) {
		// Implement by LosoLai
        Node<T> newNode = new Node<T>(item);
        
        // If head is empty, then list is empty and head and tail references need to be initialised.
        if (mHead == null) {
            mHead = newNode;
            mTail = newNode;
        }
        // otherwise, add node to the head of list.
        else {
            newNode.setNext(mHead);
            mHead.setPrev(newNode);
            mHead = newNode;
        }
        
        mLength++;
	} // end of add()
	
	/**
     * Returns the value stored in node at position 'index' of list.
     * 
     * @param item Value to search for.
     * @return index if value is in list, otherwise -1.
     */
	public int search(T item) {
		// Implement by LosoLai
		Node<T> currNode = mHead;
        for (int i = 0; i < mLength; ++i) {
        	if (currNode.getValue() == item) {
        		// return current index
        		return i;
        	}
            currNode = currNode.getNext();
        }
		
		// return -1 present item cannot find
		return -1;
	} // end of add()
	
	/**
     * Delete given value from list (delete first instance found).
     *   
     * @param item Value to remove.
     */
	public void removeOne(T item) {
		// Implement by LosoLai
		Node<T> currNode = mHead;

        // check if value is head node
        if (currNode.getValue() == item) {
            // check if length of 1
            if (mLength == 1) {
                mHead = mTail= null;
            }
            else {
                mHead = currNode.getNext();
                mHead.setPrev(null);
                currNode = null;
            }
            
            mLength--;
        }
        // search for value in rest of list
        else {
            currNode = currNode.getNext();

            while (currNode != null) {
                if (currNode.getValue() == item) {
                    Node<T> prevNode = currNode.getPrev();
                    prevNode.setNext(currNode.getNext());
                    // check if tail
                    if (currNode.getNext() != null) {
                    	currNode.getNext().setPrev(prevNode);
                    }
                    else {
                    	mTail = prevNode;
                    }
                    currNode = null;
                    mLength--;
                }
 
                currNode = currNode.getNext();
            }	
        }
	} // end of removeOne()
	
	
	public void removeAll(T item) {
		// Implement by LosoLai
		Node<T> currNode = mHead;

        // check if value is head node
        if (currNode.getValue() == item) {
            // check if length of 1
            if (mLength == 1) {
                mHead = mTail= null;
            }
            else {
                mHead = currNode.getNext();
                mHead.setPrev(null);
                currNode = null;
            }
            
            mLength--;
        }
        // search for value in rest of list
        else {
            currNode = currNode.getNext();

            while (currNode != null) {
                if (currNode.getValue() == item) {
                    Node<T> prevNode = currNode.getPrev();
                    prevNode.setNext(currNode.getNext());
                    // check if tail
                    if (currNode.getNext() != null) {
                    	currNode.getNext().setPrev(prevNode);
                    }
                    else {
                    	mTail = prevNode;
                    }
                    //comment this line
                    //in order to keep searching till tail
                    //currNode = null;
                    mLength--;
                }
 
                currNode = currNode.getNext();
            }
        }
	} // end of removeAll()
	
	/**
     * Print the list in head to tail.
     */
	public void print(PrintStream out) {
		// Implement by LosoLai
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
	
} // end of class LinkedListMultiset