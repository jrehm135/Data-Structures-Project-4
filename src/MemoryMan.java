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
    private DoublyLinkedList<FreeBlock> freeBlocks;
    private int currMemSize;
    private int initMemSize;
    private RandomAccessFile bioFile;


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
        bioFile = new RandomAccessFile(memoryFile, "rw");
        freeBlocks = new DoublyLinkedList<FreeBlock>();
        initMemSize = memSize;
        // Only allocate if greater than 0
        if (memSize > 0) {
            freeBlocks.insert(new FreeBlock(0, memSize));
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
        bioFile.seek(insertLoc);
        bioFile.write(newID.getBytes());
        updateBlockList(insertLoc, seqLength);
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
        bioFile.seek(insertLoc);
        bioFile.write(newInsert.getBytes());
        updateBlockList(insertLoc, seqLength);
        return new MemHandle(insertLoc, seqLength);
    }


    /**
     * Removes something from the memory file given by the input handle
     * 
     * @param handle
     *            handle of the thing to be removed.
     */
    public void remove(MemHandle[] handles) {
        // Type of handle doesn't matter, we just grab each and free memory
        for (MemHandle handle : handles) {
            int offset = handle.getMemLoc();
            int length = handle.getMemLength();

            // Don't actually need to zero out sequence, just
            // add a free block where it was
            FreeBlock cur = freeBlocks.getElement();
            if (cur == null) {
                freeBlocks.insert(new FreeBlock(offset, length));
            }
            while (freeBlocks.hasNext()) {
                if (cur.getPos() < offset) {
                    freeBlocks.next();
                }
                else {
                    freeBlocks.insert(new FreeBlock(offset, length));
                    mergeBlocks();
                    break;
                }
            }
            // Before we finish, we need to check against the last value
            if (cur.getPos() > offset) {
                // We want to insert before the last value
                freeBlocks.insertBefore(new FreeBlock(offset, length));
                mergeBlocks();
            }
            else {
                // At this point, we have made it to the end of the list,
                // so we add a FreeBlock to the end
                freeBlocks.insert(new FreeBlock(offset, length));
            }
        }
    }


    public String[] search(MemHandle handle[], String sequence[])
        throws IOException {
        Sequence sequenceIDToFind = new Sequence(sequence[0]);
        byte idByteFromFile[] = new byte[handle[0].getMemLength()];
        bioFile.seek(handle[0].getMemLoc());
        bioFile.read(idByteFromFile, 0, handle[0].getMemLength());
        if (idByteFromFile.equals(sequenceIDToFind.getBytes())) {
            Sequence sequenceToFind = new Sequence(sequence[1]);
            byte seqFromFile[] = new byte[handle[1].getMemLength()];
            bioFile.seek(handle[1].getMemLoc());
            bioFile.read(seqFromFile, 0, handle[1].getMemLength());
            if (seqFromFile.equals(sequenceToFind.getBytes())) {
                String[] output = new String[2];
                output[0] = sequenceIDToFind.toString();
                output[1] = sequenceToFind.toString();
                return output;
            }
            else {
                return new String[0];
            }
        }
        else {
            return new String[0];
        }
    }


    /**
     * Iterate through the free blocks list and look for any blocks that need to
     * be merged together and merge them if needed.
     */
    private void mergeBlocks() {
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
            else {
                freeBlocks.next();
            }
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
        freeBlocks.moveToHead();
        freeBlocks.next();
        FreeBlock cur = freeBlocks.getElement();
        // Make sure to return the memsize if the value is null
        if (cur == null) {
            return currMemSize;
        }
        while (freeBlocks.hasNext()) {
            //
            if (cur.getLength() > lengthNeeded) {
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


    private void updateBlockList(int location, int length) {
        freeBlocks.moveToHead();
        do {
            freeBlocks.next();
            FreeBlock cur = freeBlocks.getElement();
            if (cur.getPos() == location) {
                freeBlocks.setElement(new FreeBlock(cur.getPos() + length, cur
                    .getLength() - length));
                return;
            }
        }
        while (freeBlocks.hasNext());
    }


    /**
     * Expand the size of the memory
     * 
     * @param length
     *            How much we want the memory size to expand by.
     */
    private void growMemSize(int length) {
        // We aren't making new free blocks, just allocate at memory size
        // freeBlocks.insert(new FreeBlock(currMemSize, length));

        currMemSize += length;
        System.out.println("Memory size expanded to " + currMemSize + " size.");
    }


    public int getCurMemSize() {
        return currMemSize;
    }


    public DoublyLinkedList<FreeBlock> getfreeBlocksList() {
        return freeBlocks;
    }
}
