import java.io.FileNotFoundException;

public class testSequnce {

    public static void main(String[] args) {
        
        try {
            sequenceHash<memHandle> hashTable = new sequenceHash<memHandle>(100);
            memoryMan manager = new memoryMan(0, "biofile.bin");
            memHandle han = manager.insertSequence("ACGTTCGA");
            memHandle han2 = manager.insertSequence("CGCGATCGCG");
            memHandle han3 = manager.insertSequence("CGCGATCGCG");
            memHandle han4 = manager.insertSequence("CGCGATCGCG");
            memHandle han5 = manager.insertSequence("CGCGATCGCG");
            memHandle han6 = manager.insertSequence("CGCGATCGCG");
            memHandle han7 = manager.insertSequence("CGCGATCGCG");
            memHandle han8 = manager.insertSequence("CGCGATCGCG");
            memHandle han9 = manager.insertSequence("CGCGATCGCG");
            memHandle han10 = manager.insertSequence("CGCGATCGCG");
            memHandle han11 = manager.insertSequence("CGCGATCGCG");
            memHandle han12 = manager.insertSequence("CGCGATCGCG");
            memHandle han13 = manager.insertSequence("CGCGATCGCG");
            memHandle han14 = manager.insertSequence("CGCGATCGCG");
            memHandle han15 = manager.insertSequence("CGCGATCGCG");
            memHandle han16 = manager.insertSequence("CGCGATCGCG");
            memHandle han17 = manager.insertSequence("CGCGATCGCG");
            memHandle han18 = manager.insertSequence("CGCGATCGCG");
            memHandle han19 = manager.insertSequence("CGCGATCGCG");
            memHandle han20 = manager.insertSequence("CGCGATCGCG");
            memHandle han21 = manager.insertSequence("CGCGATCGCG");
            memHandle han22 = manager.insertSequence("CGCGATCGCG");
            memHandle han23 = manager.insertSequence("CGCGATCGCG");
            memHandle han24 = manager.insertSequence("CGCGATCGCG");
            
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
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
