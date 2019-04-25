

public class testSequnce {

    public static void main(String[] args) {
        
        try {
            SequenceHash<MemHandle> hashTable = new SequenceHash<MemHandle>(100,"hashFile.bin");
            MemoryMan manager = new MemoryMan(0, "biofile.bin");
            MemHandle han = manager.insertSequence("ACGTTCGA");
            MemHandle han2 = manager.insertSequence("CGCGATCGCG");
            MemHandle han3 = manager.insertSequence("CGCGATCGCG");
            MemHandle han4 = manager.insertSequence("CGCGATCGCG");
            MemHandle han5 = manager.insertSequence("CGCGATCGCG");
            MemHandle han6 = manager.insertSequence("CGCGATCGCG");
            MemHandle han7 = manager.insertSequence("CGCGATCGCG");
            MemHandle han8 = manager.insertSequence("CGCGATCGCG");
            MemHandle han9 = manager.insertSequence("CGCGATCGCG");
            MemHandle han10 = manager.insertSequence("CGCGATCGCG");
            MemHandle han11 = manager.insertSequence("CGCGATCGCG");
            MemHandle han12 = manager.insertSequence("CGCGATCGCG");
            MemHandle han13 = manager.insertSequence("CGCGATCGCG");
            MemHandle han14 = manager.insertSequence("CGCGATCGCG");
            MemHandle han15 = manager.insertSequence("CGCGATCGCG");
            MemHandle han16 = manager.insertSequence("CGCGATCGCG");
            MemHandle han17 = manager.insertSequence("CGCGATCGCG");
            MemHandle han18 = manager.insertSequence("CGCGATCGCG");
            MemHandle han19 = manager.insertSequence("CGCGATCGCG");
            MemHandle han20 = manager.insertSequence("CGCGATCGCG");
            MemHandle han21 = manager.insertSequence("CGCGATCGCG");
            MemHandle han22 = manager.insertSequence("CGCGATCGCG");
            MemHandle han23 = manager.insertSequence("CGCGATCGCG");
            MemHandle han24 = manager.insertSequence("CGCGATCGCG");
            
            hashTable.insert("CABC", han);
            hashTable.insert("CABC", han2);
            hashTable.insert("CABC", han3);
            hashTable.insert("CABC", han4);
            hashTable.insert("CABC", han5);
            hashTable.insert("CABC", han6);
            hashTable.insert("CABC", han7);
            hashTable.insert("CABC", han8);
            hashTable.insert("CABC", han9);
            hashTable.insert("CABC", han10);
            hashTable.insert("CABC", han11);
            hashTable.insert("CABC", han12);
            hashTable.insert("CABC", han13);
            hashTable.insert("CABC", han14);
            hashTable.insert("CABC", han15);
            hashTable.insert("CABC", han16);
            hashTable.insert("CABC", han17);
            hashTable.insert("CABC", han18);
            hashTable.insert("CABC", han19);
            hashTable.insert("CABC", han20);
            hashTable.insert("CABC", han21);
            hashTable.insert("CABC", han22);
            hashTable.insert("CABC", han23);
            hashTable.insert("CABC", han24);
            manager.remove(han);
            manager.insertSequence("GACTAAAA");
        }
        catch (Exception FileNotFoundException) {
            // TODO Auto-generated catch block
            FileNotFoundException.printStackTrace();
        }
    }

}
