import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 */

/**
 * @author Josh
 *
 */
public class SequenceHash<T extends MemHandle> implements HashTable<MemHandle> {
    private MemHandle[][] tableArray;
    private int tableSize;
    private RandomAccessFile file;


    // Open up a hash file and allocate space for MemHandles
    SequenceHash(int tableSize, String hashFile) {
        try {// Create a matrix with size tablesize and 2 elements per row
            tableArray = new MemHandle[tableSize][2];
            this.tableSize = tableSize;

            file = new RandomAccessFile(hashFile, "rw");
        }
        catch (IOException e) {
            System.out.println("Error in sequence hash constructor: " + e
                .toString());
        }
    }


    // This implements the sfold hash function
    @Override
    public long hash(String s, int M) {
        int intLength = s.length() / 4;
        long sum = 0;
        for (int j = 0; j < intLength; j++) {
            char c[] = s.substring(j * 4, (j * 4) + 4).toCharArray();
            long mult = 1;
            for (int k = 0; k < c.length; k++) {
                sum += c[k] * mult;
                mult *= 256;
            }
        }

        char c[] = s.substring(intLength * 4).toCharArray();
        long mult = 1;
        for (int k = 0; k < c.length; k++) {
            sum += c[k] * mult;
            mult *= 256;
        }

        sum = (sum * sum) >> 8;
        return (Math.abs(sum) % M);
    }


    // Insert a memory handle into hashtable
    @Override
    public boolean insert(String seqID, MemHandle[] handles) {
        long hashSlot = hash(seqID, tableSize);
        int bucket = (int)hashSlot / 32;
        int currSlot = (int)hashSlot % 32;

        // Try first slot to check for availability
        // We only need to check the first value since the values are related
        if (tableArray[(int)hashSlot][0] == null) {
            tableArray[(int)hashSlot] = handles;
            return true;
        }
        // Move to next slot
        currSlot = (currSlot + 1) % 32;

        // Iterate through slots until one is found
        while ((bucket * 32) + currSlot != hashSlot) {
            if (tableArray[(bucket * 32) + currSlot][0] == null) {
                tableArray[(bucket * 32) + currSlot] = handles;
                return true;
            }

            // Move to next slot
            currSlot = (currSlot + 1) % 32;
        }

        // If we make it here, then the bucket is full, reject insert
        return false;
    }


    @Override
    public boolean remove(String seqID, RandomAccessFile file)
        throws IOException {
        long hashSlot = hash(seqID, tableSize);
        int bucket = (int)hashSlot / 32;
        int currSlot = (int)hashSlot % 32;

        // Try first slot to check for availability
        // We only need to check the first value since the values are related
        if (tableArray[(int)hashSlot][0] == null) {
            return false;
        }
        else {
            MemHandle idHandle = tableArray[(int)hashSlot][0];
            int offset = idHandle.getMemLoc();
            int length = idHandle.getMemLength();
            byte[] fromFile = new byte[(int)Math.ceil(idHandle.getMemLength()
                / 4.0)];
            file.seek(offset);
            file.read(fromFile, 0, (int)Math.ceil(idHandle.getMemLength()
                / 4.0));
            Sequence temp = new Sequence();
            String idFromFile = temp.bytesToString(fromFile);
            if (idFromFile.equals(seqID)) {
                tableArray[(int)hashSlot][0] = null;
                tableArray[(int)hashSlot][1] = null;
                return true;
            }
        }
        // Move to next slot
        currSlot = (currSlot + 1) % 32;
        return true;
    }


    @Override
    public boolean search(String seqID, String sequence, RandomAccessFile file)
        throws IOException {
        long hashSlot = hash(seqID, tableSize);
        int bucket = (int)hashSlot / 32;
        int currSlot = (int)hashSlot % 32;

        // look at first slot
        if (tableArray[(int)hashSlot][0] == null) {
            return false;
        }
        return false;
    }

}
