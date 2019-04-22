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
        freeBlocks.insert(new freeBlock(0, memSize));
    }


    memHandle insert(String inputSequence) throws IOException{
        sequence newInsert = new sequence(inputSequence);
        int insertLoc = getNextMemPosition(newInsert.getLength());
        if(insertLoc + newInsert.getLength() > currMemSize)
        {
            growMemSize();
        }
        biofile.seek(insertLoc);
        biofile.write(newInsert.getBytes());
        return new memHandle(insertLoc);
    }

    int getNextMemPosition(int lengthNeeded) {
        freeBlock cur = freeBlocks.getElement();
        while (freeBlocks.hasNext()) {
            if (cur.getLength() >= lengthNeeded) {
                break;
            }
        }
        return cur.getPos();
    }
    
    void growMemSize()
    {
        freeBlocks.moveToHead();
        freeBlocks.insert(new freeBlock(currMemSize,initMemSize));
        //mergeBlocks();
        currMemSize += initMemSize;
        System.out.println("Memory size expanded to " + currMemSize + " size.");
    }

}
