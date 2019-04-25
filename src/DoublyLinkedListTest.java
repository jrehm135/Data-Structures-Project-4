import junit.framework.TestCase;

public class doublyLinkedListTest extends TestCase{
    doublyLinkedList<freeBlock> testList;
    
    public void testInsert() {
        testList = new doublyLinkedList<freeBlock>();
        testList.insert(new freeBlock(0,3000));
        testList.insert(new freeBlock(0,4000));
        testList.insert(new freeBlock(0,5000));
        assertTrue(testList.getLength() == 3);
    }
    
    public void testIterator() {
        testList = new doublyLinkedList<freeBlock>();
        testList.insert(new freeBlock(0,3000));
        testList.insert(new freeBlock(3000,4000));
        testList.insert(new freeBlock(7000,5000));
        assertTrue(testList.getLength() == 3);
        testList.moveToHead();
        assertTrue(testList.next());
        freeBlock actual = testList.getElement();
        freeBlock expected = new freeBlock(0,3000);
        assertTrue(actual.getLength() == expected.getLength());
        assertTrue(actual.getPos() == expected.getPos());
        testList.moveToTail();
        assertTrue(testList.previous());
        actual = testList.getElement();
        expected = new freeBlock(7000,5000);
        assertTrue(actual.getLength() == expected.getLength());
        assertTrue(actual.getPos() == expected.getPos());
    }
    
    public void testRemove() {
        testList = new doublyLinkedList<freeBlock>();
        testList.insert(new freeBlock(0,3000));
        testList.insert(new freeBlock(3000,4000));
        testList.insert(new freeBlock(7000,5000));
        testList.moveToTail();
        assertFalse(testList.remove());
        testList.previous();
        testList.remove();
        assertTrue(testList.getLength() == 2);
    }
}
