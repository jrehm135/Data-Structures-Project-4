/**
 * 
 */

/**
 * @author Josh
 *
 */
public class Sequence {
    private int seqLength;
    private byte seqArray[];
    
    Sequence(String inString){
        //Allocate bytes for the sequence
        seqArray = new byte[(int) Math.ceil(inString.length() / 4.0)];
        seqLength = inString.length();
        
        for(int i = 0; i < inString.length(); i++) {
            //Begin at the start of byte minus 2, then subtract
            // the number of bits into the byte we are
            int bytePos = 6 - (i % 4) * 2;
            //Divide by the current position in string to find current byte
            int currByte = i / 4;
            char currChar = inString.charAt(i);
            if(currChar == 'A') {
                seqArray[currByte] |= 0 << bytePos;
            }
            else if(currChar == 'C') {
                seqArray[currByte] |= 1 << bytePos;
            }
            else if(currChar == 'G') {
                seqArray[currByte] |= 2 << bytePos;
            }
            else if(currChar == 'T') {
                seqArray[currByte] |= 3 << bytePos;
            }
            else {
                System.out.print("Could not create sequence!");
            }
        }
    }
    
    public int getLength() {
        return this.seqLength;
    }
    
    public byte[] getBytes() {
        return this.seqArray;
    }
    
    @Override
    public String toString() {
        StringBuilder buildString = new StringBuilder();
        for(int i = 0; i < this.seqLength; i++) {
            //Begin at the start of byte minus 2, then subtract
            // the number of bits into the byte we are
            int bytePos = 6 - (i % 4) * 2;
            //Only need 2 bits, so and with 3
            int currChar = 0x3 & seqArray[i/4] >> bytePos;
            
            if(currChar == 0) {
                buildString.append('A');
            }
            else if(currChar == 1) {
                buildString.append('C');
            }
            else if(currChar == 2) {
                buildString.append('G');
            }
            else if(currChar == 3) {
                buildString.append('T');
            }
        }
        
        String seqString = buildString.toString();
        return seqString;
    }
        
}
