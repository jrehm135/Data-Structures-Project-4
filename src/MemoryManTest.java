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
    
    public void testRemove() {
        try {
            testMan = new MemoryMan(1000, "bioFile.bin");
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
        //Test equal to block size
        testMan.insert("TTT", "AAAATTTTCCCCGGGGAA");//1 + 5 = 6
        
        DoublyLinkedList <FreeBlock> testList = testMan.getfreeBlocksList();
        
        
        
    }

}
