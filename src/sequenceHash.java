import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 * 
 */

/**
 * @author Josh
 *
 */
public class sequenceHash<T extends memHandle> implements hashTable<memHandle> {
    private memHandle[] tableArray;
    private int tableSize;
    private RandomAccessFile file;
    
    private final int BUCKETSIZE = 32;
    
    sequenceHash(int tableSize) throws FileNotFoundException{
        tableArray = new memHandle[tableSize];
        this.tableSize = tableSize;
        
        file = new RandomAccessFile("hashFile.bin", "rw");
    }
    
    //This implements the 
    @Override
    public long hash(String s, int M) {
        int intLength = s.length() / 4;
        long sum = 0;
        for (int j = 0; j < intLength; j++) {
          char c[] = s.substring(j * 4, (j * 4) + 4).toCharArray();
          long mult = 1;
          for (int k = 0; k < c.length; k++) {
            sum += c[k] * mult;
            mult *= 256;
          }
        }

        char c[] = s.substring(intLength * 4).toCharArray();
        long mult = 1;
        for (int k = 0; k < c.length; k++) {
          sum += c[k] * mult;
          mult *= 256;
        }

        sum = (sum * sum) >> 8;
        return(Math.abs(sum) % M);
    }

    @Override
    public void insert(String seqID, memHandle handle) {
        long hashSlot = hash(seqID, tableSize);
        int bucket = (int) hashSlot / 32;
        int currSlot = (int) hashSlot % 32;
        
        //Try first slot to check for availability
        if(tableArray[(int)hashSlot] == null) {
            tableArray[(int)hashSlot] = handle;
            return;
        }
        //Move to next slot
        currSlot = (currSlot + 1) % 32;
        
        //Iterate through slots until one is found
        while((bucket * 32) + currSlot != hashSlot) {
            if(tableArray[(bucket * 32) + currSlot] == null) {
                tableArray[(bucket * 32) + currSlot] = handle;
                break;
            }
            
            //Move to next slot
            currSlot = (currSlot + 1) % 32;
        }
    }

    @Override
    public void remove(String seqID) {
        
    }

}
