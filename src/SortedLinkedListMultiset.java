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


public class SortedLinkedListMultiset<T extends Comparable<T>> extends Multiset<T>
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
        	Node<T> currNode = mHead;
            while (currNode != null) {
            	// item value is smaller than value
            	if(currNode.getValue().compareTo(item) > 0) {
            		if(currNode.getPrev() == null)
            		{
            			// addBefore
            			newNode.setNext(mHead);
                        mHead.setPrev(newNode);
                        mHead = newNode;
                        break;
            		}
            		else
            		{
            			newNode.setPrev(currNode.getPrev());
            			newNode.setNext(currNode);
            			currNode.getPrev().setNext(newNode);
            			currNode.setPrev(newNode);
            			break;
            		}
            	}
            	// item value is grater than value
            	else if(currNode.getValue().compareTo(item) < 0) {
            		if(currNode.getNext() == null)
            		{
            			// addLast
            			currNode.setNext(newNode);
            			newNode.setPrev(currNode);
            			mTail = newNode;
            			break;
            		}
            	}
            	else {
            		currNode.increaseFound();
            		break;
            	}
            	
            	currNode = currNode.getNext();
            }  
        }
        
        mLength++;
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
		while (currNode != null) {
        	if (currNode.getValue().compareTo(item) == 0) {
        		found = currNode.getFound();
        		return found;
        	}
            currNode = currNode.getNext();
        }
		
		// return the found variable
		return found;
	} // end of add()
	
	
	public void removeOne(T item) {
		//check the head is not empty
		if(mHead != null)
		{
			Node<T> currNode = mHead;
			
			// check if value is head node
	        if (currNode.getValue().compareTo(item) == 0) {
	            // check if length of 1
	            if (mLength == 1) {
	                mHead = mTail= null;
	            }
	            else {
	            	if(currNode.getFound() == 1)
	            	{
	            		mHead = currNode.getNext();
	            		mHead.setPrev(null);
	            		currNode = null;
	            	}
	            	else
	            		currNode.decreaseFound();
	            }
	            
	            mLength--;
	        }
	        // search for value in rest of list
	        else {
	            currNode = currNode.getNext();

	            while (currNode != null) {
	                if (currNode.getValue().compareTo(item) == 0) {
	                	// check the number
	                	if(currNode.getFound() <= 0)
	                		break;
	                	
	                	if(currNode.getFound() == 1)
	                	{
	                		Node prevNode = currNode.getPrev();
	                        prevNode.setNext(currNode.getNext());
	                        // check if tail
	                        if (currNode.getNext() != null) {
	                        	currNode.getNext().setPrev(prevNode);
	                        }
	                        else {
	                        	mTail = prevNode;
	                        }
	                        currNode = null;
	                	}
	                	else
	                		currNode.decreaseFound();
	                    mLength--;
	                    break;
	                }
	                else
	                	currNode = currNode.getNext();
	            }	
	        }
		}
	} // end of removeOne()
	
	
	public void removeAll(T item) {
		Node<T> currNode = mHead;

		// check if value is head node
        if (currNode.getValue().compareTo(item) == 0) {
            // check if length of 1
            if (mLength == 1) {
                mHead = mTail= null;
                mLength--;
            }
            else {
            	int number = currNode.getFound();
            	mLength -= number;
            	mHead = currNode.getNext();
            	mHead.setPrev(null);
            	currNode = null;
            }
        }
        // search for value in rest of list
        else {
            currNode = currNode.getNext();

            while (currNode != null) {
                if (currNode.getValue().compareTo(item) == 0) {
                	int number = currNode.getFound();
                	mLength -= number;
                	Node prevNode = currNode.getPrev();
                    prevNode.setNext(currNode.getNext());
                    // check if tail
                    if (currNode.getNext() != null) {
                    	currNode.getNext().setPrev(prevNode);
                    }
                    else {
                    	mTail = prevNode;
                    }
                    currNode = null;
                    break;
                }
                else
                	currNode = currNode.getNext();
            }	
        }
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
        int found = 0;

        while (currNode != null) {
        	str.append(currNode.getValue() + printDelim + currNode.getFound() + "\n");
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
        /** Stored value of the same value. */
        private int mNumber;

        public Node(T value) {
            mValue = value;
            mNext = null;
            mPrev = null;
            mNumber = 1;
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
        
        public int getFound() {
        	return mNumber;
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
        
        public void increaseFound() {
        	mNumber++;
        }
        
        public void decreaseFound() {
        	mNumber--;
        }
    } // end of inner class Node
	
} // end of class SortedLinkedListMultiset