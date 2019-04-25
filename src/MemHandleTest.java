import junit.framework.TestCase;

public class MemHandleTest extends TestCase {
    MemHandle test;
    
    public void testGetSet()
    {
        test = new MemHandle(0, 10);
        assertTrue(test.getMemLoc() == 0);
        assertTrue(test.getMemLength() == 10);
    }
}
