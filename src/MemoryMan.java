import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author Josh Rehm
 * @author Quinton Miller
 *
 *         This is the memory manager that handles inserting sequences and
 *         sequence IDs into the memory file.
 * @version 5/8/2019
 */
public class MemoryMan {
    private DoublyLinkedList<FreeBlock> freeBlocks;
    private int currMemSize;
    private RandomAccessFile bioFile;


    /**
     * This is the manager that will insert sequences and IDs into the memory
     * file.
     * 
     * @param memoryFile
     *            File to store sequences and ID's in.
     * @throws IOException
     *             Throws when the emoryFile can't be found.
     */
    MemoryMan(String memoryFile) {
        try {
            currMemSize = 0;
            freeBlocks = new DoublyLinkedList<FreeBlock>();
            bioFile = new RandomAccessFile(memoryFile, "rw");
            // Only allocate if greater than 0
        }
        catch (IOException e) {
            System.out.println("Error in memory manager constructor: " + e
                .toString());
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
    public MemHandle[] insert(String inputSequenceID, String inputSequence, int inputLength) {
        MemHandle[] insertLocations = new MemHandle[2];
        try {
            insertLocations[0] = insertID(inputSequenceID);
            insertLocations[1] = insertSequence(inputSequence, inputLength);
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
            growMemSize(seqLength);
        }
        bioFile.seek(insertLoc);
        bioFile.write(newID.getBytes());
        updateBlockList(insertLoc, seqLength);
        return new MemHandle(insertLoc, newID.getLength());
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
    public MemHandle insertSequence(String inputSequence, int inputLength) throws IOException {
        Sequence newInsert = new Sequence(inputSequence);
        int seqLength = (int)Math.ceil(inputLength / 4.0);
        int insertLoc = getNextMemPosition(seqLength);
        if (insertLoc + seqLength > currMemSize) {
            growMemSize(seqLength);
        }
        bioFile.seek(insertLoc);
        bioFile.write(newInsert.getBytes());
        updateBlockList(insertLoc, seqLength);
        return new MemHandle(insertLoc, inputLength);
    }


    /**
     * Removes something from the memory file given by the input handles
     * 
     * @param handles
     *            handles of the things to be removed.
     */
    public void remove(MemHandle[] handles) {
        if (handles[0] == null) {
            return;
        }
        // Type of handle doesn't matter, we just grab each and free memory
        for (MemHandle handle : handles) {
            int offset = handle.getMemLoc();
            int length = (int)Math.ceil(handle.getMemLength() / 4.0);

            freeBlocks.moveToHead();
            freeBlocks.next();
            // Don't actually need to zero out sequence, just
            // add a free block where it was
            FreeBlock cur = freeBlocks.getElement();
            if (cur == null) {
                freeBlocks.insert(new FreeBlock(offset, length));
                continue;
            }
            boolean alreadyInserted = false;
            while (freeBlocks.hasNext()) {
                if ((cur.getPos() + cur.getLength()) > offset) {
                    freeBlocks.insertBefore(new FreeBlock(offset, length));
                    alreadyInserted = true;
                    break;
                }
                else {
                    // we find something that needs to get merged
                    freeBlocks.next();
                    cur = freeBlocks.getElement();

                }
            }
            if (alreadyInserted) {
                mergeBlocks();
                continue;
            }
            // Before we finish, we need to check against the last value
            if (cur.getPos() + cur.getLength() > offset) {

                // We want to insert before the last value
                freeBlocks.insertBefore(new FreeBlock(offset, length));
                mergeBlocks();
            }
            else {

                // At this point, we have made it to the end of the list,
                // so we add a FreeBlock to the end
                freeBlocks.insert(new FreeBlock(offset, length));
                mergeBlocks();
            }
            freeBlocks.moveToTail();
            freeBlocks.previous();
            cur = freeBlocks.getElement();
            while (cur != null) {
                if (cur.getLength() + cur.getPos() == getCurMemSize()) {
                    freeBlocks.remove();
                    shrinkMemSize(cur.getLength());
                    freeBlocks.previous();
                    cur = freeBlocks.getElement();
                }
                else {
                    break;
                }
            }
        }
    }


    /**
     * searchs through the memory file for the given sequence
     * 
     * @param handle
     *            the places to look for the sequence
     * @param sequence
     *            sequences you are looking for
     * @return array of strings that the manager has found, empty if nothing is
     *         found
     * @throws IOException
     *             throws if there is file access error
     */
    public String[] search(MemHandle[] handle, String[] sequence)
        throws IOException {
        Sequence sequenceIDToFind = new Sequence(sequence[0]);
        int idLength = (int)Math.ceil(handle[0].getMemLength() / 4.0);
        int seqLength = (int)Math.ceil(handle[1].getMemLength() / 4.0);
        byte[] idByteFromFile = new byte[idLength];
        bioFile.seek(handle[0].getMemLoc());
        bioFile.read(idByteFromFile, 0, idLength);
        if (idByteFromFile.equals(sequenceIDToFind.getBytes())) {
            Sequence sequenceToFind = new Sequence(sequence[1]);
            byte[] seqFromFile = new byte[seqLength];
            bioFile.seek(handle[1].getMemLoc());
            bioFile.read(seqFromFile, 0, seqLength);
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
     * searches through the memory file to find things
     * 
     * @param handles
     *            where to look for the sequences
     * @return array of strings that from the handle inputs
     * @throws IOException
     *             throws when there is a file read error
     */
    public String[] print(MemHandle[] handles) throws IOException {
        String[] outputIDs = new String[handles.length];
        int i = 0;
        for (MemHandle m : handles) {
            byte[] seqFromFile = new byte[(int)Math.ceil(m.getMemLength()
                / 4.0)];
            bioFile.seek(m.getMemLoc());
            bioFile.read(seqFromFile, 0, (int)Math.ceil(m.getMemLength()
                / 4.0));
            Sequence fromFile = new Sequence();
            String stringFromFile = fromFile.bytesToString(seqFromFile);
            String finalString = stringFromFile.substring(0, m.getMemLength());
            outputIDs[i] = finalString;
            i++;
        }
        return outputIDs;
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
        boolean alreadyRemoved = false;
        // Make sure to return the memsize if the value is null
        if (cur == null) {
            return currMemSize;
        }
        while (freeBlocks.hasNext()) {
            if (cur.getLength() > lengthNeeded) {
                break;
            }
            else if (cur.getLength() == lengthNeeded) {
                // If the blocks are equal, simply remove
                freeBlocks.remove();
                alreadyRemoved = true;
                break;
            }
            freeBlocks.next();
            cur = freeBlocks.getElement();
        }
        if (cur.getLength() < lengthNeeded) {
            return currMemSize;
        }
        else if (cur.getLength() == lengthNeeded && !alreadyRemoved) {
            // If the blocks are equal, simply remove
            freeBlocks.remove();
        }
        return cur.getPos();
    }


    /**
     * update the free block list with a block with the location and length
     * 
     * @param location
     *            location of new block
     * @param length
     *            length of new block
     */
    private void updateBlockList(int location, int length) {
        if (freeBlocks.getLength() == 0) {
            return;
        }
        freeBlocks.moveToHead();
        do {
            freeBlocks.next();
            FreeBlock cur = freeBlocks.getElement();
            if (cur.getPos() == location) {
                if (cur.getLength() == length) {
                    freeBlocks.remove();
                }
                else {
                    freeBlocks.setElement(new FreeBlock(cur.getPos() + length,
                        cur.getLength() - length));
                }
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
    }


    /**
     * shrink the size of the memory
     * 
     * @param length
     *            amount to shrink by
     */
    private void shrinkMemSize(int length) {

        currMemSize -= length;
    }


    /**
     * getter for the current memory size
     * 
     * @return the current memory size;
     */
    public int getCurMemSize() {
        return currMemSize;
    }


    /**
     * returns the current free block list
     * 
     * @return current free block list
     */
    public DoublyLinkedList<FreeBlock> getFreeBlocksList() {
        return freeBlocks;
    }
}
