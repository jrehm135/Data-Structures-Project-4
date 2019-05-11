import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 * @author Josh Rehm
 * @author Quinton Miller
 * @version 5/10/2019
 *
 * @param <T>
 *            things to store in the hash table
 */
public class SequenceHash<T extends MemHandle> implements HashTable<MemHandle> {
    private MemHandle[][] tableArray;
    private int tableSize;
    private RandomAccessFile file;
    private int currSize;


    // Open up a hash file and allocate space for MemHandles
    /**
     * create a new hash table
     * 
     * @param tableSize
     *            size of the table
     * @param hashFile
     *            file to store the hashes in
     */
    SequenceHash(int tableSize, String hashFile) {
        try { // Create a matrix with size tablesize and 2 elements per row
            tableArray = new MemHandle[tableSize][2];
            this.tableSize = tableSize;
            currSize = 0;

            file = new RandomAccessFile(hashFile, "rw");
        }
        catch (IOException e) {
            System.out.println("Error in sequence hash constructor: " + e
                .toString());
        }
    }


    // This implements the sfold hash function
    @Override
    public long hash(String s, int m) {
        int intLength = s.length() / 4;
        long sum = 0;
        for (int j = 0; j < intLength; j++) {
            char[] c = s.substring(j * 4, (j * 4) + 4).toCharArray();
            long mult = 1;
            for (int k = 0; k < c.length; k++) {
                sum += c[k] * mult;
                mult *= 256;
            }
        }

        char[] c = s.substring(intLength * 4).toCharArray();
        long mult = 1;
        for (int k = 0; k < c.length; k++) {
            sum += c[k] * mult;
            mult *= 256;
        }

        sum = (sum * sum) >> 8;
        return (Math.abs(sum) % m);
    }


    // Insert a memory handle into hashtable
    // Returns an int indicating insertion details
    // 1 for successful insert
    // 0 for a value already in table
    // -1 for a full bucket
    @Override
    public int insert(
        String seqID,
        MemHandle[] handles,
        RandomAccessFile seqFile)
        throws IOException {
        long hashSlot = hash(seqID, tableSize);
        int bucket = (int)hashSlot / 32;
        int currSlot = (int)hashSlot % 32;
        MemHandle tombStone = new MemHandle(-1, -1);
        int lowestTombstone = -1;
        boolean tombFound = false;

        // Iterate through slots until one is found
        // Use a for loop to keep track of tombstones
        do {
            if (tableArray[(bucket * 32) + currSlot][0] == null) {
                // Need to initialize a different array to store
                MemHandle[] hashHandle = new MemHandle[2];
                System.arraycopy(handles, 0, hashHandle, 0, 2);
                tableArray[(bucket * 32) + currSlot] = hashHandle;
                currSize++;
                // Store values in hash file
                file.seek(((bucket * 32) + currSlot) * 16);
                file.write(hashHandle[0].getMemLoc());
                file.seek(((bucket * 32) + currSlot) * 16 + 4);
                file.write(hashHandle[0].getMemLength());
                file.seek(((bucket * 32) + currSlot) * 16 + 8);
                file.write(hashHandle[1].getMemLoc());
                file.seek(((bucket * 32) + currSlot) * 16 + 12);
                file.write(hashHandle[1].getMemLength());
                return 1;
            }
            // Find first tombstone in sequence
            else if (tableArray[(bucket * 32) + currSlot][0].equals(
                tombStone) && !tombFound) {
                lowestTombstone = (bucket * 32) + currSlot;
                tombFound = true;
            }
            // The given handles already exist in the table
            byte[] fromFile = new byte[(int)Math.ceil(tableArray[(bucket * 32)
                + currSlot][0].getMemLength() / 4.0)];
            // Read from file location for comparison
            seqFile.seek(tableArray[(bucket * 32) + currSlot][0].getMemLoc());
            seqFile.read(fromFile, 0, (int)Math.ceil(tableArray[(bucket * 32)
                + currSlot][0].getMemLength() / 4.0));
            // Convert to a sequence to compare
            Sequence temp = new Sequence();
            String fullBytes = temp.bytesToString(fromFile);
            // We must then shorten the string to the proper length
            String fileID = fullBytes.substring(0, tableArray[(bucket * 32)
                + currSlot][0].getMemLength());

            if (fileID.equals(seqID)) {
                return 0;
            }

            // Move to next slot
            currSlot = (currSlot + 1) % 32;
        }
        while ((bucket * 32) + currSlot != hashSlot);

        // If a tombstone has an open spot, we can fill it here
        if (tombFound) {
            MemHandle[] hashHandle = new MemHandle[2];
            System.arraycopy(handles, 0, hashHandle, 0, 2);
            tableArray[lowestTombstone] = hashHandle;
            currSize++;
            // Store values in hash file
            file.seek(((bucket * 32) + currSlot) * 16);
            file.write(hashHandle[0].getMemLoc());
            file.seek(((bucket * 32) + currSlot) * 16 + 4);
            file.write(hashHandle[0].getMemLength());
            file.seek(((bucket * 32) + currSlot) * 16 + 8);
            file.write(hashHandle[1].getMemLoc());
            file.seek(((bucket * 32) + currSlot) * 16 + 12);
            file.write(hashHandle[1].getMemLength());
            return 1;
        }
        // If we make it here, then the bucket is full, reject insert
        return -1;
    }


    @Override
    public MemHandle[] remove(String seqID, RandomAccessFile seqFile)
        throws IOException {
        long hashSlot = hash(seqID, tableSize);
        int bucket = (int)hashSlot / 32;
        int currSlot = (int)hashSlot % 32;

        // Try first slot to check for availability
        // We only need to check the first value since the values are related
        if (tableArray[(bucket * 32) + currSlot][0] == null) {
            return tableArray[(bucket * 32) + currSlot];
        }
        do {
            // We want to skip over tombstones
            if (tableArray[(bucket * 32) + currSlot][0].equals(new MemHandle(-1,
                -1))) {

                currSlot = (currSlot + 1) % 32;
                continue;
            }
            MemHandle idHandle = tableArray[(bucket * 32) + currSlot][0];
            int offset = idHandle.getMemLoc();
            int length = idHandle.getMemLength();
            byte[] fromFile = new byte[(int)Math.ceil(idHandle.getMemLength()
                / 4.0)];
            // Read from file location for comparison
            seqFile.seek(offset);
            seqFile.read(fromFile, 0, (int)Math.ceil(idHandle.getMemLength()
                / 4.0));
            // Convert to a sequence to compare
            Sequence temp = new Sequence();
            String fullBytes = temp.bytesToString(fromFile);
            // We must then shorten the string to the proper length
            String fileID = fullBytes.substring(0, length);
            if (fileID.equals(seqID)) {
                currSize--;
                // For a tombstone, we use values of -1, impossible length and
                // offset
                MemHandle[] tempHandle = new MemHandle[2];
                System.arraycopy(tableArray[(bucket * 32) + currSlot], 0,
                    tempHandle, 0, 2);
                tableArray[(bucket * 32) + currSlot][0] = new MemHandle(-1, -1);
                tableArray[(bucket * 32) + currSlot][1] = new MemHandle(-1, -1);
                // Set values to -1 to represent a tombstone
                file.seek(((bucket * 32) + currSlot) * 16);
                file.write(-1);
                file.seek(((bucket * 32) + currSlot) * 16 + 4);
                file.write(-1);
                file.seek(((bucket * 32) + currSlot) * 16 + 8);
                file.write(-1);
                file.seek(((bucket * 32) + currSlot) * 16 + 12);
                file.write(-1);
                return tempHandle;
            }
            // Move to next slot
            currSlot = (currSlot + 1) % 32;
        }
        while ((bucket * 32) + currSlot != hashSlot);
        // The sequence id was not found in any slot
        return new MemHandle[2];
    }


    @Override
    public String search(String seqID, RandomAccessFile seqFile)
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
            if (offset == -1 || length == -1) {
                continue;
            }
            // Read from file location for comparison
            seqFile.seek(offset);
            seqFile.read(fromFile, 0, (int)Math.ceil(idHandle.getMemLength()
                / 4.0));
            // Convert to a sequence to compare
            Sequence temp = new Sequence();
            String fullBytes = temp.bytesToString(fromFile);
            // We must then shorten the string to the proper length
            String fileID = fullBytes.substring(0, length);
            if (fileID.equals(seqID)) {
                MemHandle seqHandle = tableArray[(bucket * 32) + currSlot][1];
                int seqOffset = seqHandle.getMemLoc();
                int seqLength = seqHandle.getMemLength();
                byte[] seqFromFile = new byte[(int)Math.ceil(seqHandle
                    .getMemLength() / 4.0)];
                // Read from file location for comparison
                seqFile.seek(seqOffset);
                seqFile.read(seqFromFile, 0, (int)Math.ceil(seqHandle
                    .getMemLength() / 4.0));
                // Convert to a sequence to compare
                Sequence tempSeq = new Sequence();
                String seqFullBytes = tempSeq.bytesToString(seqFromFile);
                // We must then shorten the string to the proper length
                String finalSeq = seqFullBytes.substring(0, seqLength);
                return finalSeq;
            }
            // Move to next slot
            currSlot = (currSlot + 1) % 32;
        }
        while ((bucket * 32) + currSlot != hashSlot);
        return "";
    }


    @Override
    public MemHandle[] getHandles(int location) {
        return tableArray[location];
    }


    /**
     * Checks to see if the sequence ID is already in the hash table
     * 
     * @param seqID
     *            id to look for
     * @param hashFile
     *            hash file to look through
     * @return if seqID is in the table.
     */
    public boolean checkForDuplicate(String seqID, RandomAccessFile hashFile) {
        long hashSlot = hash(seqID, tableSize);
        int bucket = (int)hashSlot / 32;
        int currSlot = (int)hashSlot % 32;

        // look at first slot
        if (tableArray[(bucket * 32) + currSlot][0] == null) {
            return true;
        }
        do {
            try {
                MemHandle idHandle = tableArray[(bucket * 32) + currSlot][0];
                int offset = idHandle.getMemLoc();
                int length = idHandle.getMemLength();
                byte[] fromFile = new byte[(int)Math.ceil(idHandle
                    .getMemLength() / 4.0)];
                if (offset == -1 || length == -1) {
                    continue;
                }
                // Read from file location for comparison
                hashFile.seek(offset);
                hashFile.read(fromFile, 0, (int)Math.ceil(idHandle
                    .getMemLength() / 4.0));
                // Convert to a sequence to compare
                Sequence temp = new Sequence();
                String fullBytes = temp.bytesToString(fromFile);
                // We must then shorten the string to the proper length
                String fileID = fullBytes.substring(0, length);
                if (fileID.equals(seqID)) {
                    return false;
                }
            }
            catch (IOException e) {
                System.out.println("issue with file access");
            }
            // Move to next slot
            currSlot = (currSlot + 1) % 32;
        }
        while ((bucket * 32) + currSlot != hashSlot);
        return false;
    }


    /**
     * get all handles at given locations
     * 
     * @param tableLocs
     *            locations to get handles from
     * @return handles at given locations
     */
    // Get table handles for printing
    public MemHandle[] getAllHandles(int[] tableLocs) {
        MemHandle[] handleTable = new MemHandle[currSize];
        int tableLoc = 0;
        for (int i = 0; i < tableSize; i++) {
            if (tableArray[i][0] == null || tableArray[i][0]
                .getMemLoc() == -1) {
                continue;
            }
            handleTable[tableLoc] = tableArray[i][0];
            tableLocs[tableLoc] = i;
            tableLoc++;
        }

        return handleTable;
    }


    /**
     * get size of the table
     * 
     * @return size of table
     */
    public int getSize() {
        return this.currSize;
    }


    /**
     * test method for insert and remove
     * 
     * @param location
     *            location to insert into
     * @param hans
     *            handle to insert
     */
    // A test method for insert/remove
    public void testSetter(int location, MemHandle[] hans) {
        tableArray[location] = hans;
    }

}
