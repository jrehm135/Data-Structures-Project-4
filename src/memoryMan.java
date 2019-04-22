import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author Josh
 *
 */
public class memoryMan {
    private doublyLinkedList<freeBlock> freeBlocks;
    private int currMemSize;
    private int initMemSize;
    private RandomAccessFile biofile;


    memoryMan(int memSize, String memoryFile) throws IOException {
        currMemSize = memSize;
        biofile = new RandomAccessFile(memoryFile,"rw");
        freeBlocks = new doublyLinkedList<freeBlock>();
        //Only allocate if greater than 0
        if(memSize > 0) {
            freeBlocks.insert(new freeBlock(0, memSize));
        }
    }


    public memHandle insert(String inputSequence) throws IOException{
        sequence newInsert = new sequence(inputSequence);
        int seqLength = (int) Math.ceil(newInsert.getLength()/4.0) ;
        int insertLoc = getNextMemPosition(seqLength);
        if(insertLoc + seqLength > currMemSize)
        {
            growMemSize(seqLength);
        }
        biofile.seek(insertLoc);
        biofile.write(newInsert.getBytes());
        return new memHandle(insertLoc, seqLength);
    }

    private int getNextMemPosition(int lengthNeeded) {
        freeBlock cur = freeBlocks.getElement();
        //Make sure to return the memsize if the value is null
        if(cur == null) {
            return currMemSize;
        }
        while (freeBlocks.hasNext()) {
            if (cur.getLength() > lengthNeeded) {
                //Add to position to reflect taken memory
                int newPos = cur.getPos() + lengthNeeded;
                //Subtract from size to remove free space
                int newBlockSize = cur.getLength() - lengthNeeded;
                cur.setBlock(newPos, newBlockSize);
                break;
            }
            else if(cur.getLength() == lengthNeeded) {
                //If the blocks are equal, simply remove
                freeBlocks.remove();
                break;
            }
            freeBlocks.next();
            cur = freeBlocks.getElement();
        }
        return cur.getPos();
    }
    
    private void growMemSize(int length)
    {        
        //We aren't making new free blocks, just allocate at memory size
        //freeBlocks.insert(new freeBlock(currMemSize, length));
        
        currMemSize += length;
        System.out.println("Memory size expanded to " + currMemSize + " size.");
    }

}
