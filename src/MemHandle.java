/**
 * 
 */

/**
 * @author Josh
 *
 */
public class memHandle {
    private int memLoc;
    private int memLength;
    
    
    memHandle(int loc, int length){
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
