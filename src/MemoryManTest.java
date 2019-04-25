import junit.framework.TestCase;

public class MemoryManTest extends TestCase{
    public MemoryMan testMan;


    public void testInit() {
        try {
            testMan = new MemoryMan(1000, "bioFile.bin");
            assertTrue(testMan.getCurMemSize() == 1000);
        }
        catch (Exception IOException) {
            System.out.println("Could not find memory file.");
        }
    }
    
    public void testInsert() {
        try {
            testMan = new MemoryMan(1000, "bioFile.bin");
        }
        catch (Exception IOException) {
            System.out.println("Could not find memory file.");
            return;
        }
        testMan.insert("AAAAA", "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT");//2 + 10 = 12
        DoublyLinkedList <FreeBlock> testList = testMan.getfreeBlocksList();
        testList.moveToHead();
        testList.next();
        FreeBlock tempBlock = testList.getElement();
        assertTrue(tempBlock.getPos() == 12);
        assertTrue(tempBlock.getLength() == 988);
        
        
    }

}
