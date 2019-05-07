import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class DNAdbase {
    private static String memFile;
    private static String hashFile;
    private static int tableSize;
    private static SequenceHash<MemHandle> hashTable;
    private static MemoryMan memManager;


    public static void main(String args[]) {
        hashFile = args[1];
        tableSize = Integer.parseInt(args[2]);
        memFile = args[3];
        parseFile(args[0]);
    }


    private static void parseFile(String fileName) {
        try {
            Scanner sc = new Scanner(new File(fileName));
            hashTable = new SequenceHash<MemHandle>(tableSize, hashFile);
            memManager = new MemoryMan(memFile);
            RandomAccessFile seqFile = new RandomAccessFile(memFile, "rw");
            while(sc.hasNextLine())
            {
                String command = sc.next();
                switch(command)
                {
                    case "insert":
                        String seqID = sc.next();
                        int seqLength = Integer.parseInt(sc.next());
                        String sequence = sc.nextLine();
                        MemHandle[] handles = memManager.insert(seqID, sequence);
                        hashTable.insert(seqID, handles);
                        break;
                        
                    case "remove":
                        seqID = sc.next();
                        handles = hashTable.remove(seqID, seqFile);
                        memManager.remove(handles);
                        byte[] fromFile = new byte[(int)Math.ceil(handles[1].getMemLength()
                                / 4.0)];
                        seqFile.seek(handles[1].getMemLoc());
                        seqFile.read(fromFile, 0, handles[1].getMemLength());
                        Sequence seq = new Sequence();
                        String stringSequence = seq.bytesToString(fromFile);
                        System.out.print("Sequence Removed: " + seqID);
                        System.out.print(stringSequence);
                        break;
                        
                    case "search":
                        seqID = sc.next();
                        sequence = hashTable.search(seqID, seqFile);
                        if(sequence == "") {
                            System.out.print("SequenceID "+ seqID + " not found");
                        }
                        else {
                            System.out.print("Sequence Found: "+ sequence);
                        }
                        break;
                        
                    case "print":
                        int[] hashLocs = new int[hashTable.getSize()];
                        MemHandle[] allHandles = 
                        hashTable.getAllHandles(hashLocs);
                        String[] sOut = memManager.print(allHandles);
                        System.out.print("Sequence IDs:");
                        for(int i = 0; i < hashTable.getSize(); i++) {
                            System.out.print(sOut[i] + ": hash slot [" + hashLocs[i] + "]");
                        }
                        DoublyLinkedList<FreeBlock> freeBlocks
                        = memManager.getFreeBlocksList();
                        if(freeBlocks.getLength() == 0) {
                            System.out.print("Free Block List: none");
                            break;
                        }
                        int blockCount = 0;
                        while(freeBlocks.hasNext()) {
                            freeBlocks.next();
                            blockCount++;
                            FreeBlock tempBlock = freeBlocks.getElement();
                            System.out.print("[Block " + blockCount
                                    + "] Starting Byte Location: " + 
                                    tempBlock.getPos() + ", Size " +
                                    tempBlock.getLength() + " bytes");
                        }
                        break;
                }
            }

        }
        catch (Exception e) {
            // do nothing if file not found.
        }
    }
}
