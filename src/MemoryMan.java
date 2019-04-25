import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author Josh
 * @author Quinton Miller
 *
 *         This is the memory manager that handles inserting sequences and
 *         sequence IDs into the memory file.
 */
public class MemoryMan {
    private DoublyLinkedList<FreeBlock> FreeBlocks;
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
    MemoryMan(int memSize, String memoryFile) throws IOException {
        currMemSize = memSize;
        biofile = new RandomAccessFile(memoryFile, "rw");
        FreeBlocks = new DoublyLinkedList<FreeBlock>();
        initMemSize = memSize;
        // Only allocate if greater than 0
        if (memSize > 0) {
            FreeBlocks.insert(new FreeBlock(0, memSize));
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
    public MemHandle[] insert(String inputSequenceID, String inputSequence) {
        MemHandle insertLocations[] = new MemHandle[2];
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
    public MemHandle insertID(String inputID) throws IOException {
        Sequence newID = new Sequence(inputID);
        int seqLength = (int)Math.ceil(newID.getLength() / 4.0);
        int insertLoc = getNextMemPosition(seqLength);
        if (insertLoc + seqLength > currMemSize) {
            growMemSize(initMemSize);
        }
        biofile.seek(insertLoc);
        biofile.write(newID.getBytes());
        return new MemHandle(insertLoc, seqLength);
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
    public MemHandle insertSequence(String inputSequence) throws IOException {
        Sequence newInsert = new Sequence(inputSequence);
        int seqLength = (int)Math.ceil(newInsert.getLength() / 4.0);
        int insertLoc = getNextMemPosition(seqLength);
        if (insertLoc + seqLength > currMemSize) {
            growMemSize(initMemSize);
        }
        biofile.seek(insertLoc);
        biofile.write(newInsert.getBytes());
        return new MemHandle(insertLoc, seqLength);
    }


    /**
     * Removes something from the memory file given by the input handle
     * 
     * @param handle
     *            handle of the thing to be removed.
     */
    public void remove(MemHandle handle) {
        int offset = handle.getMemLoc();
        int length = handle.getMemLength();

        // Don't actually need to zero out sequence, just
        // add a free block where it was
        FreeBlock cur = FreeBlocks.getElement();
        if (cur == null) {
            FreeBlocks.insert(new FreeBlock(offset, length));
            return;
        }
        while (FreeBlocks.hasNext()) {
            if (cur.getPos() < offset) {
                FreeBlocks.next();
            }
            else {
                FreeBlocks.insert(new FreeBlock(offset, length));
                mergeBlocks();
                return;
            }
        }
        // At this point, we have made it to the end of the list,
        // so we add a FreeBlock to the end
        FreeBlocks.insert(new FreeBlock(offset, length));
    }


    /**
     * Iterate through the free blocks list and look for any blocks that need to
     * be merged together and merge them if needed.
     */
    public void mergeBlocks() {
        FreeBlocks.moveToHead();
        FreeBlocks.next();
        while (FreeBlocks.hasNext()) {
            int firstLength = FreeBlocks.getElement().getLength();
            int firstPos = FreeBlocks.getElement().getPos();
            FreeBlocks.next();
            int nextStart = FreeBlocks.getElement().getPos();
            int nextLength = FreeBlocks.getElement().getLength();

            if (firstPos + firstLength == nextStart) {
                FreeBlocks.getElement().setBlock(firstPos, nextLength
                    + firstLength);
                FreeBlocks.previous();
                FreeBlocks.remove();
            }
            FreeBlocks.next();
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
        FreeBlock cur = FreeBlocks.getElement();
        // Make sure to return the memsize if the value is null
        if (cur == null) {
            return currMemSize;
        }
        while (FreeBlocks.hasNext()) {
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
                FreeBlocks.remove();
                break;
            }
            FreeBlocks.next();
            cur = FreeBlocks.getElement();
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
        // FreeBlocks.insert(new FreeBlock(currMemSize, length));

        currMemSize += length;
        System.out.println("Memory size expanded to " + currMemSize + " size.");
    }


    public int getCurMemSize() {
        return currMemSize;
    }
}
