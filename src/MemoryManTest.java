import junit.framework.TestCase;

public class MemoryManTest extends TestCase{
    public MemoryMan testMan;


    public void testInit() {
        try {
            testMan = new MemoryMan("bioFile.bin");
            assertTrue(testMan.getCurMemSize() == 1000);
        }
        catch (Exception IOException) {
            System.out.println("Could not find memory file.");
        }
    }
    
    public void testInsert() {
        try {
            testMan = new MemoryMan("bioFile.bin");
        }
        catch (Exception IOException) {
            System.out.println("Could not find memory file.");
            return;
        }
        MemHandle[] han = testMan.insert("AAAAA", "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT");//2 + 10 = 12
        DoublyLinkedList <FreeBlock> testList = testMan.getFreeBlocksList();
        assertEquals(testList.getLength(), 0);
        assertEquals(han[0].getMemLength(), 5);
        assertEquals(han[0].getMemLoc(), 0);
        assertEquals(han[1].getMemLength(), 40);
        assertEquals(han[1].getMemLoc(), 2);
    }
    
    public void testRemove() {
        try {
            testMan = new MemoryMan("bioFile.bin");
        }
        catch (Exception IOException) {
            System.out.println("Could not find memory file.");
            return;
        }
        MemHandle[] handles = testMan.insert("AAAAA", "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT");//2 + 10 = 12
        testMan.insert("TTTTT", "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT");//2 + 10 = 12
        
        testMan.remove(handles);
        
        //Test less than block size
        testMan.insert("CCC", "AAAATTTTCCCCGGGGAA");//1 + 5 = 6
        
        DoublyLinkedList <FreeBlock> testList = testMan.getFreeBlocksList();
        testList.moveToHead();
        testList.next();
        FreeBlock tempBlock = testList.getElement();
        assertEquals(tempBlock.getPos(), 6);
        assertEquals(tempBlock.getLength(), 6);
        testList.next();
        
        //Test equal to block size
        testMan.insert("TTT", "AAAATTTTCCCCGGGGAA");//1 + 5 = 6
        
        testList = testMan.getFreeBlocksList();
        assertTrue(testList.getLength() == 0);
    }

}
