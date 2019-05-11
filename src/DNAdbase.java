import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

/**
 * 
 * @author Josh Rehm
 * @author Quinton Miller
 * 
 * @version 5/8/2019 THis is the main function for the DNA data base
 *
 */
public class DNAdbase {
    private static String memFile;
    private static String hashFile;
    private static int tableSize;
    private static SequenceHash<MemHandle> hashTable;
    private static MemoryMan memManager;


    /**
     * this starts the DNAdbase program
     * 
     * @param args
     *            the memfile, hasfhfile, commandfile, and table size
     */
    public static void main(String[] args) {
        hashFile = args[1];
        tableSize = Integer.parseInt(args[2]);
        memFile = args[3];
        parseFile(args[0]);
    }


    /**
     * parses through the command file and runs the commands
     * 
     * @param fileName
     *            name of the command file to parse through
     */
    private static void parseFile(String fileName) {
        try {
            Scanner sc = new Scanner(new File(fileName));
            hashTable = new SequenceHash<MemHandle>(tableSize, hashFile);
            memManager = new MemoryMan(memFile);
            RandomAccessFile seqFile = new RandomAccessFile(memFile, "rw");
            while (sc.hasNextLine()) {
                String command = sc.next();
                MemHandle[] handles;
                switch (command) {
                    case "insert":
                        String seqID = sc.next();
                        int length = sc.nextInt();
                        String sequence = sc.next();
                        try {
                            int searchFlag = hashTable.checkForDuplicate(seqID,
                                seqFile);
                            if (searchFlag == 1) {
                                handles = memManager.insert(seqID, sequence, length);
                                hashTable.insert(seqID, handles, seqFile);
                            }
                            else if (searchFlag == -1) {
                                System.out.println("Bucket full.Sequence "
                                    + seqID + " could not be inserted");
                            }
                            else if (searchFlag == 0) {
                                System.out.println("SequenceID " + seqID
                                    + " exists");
                            }
                        }
                        catch (IOException e) {
                            System.out.println("IO issue in insert");
                        }
                        break;

                    case "remove":
                        seqID = sc.next();
                        handles = hashTable.remove(seqID, seqFile);
                        if (handles[0] == null) {
                            System.out.println("SequenceID " + seqID
                                + " not found");
                            continue;
                        }
                        memManager.remove(handles);
                        byte[] fromFile = new byte[(int)Math.ceil(handles[1]
                            .getMemLength() / 4.0)];
                        seqFile.seek(handles[1].getMemLoc());
                        seqFile.read(fromFile, 0, (int)Math.ceil(handles[1]
                            .getMemLength() / 4.0));
                        Sequence seq = new Sequence();
                        String firstString = seq.bytesToString(fromFile);
                        String stringSequence = firstString.substring(0,
                            handles[1].getMemLength());
                        System.out.println("Sequence Removed " + seqID + ":");
                        System.out.println(stringSequence);
                        break;

                    case "search":
                        seqID = sc.next();
                        sequence = hashTable.search(seqID, seqFile);
                        if (sequence.equals("")) {
                            System.out.println("SequenceID " + seqID
                                + " not found");
                        }
                        else {
                            System.out.println("Sequence Found: " + sequence);
                        }
                        break;

                    case "print":
                        int[] hashLocs = new int[hashTable.getSize()];
                        MemHandle[] allHandles = hashTable.getAllHandles(
                            hashLocs);
                        String[] sOut = memManager.print(allHandles);
                        System.out.println("Sequence IDs:");
                        for (int i = 0; i < hashTable.getSize(); i++) {
                            System.out.println(sOut[i] + ": hash slot ["
                                + hashLocs[i] + "]");
                        }
                        DoublyLinkedList<FreeBlock> freeBlocks = memManager
                            .getFreeBlocksList();
                        if (freeBlocks.getLength() == 0) {
                            System.out.println("Free Block List: none");
                            break;
                        }
                        int blockCount = 0;
                        freeBlocks.moveToHead();
                        System.out.println("Free Block List:");
                        while (freeBlocks.hasNext()) {
                            freeBlocks.next();
                            blockCount++;
                            FreeBlock tempBlock = freeBlocks.getElement();
                            System.out.println("[Block " + blockCount
                                + "] Starting Byte Location: " + tempBlock
                                    .getPos() + ", Size " + tempBlock
                                        .getLength() + " bytes");
                        }
                        break;
                    default:
                        System.out.println("Unrecognized input");
                        break;
                }
            }
            sc.close();
        }
        catch (Exception e) {
            // do nothing if file not found.
        }
    }
}
