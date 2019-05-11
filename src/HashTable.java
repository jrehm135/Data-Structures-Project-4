import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 * @author Josh Rehm
 * @author Quinton Miller
 * @version 5/10/2019
 *
 * @param <T> memory handles
 */
public interface HashTable<T> {
    /**
     * define the hash function
     * @param s string in
     * @param m bucket
     * @return the slot
     */
    public long hash(String s, int m);

    /**
     * insert into the table
     * @param seqID sequence to insert
     * @param e memHandles
     * @param file hash file
     * @return memory handles
     * @throws IOException when the file access is not correct.
     */
    public int insert(String seqID, T[] e, RandomAccessFile file)
        throws IOException;

    /**
     * remove from table
     * @param seqID id to remove
     * @param file file to remove from
     * @return memory handles removed
     * @throws IOException bad file access
     */
    public T[] remove(String seqID, RandomAccessFile file)
        throws IOException;

    /**
     * search through the table
     * @param seqID id to search for
     * @param file file to search through
     * @return sequence in the file
     * @throws IOException bad file access
     */
    public String search(String seqID, RandomAccessFile file)
        throws IOException;
    
    /**
     * get the handles a t a particular location
     * @param location place to get handles from
     * @return handles at location.
     */
    public T[] getHandles(int location);
}