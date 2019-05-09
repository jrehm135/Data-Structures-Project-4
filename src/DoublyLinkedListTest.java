import junit.framework.TestCase;

public class DoublyLinkedListTest extends TestCase{
    DoublyLinkedList<FreeBlock> testList;
    
    public void testInsert() {
        testList = new DoublyLinkedList<FreeBlock>();
        testList.insert(new FreeBlock(0,3000));
        testList.insert(new FreeBlock(0,4000));
        testList.insert(new FreeBlock(0,5000));
        assertTrue(testList.getLength() == 3);
    }
    
    public void testIterator() {
        testList = new DoublyLinkedList<FreeBlock>();
        testList.insert(new FreeBlock(0,3000));
        testList.insert(new FreeBlock(3000,4000));
        testList.insert(new FreeBlock(7000,5000));
        assertTrue(testList.getLength() == 3);
        testList.moveToHead();
        assertTrue(testList.next());
        FreeBlock actual = testList.getElement();
        FreeBlock expected = new FreeBlock(0,3000);
        assertTrue(actual.getLength() == expected.getLength());
        assertTrue(actual.getPos() == expected.getPos());
        testList.moveToTail();
        assertTrue(testList.previous());
        actual = testList.getElement();
        expected = new FreeBlock(7000,5000);
        assertTrue(actual.getLength() == expected.getLength());
        assertTrue(actual.getPos() == expected.getPos());
    }
    
    public void testRemove() {
        testList = new DoublyLinkedList<FreeBlock>();
        testList.insert(new FreeBlock(0,3000));
        testList.insert(new FreeBlock(3000,4000));
        testList.insert(new FreeBlock(7000,5000));
        testList.moveToTail();
        assertFalse(testList.remove());
        testList.previous();
        testList.remove();
        assertTrue(testList.getLength() == 2);
    }
}