/**
 * 
 */

/**
 * @author Josh
 * @author Quinton Miller
 * 
 * @version 5/8/2019
 *
 */
public class Sequence {
    private int seqLength;
    private byte[] seqArray;


    /**
     * make a new sequence
     * 
     * @param inString
     *            string to make into a sequence
     */
    Sequence(String inString) {
        // Allocate bytes for the sequence
        seqArray = new byte[(int)Math.ceil(inString.length() / 4.0)];
        seqLength = inString.length();

        for (int i = 0; i < inString.length(); i++) {
            // Begin at the start of byte minus 2, then subtract
            // the number of bits into the byte we are
            int bytePos = 6 - (i % 4) * 2;
            // Divide by the current position in string to find current byte
            int currByte = i / 4;
            char currChar = inString.charAt(i);
            if (currChar == 'A') {
                seqArray[currByte] |= 0 << bytePos;
            }
            else if (currChar == 'C') {
                seqArray[currByte] |= 1 << bytePos;
            }
            else if (currChar == 'G') {
                seqArray[currByte] |= 2 << bytePos;
            }
            else if (currChar == 'T') {
                seqArray[currByte] |= 3 << bytePos;
            }
            else {
                System.out.print("Could not create sequence!");
            }
        }
    }


    /**
     * default constructor of sequence
     */
    Sequence() {
        // this helps with converting bytes back to a string.
    }


    /**
     * get length of sequence
     * 
     * @return length of sequence
     */
    public int getLength() {
        return this.seqLength;
    }

    /**
     * get the bytes of the sequence
     * @return byte array of sequence
     */
    public byte[] getBytes() {
        return this.seqArray;
    }


    /**
     * convert bytes to a string
     * 
     * @param input
     *            bytes to convert
     * @return converted string from bytes
     */
    public String bytesToString(byte[] input) {
        StringBuilder buildString = new StringBuilder();
        for (int i = 0; i < input.length * 4; i++) {
            // Begin at the start of byte minus 2, then subtract
            // the number of bits into the byte we are
            int bytePos = 6 - (i % 4) * 2;
            // Only need 2 bits, so and with 3
            int currChar = 0x3 & input[i / 4] >> bytePos;

            if (currChar == 0) {
                buildString.append('A');
            }
            else if (currChar == 1) {
                buildString.append('C');
            }
            else if (currChar == 2) {
                buildString.append('G');
            }
            else if (currChar == 3) {
                buildString.append('T');
            }
        }

        String seqString = buildString.toString();
        return seqString;
    }


    @Override
    public String toString() {
        StringBuilder buildString = new StringBuilder();
        for (int i = 0; i < this.seqLength; i++) {
            // Begin at the start of byte minus 2, then subtract
            // the number of bits into the byte we are
            int bytePos = 6 - (i % 4) * 2;
            // Only need 2 bits, so and with 3
            int currChar = 0x3 & seqArray[i / 4] >> bytePos;

            if (currChar == 0) {
                buildString.append('A');
            }
            else if (currChar == 1) {
                buildString.append('C');
            }
            else if (currChar == 2) {
                buildString.append('G');
            }
            else if (currChar == 3) {
                buildString.append('T');
            }
        }

        String seqString = buildString.toString();
        return seqString;
    }

}
