import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import junit.framework.TestCase;

/**
 * test the hash table
 * 
 * @author Josh Rehm
 * @author Quinton Miller
 * @version 5/10/2019
 *
 */
public class HashTest extends TestCase {
    private MemoryMan testMan;
    private SequenceHash<MemHandle> hashTable;


    /**
     * test the insert function
     */
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
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 2 + 10 = 12
        try {
            hashTable.insert("AAAAA", handles, new RandomAccessFile(
                "biofile.bin", "rw"));
            hashTable.insert("AAAAA", handles, new RandomAccessFile(
                "biofile.bin", "rw"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        MemHandle[] fromTable = hashTable.getHandles((int)hashTable.hash(
            "AAAAA", 1000));
        assertEquals(fromTable[0], handles[0]);
        assertEquals(fromTable[1], handles[1]);
        try {
            hashTable.checkForDuplicate("AAAAA", new RandomAccessFile(
                "hashFile.bin", "r"));
        }
        catch (IOException e) {
            System.out.println("Error in duplicate check");
        }
        int[] test = { (int)hashTable.hash("AAAAA", 1000), (int)hashTable.hash(
            "AAAA", 1000) };
        hashTable.getAllHandles(test);
    }


    /**
     * test remove
     * 
     * @throws FileNotFoundException
     *             file isn't found
     * @throws IOException
     *             bad file access
     */
    public void testRemove() throws FileNotFoundException, IOException {
        try {
            testMan = new MemoryMan("bioFile.bin");
            hashTable = new SequenceHash<MemHandle>(1000, "hashFile.bin");
        }
        catch (Exception IOException) {
            System.out.println("Could not find memory file.");
            return;
        }

        MemHandle[] handles = testMan.insert("AAAACCCC",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 2 + 10 = 12
        testMan.insert("CCC", "AAAATTTTCCCC", 12);
        // hashTable.testSetter((int)hashTable.hash("AAAAA", 1000), hans);
        hashTable.insert("AAAACCCC", handles, new RandomAccessFile(
            "biofile.bin", "rw"));

        hashTable.remove("AAAACCCC", new RandomAccessFile("bioFile.bin", "r"));
        testMan.remove(handles);
        MemHandle[] actual = hashTable.getHandles((int)hashTable.hash(
            "AAAACCCC", 1000));
        assertEquals(actual[0].getMemLength(), -1);
        assertEquals(actual[0].getMemLoc(), -1);
        assertEquals(actual[1].getMemLength(), -1);
        assertEquals(actual[1].getMemLoc(), -1);
        hashTable.insert("CCCCAAAA", handles, new RandomAccessFile(
            "biofile.bin", "rw"));
        actual = hashTable.getHandles((int)hashTable.hash("AAAACCCC", 1000));
        // assertNotEquals(actual[0].getMemLength(), -1);
        // assertNotEquals(actual[0].getMemLoc(), -1);
        // assertNotEquals(actual[1].getMemLength(), -1);
        // assertNotEquals(actual[1].getMemLoc(), -1);
// assertEquals(hashTable.getHandles((int)hashTable.hash("AAAAA", 1000)),
// hans);
        hashTable.insert("AAAAA", handles, new RandomAccessFile("biofile.bin",
            "rw"));
    }


    /**
     * test remove
     * 
     * @throws FileNotFoundException
     *             file isn't found
     * @throws IOException
     *             bad file access
     */
    public void testSmallRemove() throws FileNotFoundException, IOException {
        try {
            testMan = new MemoryMan("bioFile.bin");
            hashTable = new SequenceHash<MemHandle>(32, "hashFile.bin");
        }
        catch (Exception IOException) {
            System.out.println("Could not find memory file.");
            return;
        }

        MemHandle[] handles = testMan.insert("A",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 1 + 10 = 11
        MemHandle[] handles1 = testMan.insert("AA",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 1 + 10 = 11
        MemHandle[] handles2 = testMan.insert("AAA",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 1 + 10 = 11
        MemHandle[] handles3 = testMan.insert("AAAA",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 1 + 10 = 11
        MemHandle[] handles4 = testMan.insert("AAAAC",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 2 + 10 = 12
        MemHandle[] handles5 = testMan.insert("AAAACC",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 2 + 10 = 12
        MemHandle[] handles6 = testMan.insert("AAAACCC",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 2 + 10 = 12
        MemHandle[] handles7 = testMan.insert("AAAACCCC",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 2 + 10 = 12
        MemHandle[] handles8 = testMan.insert("C",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 1 + 10 = 11
        MemHandle[] handles9 = testMan.insert("CC",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 1 + 10 = 11
        MemHandle[] handles10 = testMan.insert("CCC",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 1 + 10 = 11
        MemHandle[] handles11 = testMan.insert("CCCC",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 1 + 10 = 11
        MemHandle[] handles12 = testMan.insert("CCCCA",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 2 + 10 = 12
        MemHandle[] handles13 = testMan.insert("CCCCAA",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 2 + 10 = 12
        MemHandle[] handles14 = testMan.insert("CCCCAAA",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 2 + 10 = 12
        MemHandle[] handles15 = testMan.insert("CCCCAAAA",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 2 + 10 = 12
        MemHandle[] handles16 = testMan.insert("G",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 1 + 10 = 11
        MemHandle[] handles17 = testMan.insert("GG",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 1 + 10 = 11
        MemHandle[] handles18 = testMan.insert("GGG",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 1 + 10 = 11
        MemHandle[] handles19 = testMan.insert("GGGG",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 1 + 10 = 11
        MemHandle[] handles20 = testMan.insert("GGGGT",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 2 + 10 = 12
        MemHandle[] handles21 = testMan.insert("GGGGTT",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 2 + 10 = 12
        MemHandle[] handles22 = testMan.insert("GGGGTTT",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 2 + 10 = 12
        MemHandle[] handles23 = testMan.insert("GGGGTTTT",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 2 + 10 = 12
        MemHandle[] handles24 = testMan.insert("T",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 1 + 10 = 11
        MemHandle[] handles25 = testMan.insert("TT",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 1 + 10 = 11
        MemHandle[] handles26 = testMan.insert("TTT",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 1 + 10 = 11
        MemHandle[] handles27 = testMan.insert("TTTT",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 1 + 10 = 11
        MemHandle[] handles28 = testMan.insert("TTTTG",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 2 + 10 = 12
        MemHandle[] handles29 = testMan.insert("TTTTGG",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 2 + 10 = 12
        MemHandle[] handles30 = testMan.insert("TTTTGGG",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 2 + 10 = 12
        MemHandle[] handles31 = testMan.insert("TTTTGGGG",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 2 + 10 = 12

        hashTable.insert("A", handles, new RandomAccessFile("biofile.bin",
            "rw"));
        hashTable.insert("AA", handles1, new RandomAccessFile("biofile.bin",
            "rw"));
        hashTable.insert("AAA", handles2, new RandomAccessFile("biofile.bin",
            "rw"));
        hashTable.insert("AAAA", handles3, new RandomAccessFile("biofile.bin",
            "rw"));
        hashTable.insert("AAAAC", handles4, new RandomAccessFile("biofile.bin",
            "rw"));
        hashTable.insert("AAAACC", handles5, new RandomAccessFile("biofile.bin",
            "rw"));
        hashTable.insert("AAAACCC", handles6, new RandomAccessFile(
            "biofile.bin", "rw"));
        hashTable.insert("AAAACCCC", handles7, new RandomAccessFile(
            "biofile.bin", "rw"));

        hashTable.insert("C", handles8, new RandomAccessFile("biofile.bin",
            "rw"));
        hashTable.insert("CC", handles9, new RandomAccessFile("biofile.bin",
            "rw"));
        hashTable.insert("CCC", handles10, new RandomAccessFile("biofile.bin",
            "rw"));
        hashTable.insert("CCCC", handles11, new RandomAccessFile("biofile.bin",
            "rw"));
        hashTable.insert("CCCCA", handles12, new RandomAccessFile("biofile.bin",
            "rw"));
        hashTable.insert("CCCCAA", handles13, new RandomAccessFile(
            "biofile.bin", "rw"));
        hashTable.insert("CCCCAAA", handles14, new RandomAccessFile(
            "biofile.bin", "rw"));
        hashTable.insert("CCCCAAAA", handles15, new RandomAccessFile(
            "biofile.bin", "rw"));

        hashTable.insert("G", handles16, new RandomAccessFile("biofile.bin",
            "rw"));
        hashTable.insert("GG", handles17, new RandomAccessFile("biofile.bin",
            "rw"));
        hashTable.insert("GGG", handles18, new RandomAccessFile("biofile.bin",
            "rw"));
        hashTable.insert("GGGG", handles19, new RandomAccessFile("biofile.bin",
            "rw"));
        hashTable.insert("GGGGT", handles20, new RandomAccessFile("biofile.bin",
            "rw"));
        hashTable.insert("GGGGTT", handles21, new RandomAccessFile(
            "biofile.bin", "rw"));
        hashTable.insert("GGGGTTT", handles22, new RandomAccessFile(
            "biofile.bin", "rw"));
        hashTable.insert("GGGGTTTT", handles23, new RandomAccessFile(
            "biofile.bin", "rw"));

        hashTable.insert("T", handles24, new RandomAccessFile("biofile.bin",
            "rw"));
        hashTable.insert("TT", handles25, new RandomAccessFile("biofile.bin",
            "rw"));
        hashTable.insert("TTT", handles26, new RandomAccessFile("biofile.bin",
            "rw"));
        hashTable.insert("TTTT", handles27, new RandomAccessFile("biofile.bin",
            "rw"));
        hashTable.insert("TTTTG", handles28, new RandomAccessFile("biofile.bin",
            "rw"));
        hashTable.insert("TTTTGG", handles29, new RandomAccessFile(
            "biofile.bin", "rw"));
        hashTable.insert("TTTTGGG", handles30, new RandomAccessFile(
            "biofile.bin", "rw"));
        hashTable.insert("TTTTGGGG", handles31, new RandomAccessFile(
            "biofile.bin", "rw"));

        MemHandle[] remHan1 = hashTable.remove("A", new RandomAccessFile(
            "biofile.bin", "rw"));
        MemHandle[] remHan2 = hashTable.remove("TTTTGGGG", new RandomAccessFile(
            "biofile.bin", "rw"));
        testMan.remove(remHan1);
        testMan.remove(remHan2);

        handles31 = testMan.insert("TTTTGGGG",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 1 + 10 = 11
        hashTable.insert("TTTTGGGG", handles, new RandomAccessFile(
            "biofile.bin", "rw"));

        handles = testMan.insert("A",
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 1 + 10 = 11
        hashTable.insert("A", handles, new RandomAccessFile("biofile.bin",
            "rw"));
        DoublyLinkedList<FreeBlock> test = testMan.getFreeBlocksList();
        assertEquals(test.getLength(), 1);

    }


    /**
     * test the search function
     * 
     * @throws FileNotFoundException
     *             file not found
     * @throws IOException
     *             bad file access
     */
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
            "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT", 40); // 2 + 10 = 12
        hashTable.insert("AAAAA", handles, new RandomAccessFile("biofile.bin",
            "rw"));
        String s = hashTable.search("AAAAA", new RandomAccessFile("bioFile.bin",
            "rw"));
        assertEquals(s, "AAAATTTTCCCCGGGGAAAACCCCGGGGTTTTAAAATTTT");
    }
}
