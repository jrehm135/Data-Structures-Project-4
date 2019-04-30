import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DNAdbase {
    private static String memFile;
    private static String hashFile;
    private static int tableSize;
    private static SequenceHash<MemHandle> hashTable;


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
            while(sc.hasNextLine())
            {
                String command = sc.next();
                switch(command)
                {
                    case "insert":
                        String seqID = sc.next();
                        int seqLength = Integer.parseInt(sc.next());
                        String sequence = sc.nextLine();
                       // hashTable.insert(seqID, handles);
                        break;
                }
            }

        }
        catch (FileNotFoundException e) {
            // do nothing if file not found.
        }
    }
}
