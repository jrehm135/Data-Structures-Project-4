/**
 * 
 */

/**
 * @author Josh
 *
 */
public class memoryMan {
    private doublyLinkedList<freeBlock> freeBlocks;
    private int memSize;
    
    memoryMan(int memSize){
        this.memSize = memSize;
        
        freeBlocks = new doublyLinkedList<freeBlock>();
        freeBlocks.insert(new freeBlock(0, memSize));
    }
    
    
}
