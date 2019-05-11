import junit.framework.TestCase;

/**
 * 
 * @author Quinton Miller
 * @author Josh Rehm
 * @version 5/10/2019
 * 
 *          test the sequence class
 */
public class SequnceTest extends TestCase {

    /**
     * test the initialization of the sequence
     */
    public void testInit() {
        String foo = "ACATGA";
        Sequence test = new Sequence(foo);
        Sequence temp = new Sequence();
        String thing = temp.bytesToString(test.getBytes());
        String finalStr = thing.substring(0, foo.length());
        assertTrue(finalStr.equals(foo));
        assertTrue(test.toString().equals(foo));
    }

}
