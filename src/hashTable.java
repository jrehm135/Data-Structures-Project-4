/**
 * 
 */

/**
 * @author Josh
 *
 */
public interface hashTable<T> {
    //Define the string hash function
    long hash(String s, int M);
    
    void insert(String seqID, T e);
    
    void remove(String seqID);
}
