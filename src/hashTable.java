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
    
    void insert(T e);
    
    void remove(T e);
}
