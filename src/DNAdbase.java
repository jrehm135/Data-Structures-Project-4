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
                        break;
                        
                    case "search":
                        seqID = sc.next();
                        sequence = hashTable.search(seqID, seqFile);
                        break;
                        
                    case "print":
                        MemHandle[] allHandles = hashTable.getAllHandles();
                        String[] sOut = memManager.print(allHandles);
                        break;
                }
            }

        }
        catch (Exception e) {
            // do nothing if file not found.
        }
    }
}
