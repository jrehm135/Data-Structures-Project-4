import java.io.IOException;
import junit.framework.TestCase;

/**
 * test the memory mnager
 * 
 * @author Josh Rehm
 * @author Quinton Miller
 * @version 5/10/2019
 */
public class MemoryManTest extends TestCase {
    private MemoryMan testMan;


    /**
     * test the initialization
     */
    public void testInit() {
        try {
            testMan = new MemoryMan("bioFile.bin");
            assertEquals(testMan.getCurMemSize(), 0);
        }
        catch (Exception IOException) {
            System.out.println("Could not find memory file.");
        }
    }


    /**
     * test insert
     */
    public void testInsert() {
        try {
            testMan = new MemoryMan("bioFile.bin");
        }
        catch (Exception IOException) {
            System.out.println("Could not find memory file.");
            return;
        }
        MemHandle[] han = testMan.insert("AAAAA",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 2 + 10 = 12
        testMan.insert("AAAA", "AAAATTTTCCCCGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 2
                                                                               // +
                                                                               // 10
                                                                               // =
                                                                               // 12
        DoublyLinkedList<FreeBlock> testList = testMan.getFreeBlocksList();
        assertEquals(testList.getLength(), 0);
        assertEquals(han[0].getMemLength(), 5);
        assertEquals(han[0].getMemLoc(), 0);
        assertEquals(han[1].getMemLength(), 40);
        assertEquals(han[1].getMemLoc(), 2);
        String[] test = { "AAAAA", "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT" };
        try {
            testMan.search(han, test);
            testMan.print(han);
        }
        catch (IOException e) {
            System.out.println("Error in search");
        }
    }


    /**
     * test remove
     */
    public void testRemove() {
        try {
            testMan = new MemoryMan("bioFile.bin");
        }
        catch (Exception IOException) {
            System.out.println("Could not find memory file.");
            return;
        }
        MemHandle[] handles = testMan.insert("AAAAA",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 2 + 10 = 12
        testMan.insert("TTTTT", "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40);
        // 2 + 10 = 12

        testMan.remove(handles);

        // Test less than block size
        testMan.insert("CCC", "AAAATTTTCCCCGGGGAA",18); // 1 + 5 = 6

        DoublyLinkedList<FreeBlock> testList = testMan.getFreeBlocksList();
        testList.moveToHead();
        testList.next();
        FreeBlock tempBlock = testList.getElement();
        assertEquals(tempBlock.getPos(), 6);
        assertEquals(tempBlock.getLength(), 6);
        testList.next();

        // Test equal to block size
        testMan.insert("TTT", "AAAATTTTCCCCGGGGAA", 18); // 1 + 5 = 6

        testList = testMan.getFreeBlocksList();
        assertEquals(testList.getLength(), 0);

    }

}
