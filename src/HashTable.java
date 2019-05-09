import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 */

/**
 * @author Josh
 *
 */
public interface HashTable<T> {
    // Define the string hash function
    public long hash(String s, int M);


    public boolean insert(String seqID, T[] e)
        throws IOException;;


    public T[] remove(String seqID, RandomAccessFile file)
        throws IOException;


    public String search(String seqID, RandomAccessFile file)
        throws IOException;
    
    public T[] getHandles(int location);
}