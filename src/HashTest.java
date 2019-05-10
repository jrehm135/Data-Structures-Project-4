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

        MemHandle[] handles = testMan.insert("AAAAA",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT");// 2 + 10 = 12
        try {
            hashTable.insert("AAAAA", handles, new RandomAccessFile("biofile.bin", "rw"));
            hashTable.insert("AAAAA", handles, new RandomAccessFile("biofile.bin", "rw"));
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        MemHandle[] fromTable = hashTable.getHandles((int)hashTable.hash(
            "AAAAA", 1000));
        assertEquals(fromTable[0], handles[0]);
        assertEquals(fromTable[1], handles[1]);
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

        MemHandle[] handles = testMan.insert("AAAAA",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT");// 2 + 10 = 12
        MemHandle[] hans = testMan.insert("CCC", "AAAATTTTCCCC");
        hashTable.testSetter((int)hashTable.hash("AAAAA", 1000), hans);
        hashTable.insert("AAAAA", handles, new RandomAccessFile("biofile.bin", "rw"));

        hashTable.remove("AAAAA", new RandomAccessFile("bioFile.bin", "r"));
        testMan.remove(handles);
        assertEquals(hashTable.getHandles((int)hashTable.hash("AAAAA", 1000)),
            hans);
        hashTable.insert("AAAAA", handles, new RandomAccessFile("biofile.bin", "rw"));
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
        MemHandle[] handles = testMan.insert("AAAAA",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT");// 2 + 10 = 12
        hashTable.insert("AAAAA", handles, new RandomAccessFile("biofile.bin", "rw"));
        String s = hashTable.search("AAAAA", new RandomAccessFile("bioFile.bin",
            "rw"));
        assertEquals(s, "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT");
    }
}
