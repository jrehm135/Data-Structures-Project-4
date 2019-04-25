/**
 * 
 */

/**
 * @author Josh
 *
 */
public interface HashTable<T> {
    //Define the string hash function
    long hash(String s, int M);
    
    boolean insert(String seqID, T e);
    
    void remove(String seqID);
}
