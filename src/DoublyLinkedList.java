/**
 * @author Josh Rehm
 * @author Quinton Miller
 *
 * @param <T>
 *            element types of linked list
 * @version 5/8/2019
 */
public class DoublyLinkedList<T> {

    // Node class for doubly linked list
    private class Node {
        private Node previous;
        private Node next;
        private T element;


        /**
         * Create an inner node for this list
         * 
         * @param element
         *            - data to contain in the node
         */
        public Node(T element) {
            this.previous = null;
            this.next = null;
            this.element = element;
        }


        /**
         * set the previous Node
         * 
         * @param newPrev
         *            the node to set previous to
         */
        public void setPrevious(Node newPrev) {
            previous = newPrev;
        }


        /**
         * get the previous node
         * 
         * @return previous node
         */
        public Node getPrevious() {
            return previous;
        }


        /**
         * set the next node
         * 
         * @param newNext
         *            nod to set next to
         */
        public void setNext(Node newNext) {
            next = newNext;
        }


        /**
         * get the next node in the list
         * 
         * @return next node in list
         */
        public Node getNext() {
            return next;
        }


        /**
         * set the element of the node
         * 
         * @param newElement
         *            new node element
         */
        public void setElement(T newElement) {
            element = newElement;
        }


        /**
         * get the nod element
         * 
         * @return node element
         */
        public T getElement() {
            return element;
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
        head.setNext(tail);
        tail.setPrevious(head);
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
     * @param e
     *            Element to insert
     */
    public void insert(T e) {
        Node newNode = new Node(e);

        newNode.setNext(curr.getNext());
        curr.setNext(newNode);
        newNode.setPrevious(curr);

        if (curr != tail) {
            newNode.getNext().setPrevious(newNode);
        }
        else {
            tail = newNode;
        }
        length++;
        // Set to next value
        curr = newNode;
    }


    /**
     * insert element after the current curr node, and
     * sets the curr pointer to itself
     * 
     * @param e
     *            Element to insert
     */
    public void insertBefore(T e) {
        Node newNode = new Node(e);

        newNode.setPrevious(curr.getPrevious());
        curr.setPrevious(newNode);
        newNode.setNext(curr);

        if (curr != head) {
            newNode.getPrevious().setNext(newNode);
        }
        else {
            head = newNode;
        }

        length++;
        // Set to next value
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
        else if (curr.getNext() == tail) {
            tail.setPrevious(curr.getPrevious());
            curr.getPrevious().setNext(tail);
            length--;
            return true;
        }
        else {
            curr.setElement(curr.getNext().getElement());
            curr.setNext(curr.getNext().getNext());
            curr.getNext().setPrevious(curr);
            length--;
            return true;
        }
    }


    /**
     * test if curr has a next node
     * 
     * @return true if it has a next, false otherwise
     */
    public boolean hasNext() {
        return (curr.getNext() != tail) && (curr.getNext() != null);
    }


    /**
     * moves curr to the next node
     * 
     * @return boolean value representing if curr has moved
     */
    public boolean next() {
        if (hasNext()) {
            curr = curr.getNext();
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
        if (curr.getPrevious() != head && curr.getPrevious() != null) {
            curr = curr.getPrevious();
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
     * 
     * @return The element at the current position
     */
    public T getElement() {
        return curr.getElement();
    }


    /**
     * set the element of curr
     * 
     * @param e
     *            new element of curr
     */
    public void setElement(T e) {
        curr.setElement(e);
    }


    /**
     * @param e
     *            The element to search the list for
     * @return Whether or not element was found
     */
    public boolean search(T e) {
        Node temp = head.getNext();
        while (temp != tail) {
            if (temp.getElement().equals(e)) {
                curr = temp;
                return true;
            }
            temp = temp.getNext();
        }
        return false;
    }
}
