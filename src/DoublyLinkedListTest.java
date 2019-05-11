import junit.framework.TestCase;

/**
 * @author Josh Rehm
 * @author Quinton Miller
 * @version 5/10/2019
 *          Test the linked list
 */
public class DoublyLinkedListTest extends TestCase {
    /**
     * list for testing
     */
    private DoublyLinkedList<FreeBlock> testList;


    /**
     * test insert
     */
    public void testInsert() {
        testList = new DoublyLinkedList<FreeBlock>();
        testList.insert(new FreeBlock(0, 3000));
        testList.insert(new FreeBlock(0, 4000));
        testList.insert(new FreeBlock(0, 5000));
        testList.insertBefore(new FreeBlock(0, 70000));
        assertEquals(testList.getLength(), 4);
    }


    /**
     * test the iterator
     */
    public void testIterator() {
        testList = new DoublyLinkedList<FreeBlock>();
        testList.insert(new FreeBlock(0, 3000));
        testList.insert(new FreeBlock(3000, 4000));
        testList.insert(new FreeBlock(7000, 5000));
        assertEquals(testList.getLength(), 3);
        testList.moveToHead();
        assertTrue(testList.next());
        FreeBlock actual = testList.getElement();
        FreeBlock expected = new FreeBlock(0, 3000);
        assertEquals(actual.getLength(), expected.getLength());
        assertEquals(actual.getPos(), expected.getPos());
        testList.moveToTail();
        assertFalse(testList.next());
        assertTrue(testList.previous());
        actual = testList.getElement();
        testList.moveToHead();
        assertFalse(testList.previous());
        expected = new FreeBlock(7000, 5000);
        assertEquals(actual.getLength(), expected.getLength());
        assertEquals(actual.getPos(), expected.getPos());
        testList.next();
        FreeBlock test = new FreeBlock(45, 8992);
        testList.setElement(test);
        assertTrue(testList.search(test));
        FreeBlock foo = new FreeBlock(0, 0);
        assertFalse(test.isAdjacent(foo));
        FreeBlock foo2 = new FreeBlock(55, 0);
        assertFalse(test.isAdjacent(foo2));
        foo2.setBlock(60, 68);
    }


    /**
     * test remove
     */
    public void testRemove() {
        testList = new DoublyLinkedList<FreeBlock>();
        testList.insert(new FreeBlock(0, 3000));
        testList.insert(new FreeBlock(3000, 4000));
        testList.insert(new FreeBlock(7000, 5000));
        testList.moveToTail();
        assertFalse(testList.remove());
        testList.previous();
        testList.remove();
        testList.previous();
        testList.previous();
        testList.remove();
        assertEquals(testList.getLength(), 1);
    }
}
