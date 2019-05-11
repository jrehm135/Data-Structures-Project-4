import junit.framework.TestCase;

/**
 * test mem handle
 * 
 * @author Quinton Miller
 * @author Josh Rehm
 * @version 5/10/2019
 *
 */
public class MemHandleTest extends TestCase {
   


    /**
     * test getting and setting
     */
    public void testGetSet() {
        MemHandle test = new MemHandle(0, 10);
        assertEquals(test.getMemLoc(), 0);
        assertEquals(test.getMemLength(), 10);
    }
}
