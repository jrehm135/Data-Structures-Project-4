/**
 * 
 */

/**
 * @author Josh
 *
 */
public class DoublyLinkedList<T> {

    //Node class for doubly linked list
    private class Node {
        Node previous;
        Node next;
        T element;

        /**
         * Create an innernode for this list
         * 
         * @param element
         *            - data to contain in the node
         */
        public Node(T element) {
            this.previous = null;
            this.next = null;
            this.element = element;
        }
    }
    
    private int length;
    private Node head;
    private Node tail;
    private Node curr;
    
    /**
     * Constructor for list
     */
    public DoublyLinkedList() {
        length = 0;
        head = new Node(null);
        tail = new Node(null);
        head.next = tail;
        tail.previous = head;
        curr = head;
    }

    /**
     * Get the length of the list
     * 
     * @return - length of the list
     */
    public int getLength() {
        return this.length;
    }

    /**
     * insert element after the current curr node, and
     * sets the curr pointer to itself
     * 
     * @param e Element to insert
     */
    public void insert(T e) {
        Node newNode = new Node(e);
        
        newNode.next = curr.next;
        curr.next = newNode;
        newNode.previous = curr;
        
        if (curr != tail) {
            newNode.next.previous = newNode;
        }
        else {
            tail = newNode;
        }
        length++;
        //Set to next value
        curr = newNode;
    }

    /**
     * remove the element at curr
     * 
     * @return - true on success - false on failure
     */
    public boolean remove() {
        if (curr == tail) {
            return false;
        }
        else if (curr.next == tail) {
            tail.previous = curr.previous;
            curr.previous.next = tail;
            length--;
            return true;
        }
        else {
            curr.element = curr.next.element;
            curr.next = curr.next.next;
            curr.next.previous = curr;
            length--;
            return true;
        }
    }

    /**
     * test if curr has a next node
     * @return true if it has a next, false otherwise
     */
    public boolean hasNext() {
        return (curr.next != tail) && (curr.next != null);
    }

    /**
     * moves curr to the next node
     * 
     * @return boolean value representing if curr has moved
     */
    public boolean next() {
        if (hasNext()) {
            curr = curr.next;
            return true;
        }
        return false;
    }

    /**
     * moves curr to the previous node
     * 
     * @return boolean value representing if curr has moved
     */
    public boolean previous() {
        if (curr.previous != head && curr.previous != null) {
            curr = curr.previous;
            return true;
        }
        return false;
    }

    /**
     * moves curr to the beginning of the list
     */
    public void moveToHead() {
        curr = head;
    }

    /**
     * moves curr to the end of the list
     */
    public void moveToTail() {
        curr = tail;
    }

    /**
     * get the data stored in current node
     * @return The element at the current position
     */
    public T getElement() {
        return curr.element;
    }
    
    public void setElement(T e) {
        curr.element = e;
    }

    /**
     * @param e The element to search the list for
     * @return Whether or not element was found
     */
    public boolean search(T e) {
        Node temp = head.next;
        while (temp != tail) {
            if (temp.element.equals(e)) {
                curr = temp;
                return true;
            }
            temp = temp.next;
        }
        return false;
    }
}
