import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

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
            testMan = new MemoryMan("bioFile.bin");
            hashTable = new SequenceHash<MemHandle>(1000, "hashFile.bin");
        }
        catch (Exception IOException) {
            System.out.println("Could not find memory file.");
            return;
        }
        
        MemHandle[] handles = testMan.insert("AAAAA", "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT");//2 + 10 = 12
        try {
            hashTable.insert("AAAAA", handles);
            hashTable.insert("AAAAA", handles);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        assertEquals(hashTable.getHandles(
                (int)hashTable.hash("AAAAA", 1000)),
                handles);
    }
    
    public void testRemove() throws FileNotFoundException, IOException {
        try {
            testMan = new MemoryMan("bioFile.bin");
            hashTable = new SequenceHash<MemHandle>(1000, "hashFile.bin");
        }
        catch (Exception IOException) {
            System.out.println("Could not find memory file.");
            return;
        }
        
        MemHandle[] handles = testMan.insert("AAAAA", "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT");//2 + 10 = 12
        MemHandle[] hans = testMan.insert("CCC", "AAAATTTTCCCC");
        hashTable.testSetter((int)hashTable.hash("AAAAA", 1000), hans);
        hashTable.insert("AAAAA", handles);
        
        hashTable.remove("AAAAA", new RandomAccessFile("bioFile.bin", "r"));
        testMan.remove(handles);
        assertEquals(hashTable.getHandles(
                (int)hashTable.hash("AAAAA", 1000)), 
                hans);
        hashTable.insert("AAAAA", handles);
    }
    
    public void testSearch() throws FileNotFoundException, IOException {
        try {
            testMan = new MemoryMan("bioFile.bin");
            hashTable = new SequenceHash<MemHandle>(1000, "hashFile.bin");
        }
        catch (Exception IOException) {
            System.out.println("Could not find memory file.");
            return;
        }
        MemHandle[] handles = testMan.insert("AAAAA", "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT");//2 + 10 = 12
        hashTable.insert("AAAAA", handles);
        String s = hashTable.search("AAAAA", new RandomAccessFile("bioFile.bin", "rw"));
        assertEquals(s, "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT");
    }
}
