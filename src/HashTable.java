import java.io.RandomAccessFile;

/**
 * 
 */

/**
 * @author Josh
 *
 */
public interface HashTable<T> {
    //Define the string hash function
    public long hash(String s, int M);
    
    public boolean insert(String seqID, T[] e);
    
    public boolean remove(String seqID, RandomAccessFile file);
}
