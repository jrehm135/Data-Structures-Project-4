/**
 * 
 * @author Josh Rehm
 * @author Quinton Miller
 * 
 * @version 5/8/2019
 */

public class FreeBlock {
    private int pos;
    private int length;


    /**
     * make a new free block
     * 
     * @param pos
     *            position of new block
     * @param length
     *            length of new block
     */
    FreeBlock(int pos, int length) {
        this.pos = pos;
        this.length = length;
    }


    /**
     * get pos of block
     * 
     * @return pos of block
     */
    public int getPos() {
        return this.pos;
    }


    /**
     * get length of block
     * 
     * @return length of block
     */
    public int getLength() {
        return this.length;
    }


    /**
     * set the block values
     * 
     * @param newPos
     *            new block pos
     * @param newLength
     *            new block length
     */
    public void setBlock(int newPos, int newLength) {
        this.pos = newPos;
        this.length = newLength;
    }


    /**
     * checks if two blocks are adjacent
     * 
     * @param otherBlock
     *            other block to check
     * @return if the blocks are adjacent or not
     */
    public boolean isAdjacent(FreeBlock otherBlock) {
        int otherPos = otherBlock.getPos();
        if (this.pos < otherPos) {
            return (this.pos + this.length) == otherPos;
        }
        else {
            return (otherPos + otherBlock.getLength()) == this.pos;
        }
    }
}
