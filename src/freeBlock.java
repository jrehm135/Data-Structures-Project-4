/**
 * 
 */

/**
 * @author Josh
 *
 */
public class freeBlock{
    private int pos;
    private int length;

    freeBlock(int pos, int length){
        this.pos = pos;
        this.length = length;
    }
    
    public int getPos() {
        return this.pos;
    }
    
    public int getLength() {
        return this.length;
    }
    
    public boolean isAdjacent(freeBlock otherBlock) {
        int otherPos = otherBlock.getPos();
        if(this.pos < otherPos) {
            return (this.pos + this.length) == otherPos;
        }
        else {
            return (otherPos + otherBlock.getLength()) == this.pos;
        }
    }
}
