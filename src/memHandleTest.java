import junit.framework.TestCase;

public class memHandleTest extends TestCase {
    memHandle test;
    
    public void testGetSet()
    {
        test = new memHandle(0, 10);
        assertTrue(test.getMemLoc() == 0);
        assertTrue(test.getMemLength() == 10);
    }
}
