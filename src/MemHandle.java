/**
 * 
 */

/**
 * @author Josh
 *
 */
public class MemHandle {
    private int memLoc;
    private int memLength;
    
    
    MemHandle(int loc, int length){
        this.memLoc = loc;
        this.memLength = length;
    }

    public int getMemLoc() {
        return memLoc;
    }
    
    public int getMemLength() {
        return memLength;
    }
    
}
