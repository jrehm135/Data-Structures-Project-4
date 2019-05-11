
/**
 * 
 * @author Josh Rehm
 * @author Quinton Miller
 * @version 5/10/2019
 */
public class MemHandle {
    private int memLoc;
    private int memLength;


    /**
     * construct a mem handle
     * 
     * @param loc
     *            location
     * @param length
     *            length
     */
    MemHandle(int loc, int length) {
        this.memLoc = loc;
        this.memLength = length;
    }


    /**
     * get the memory location
     * 
     * @return memory location
     */
    public int getMemLoc() {
        return memLoc;
    }


    /**
     * get the memory length
     * 
     * @return memory length
     */
    public int getMemLength() {
        return memLength;
    }

}
