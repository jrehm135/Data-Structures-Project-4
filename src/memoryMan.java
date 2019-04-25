import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author Josh
 * @author Quinton Miller
 *
 *         This is the memory manager that handles inserting sequences and
 *         sequence IDs into the memory file.
 */
public class memoryMan {
    private doublyLinkedList<freeBlock> freeBlocks;
    private int currMemSize;
    private int initMemSize;
    private RandomAccessFile biofile;


    /**
     * This is the manager that will insert sequences and IDs into the memory
     * file.
     * 
     * @param memSize
     *            Initial size of the memory.
     * @param memoryFile
     *            File to store sequences and ID's in.
     * @throws IOException
     *             Throws when the emoryFile can't be found.
     */
    memoryMan(int memSize, String memoryFile) throws IOException {
        currMemSize = memSize;
        biofile = new RandomAccessFile(memoryFile, "rw");
        freeBlocks = new doublyLinkedList<freeBlock>();
        initMemSize = memSize;
        // Only allocate if greater than 0
        if (memSize > 0) {
            freeBlocks.insert(new freeBlock(0, memSize));
        }
    }


    /**
     * 
     * @param inputSequenceID
     *            sequence ID to be inserted.
     * @param inputSequence
     *            DNA sequence to be inserted.
     * @return array of memory locations, the 0 index is the sequence ID
     *         and the 1 index is the sequence. Returns an empty array if there
     *         was in IOException
     * @throws IOException
     */
    public memHandle[] insert(String inputSequenceID, String inputSequence) {
        memHandle insertLocations[] = new memHandle[2];
        try {
            insertLocations[0] = insertID(inputSequenceID);
            insertLocations[1] = insertSequence(inputSequence);
            return insertLocations;
        }
        catch (Exception IOException) {
            System.out.println(
                "Trying to insert into an invalid memory location.");
            return insertLocations;
        }
    }


    /**
     * Insert a sequence ID into the memory file.
     * 
     * @param inputID
     *            Sequence ID to be inserted.
     * @return Memory handle of the sequence ID.
     * @throws IOException
     *             Throws when trying to write to invalid memory location.
     */
    public memHandle insertID(String inputID) throws IOException {
        sequence newID = new sequence(inputID);
        int seqLength = (int)Math.ceil(newID.getLength() / 4.0);
        int insertLoc = getNextMemPosition(seqLength);
        if (insertLoc + seqLength > currMemSize) {
            growMemSize(initMemSize);
        }
        biofile.seek(insertLoc);
        biofile.write(newID.getBytes());
        return new memHandle(insertLoc, seqLength);
    }


    /**
     * Insert a sequence into the memory file.
     * 
     * @param inputSequence
     *            Sequence to be inserted.
     * @return Memory handle of sequence.
     * @throws IOException
     *             Throws when trying to write to invalid memory location.
     */
    public memHandle insertSequence(String inputSequence) throws IOException {
        sequence newInsert = new sequence(inputSequence);
        int seqLength = (int)Math.ceil(newInsert.getLength() / 4.0);
        int insertLoc = getNextMemPosition(seqLength);
        if (insertLoc + seqLength > currMemSize) {
            growMemSize(initMemSize);
        }
        biofile.seek(insertLoc);
        biofile.write(newInsert.getBytes());
        return new memHandle(insertLoc, seqLength);
    }


    /**
     * Removes something from the memory file given by the input handle
     * 
     * @param handle
     *            handle of the thing to be removed.
     */
    public void remove(memHandle handle) {
        int offset = handle.getMemLoc();
        int length = handle.getMemLength();

        // Don't actually need to zero out sequence, just
        // add a free block where it was
        freeBlock cur = freeBlocks.getElement();
        if (cur == null) {
            freeBlocks.insert(new freeBlock(offset, length));
            return;
        }
        while (freeBlocks.hasNext()) {
            if (cur.getPos() < offset) {
                freeBlocks.next();
            }
            else {
                freeBlocks.insert(new freeBlock(offset, length));
                mergeBlocks();
                return;
            }
        }
        // At this point, we have made it to the end of the list,
        // so we add a freeblock to the end
        freeBlocks.insert(new freeBlock(offset, length));
    }


    /**
     * Iterate through the free blocks list and look for any blocks that need to
     * be merged together and merge them if needed.
     */
    public void mergeBlocks() {
        freeBlocks.moveToHead();
        freeBlocks.next();
        while (freeBlocks.hasNext()) {
            int firstLength = freeBlocks.getElement().getLength();
            int firstPos = freeBlocks.getElement().getPos();
            freeBlocks.next();
            int nextStart = freeBlocks.getElement().getPos();
            int nextLength = freeBlocks.getElement().getLength();

            if (firstPos + firstLength == nextStart) {
                freeBlocks.getElement().setBlock(firstPos, nextLength
                    + firstLength);
                freeBlocks.previous();
                freeBlocks.remove();
            }
            freeBlocks.next();
        }
    }


    /**
     * Get the location of the next free block that we can insert into for the
     * given length.
     * 
     * @param lengthNeeded
     *            Amount of memory needed to insert into the file.
     * @return the position where the thing can be inserted.
     */
    private int getNextMemPosition(int lengthNeeded) {
        freeBlock cur = freeBlocks.getElement();
        // Make sure to return the memsize if the value is null
        if (cur == null) {
            return currMemSize;
        }
        while (freeBlocks.hasNext()) {
            if (cur.getLength() > lengthNeeded) {
                // Add to position to reflect taken memory
                int newPos = cur.getPos() + lengthNeeded;
                // Subtract from size to remove free space
                int newBlockSize = cur.getLength() - lengthNeeded;
                cur.setBlock(newPos, newBlockSize);
                break;
            }
            else if (cur.getLength() == lengthNeeded) {
                // If the blocks are equal, simply remove
                freeBlocks.remove();
                break;
            }
            freeBlocks.next();
            cur = freeBlocks.getElement();
        }
        return cur.getPos();
    }


    /**
     * Expand the size of the memory
     * 
     * @param length
     *            How much we want the memory size to expand by.
     */
    private void growMemSize(int length) {
        // We aren't making new free blocks, just allocate at memory size
        // freeBlocks.insert(new freeBlock(currMemSize, length));

        currMemSize += length;
        System.out.println("Memory size expanded to " + currMemSize + " size.");
    }

}
