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
            //Need to initialize a different array to store
            MemHandle[] hashHandle = new MemHandle[2];
            System.arraycopy(handles, 0, hashHandle, 0, 2);
            tableArray[(int)hashSlot] = hashHandle;
            return true;
        }
        // Move to next slot
        currSlot = (currSlot + 1) % 32;

        // Iterate through slots until one is found
        while ((bucket * 32) + currSlot != hashSlot) {
            if (tableArray[(bucket * 32) + currSlot][0] == null ||
                    tableArray[(bucket * 32) + currSlot][0].equals(
                            new MemHandle(-1, -1))) {
                //Need to initialize a different array to store
                MemHandle[] hashHandle = new MemHandle[2];
                System.arraycopy(handles, 0, hashHandle, 0, 2);
                tableArray[(bucket * 32) + currSlot] = hashHandle;
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
        if (tableArray[(bucket * 32) + currSlot][0] == null) {
            return false;
        }
        do {
            //We want to skip over tombstones
            if (tableArray[(bucket * 32) + currSlot][0] == null ||
                    tableArray[(bucket * 32) + currSlot][0].equals
                    (new MemHandle(-1, -1))) {
                continue;
            }
            MemHandle idHandle = tableArray[(bucket * 32) + currSlot][0];
            int offset = idHandle.getMemLoc();
            int length = idHandle.getMemLength();
            byte[] fromFile = new byte[(int)Math.ceil(idHandle.getMemLength()
                / 4.0)];
            //Read from file location for comparison
            file.seek(offset);
            file.read(fromFile, 0, (int)Math.ceil(idHandle.getMemLength()
                / 4.0));
            //Convert to a sequence to compare
            Sequence temp = new Sequence();
            String fullBytes = temp.bytesToString(fromFile);
            //We must then shorten the string to the proper length
            String fileID = fullBytes.substring(0, length);
            if (fileID.equals(seqID)) {
                //For a tombstone, we use values of -1, impossible length and offset
                tableArray[(bucket * 32) + currSlot][0] = new MemHandle(-1, -1);
                tableArray[(bucket * 32) + currSlot][1] = new MemHandle(-1, -1);
                return true;
            }
            // Move to next slot
            currSlot = (currSlot + 1) % 32;
        }
        while((bucket * 32) + currSlot != hashSlot);
        //The sequence id was not found in any slot
        return false;
    }


    @Override
    public String search(String seqID, RandomAccessFile file)
        throws IOException {
        long hashSlot = hash(seqID, tableSize);
        int bucket = (int)hashSlot / 32;
        int currSlot = (int)hashSlot % 32;

        // look at first slot
        if (tableArray[(bucket * 32) + currSlot][0] == null) {
            return "";
        }
        do {
            MemHandle idHandle = tableArray[(bucket * 32) + currSlot][0];
            int offset = idHandle.getMemLoc();
            int length = idHandle.getMemLength();
            byte[] fromFile = new byte[(int)Math.ceil(idHandle.getMemLength()
                / 4.0)];
            //Read from file location for comparison
            file.seek(offset);
            file.read(fromFile, 0, (int)Math.ceil(idHandle.getMemLength()
                / 4.0));
            //Convert to a sequence to compare
            Sequence temp = new Sequence();
            String fullBytes = temp.bytesToString(fromFile);
            //We must then shorten the string to the proper length
            String fileID = fullBytes.substring(0, length);
            if (fileID.equals(seqID)) {
                MemHandle seqHandle = tableArray[(bucket * 32) + currSlot][1];
                int seqOffset = seqHandle.getMemLoc();
                int seqLength = seqHandle.getMemLength();
                byte[] seqFromFile = new byte[(int)Math.ceil(seqHandle.getMemLength()
                    / 4.0)];
                //Read from file location for comparison
                file.seek(seqOffset);
                file.read(seqFromFile, 0, (int)Math.ceil(seqHandle.getMemLength()
                    / 4.0));
                //Convert to a sequence to compare
                Sequence tempSeq = new Sequence();
                String seqFullBytes = tempSeq.bytesToString(seqFromFile);
                //We must then shorten the string to the proper length
                String finalSeq = seqFullBytes.substring(0, seqLength);
                return finalSeq;
            }
            // Move to next slot
            currSlot = (currSlot + 1) % 32;
        }
        while((bucket * 32) + currSlot != hashSlot);
        return "";
    }
    
    @Override
    public MemHandle[] getHandles(int location) {
        return tableArray[location];
    }
    
    //A test method for insert/remove
    public void testSetter(int location, MemHandle[] hans) {
        tableArray[location] = hans;
    }

}
