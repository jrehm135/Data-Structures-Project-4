/**
 * 
 */

/**
 * @author Josh
 *
 */
public class FreeBlock{
    private int pos;
    private int length;

    FreeBlock(int pos, int length){
        this.pos = pos;
        this.length = length;
    }
    
    public int getPos() {
        return this.pos;
    }
    
    public int getLength() {
        return this.length;
    }
    
    public void setBlock(int pos, int length) {
        this.pos = pos;
        this.length = length;
    }
    
    public boolean isAdjacent(FreeBlock otherBlock) {
        int otherPos = otherBlock.getPos();
        if(this.pos < otherPos) {
            return (this.pos + this.length) == otherPos;
        }
        else {
            return (otherPos + otherBlock.getLength()) == this.pos;
        }
    }
}
