

public class testSequnce {

    public static void main(String[] args) {
        
        try {
            SequenceHash<MemHandle> hashTable = new SequenceHash<MemHandle>(100,"hashFile.bin");
            MemoryMan manager = new MemoryMan("biofile.bin");
            manager.insertSequence("GACTAAAA");
        }
        catch (Exception FileNotFoundException) {
            // TODO Auto-generated catch block
            FileNotFoundException.printStackTrace();
        }
    }

}
