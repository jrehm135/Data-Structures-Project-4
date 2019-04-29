import junit.framework.TestCase;

/**
 * 
 */

/**
 * @author Josh
 *
 */
public class HashTest extends TestCase {
    private MemoryMan testMan;
    private SequenceHash<MemHandle> hashTable;
    
    public void testInsert() {
        try {
            testMan = new MemoryMan(1000, "bioFile.bin");
            hashTable = new SequenceHash<MemHandle>(1000, "hashFile.bin");
        }
        catch (Exception IOException) {
            System.out.println("Could not find memory file.");
            return;
        }
        
        MemHandle[] handles = testMan.insert("AAAAA", "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT");//2 + 10 = 12
        hashTable.insert("AAAAA", handles);
        hashTable.insert("AAAAA", handles);
    }
}
