import java.util.Random;
import java.util.Arrays;
import java.util.HashSet;

public class linearprobing {
    private int[] hashTable;
    private int size;

    public linearprobing(int size) {
        this.size = size;
        this.hashTable = new int[size];
        for (int i = 0; i < size; i++) {
            hashTable[i] = -1;
        }
    }

    private int hashFunction(int key) { //Simple uniform hashing function
        //return Math.abs(key) % size;
        return Math.floorMod(key, size);
    }

    public void insertListOfKeys(int[] keys) {
        for (int key : keys){
            int index = hashFunction(key);
            while (hashTable[index] != -1) {
                index = (index + 1) % size; //Linear probing to find the next available slot
            }
            hashTable[index] = key;
        }
    }

    //returns number of probes required to find the key
    public int search(int key) {
        int index = hashFunction(key);
        int probes = 0;
        while (true) {
            probes++;
            if (hashTable[index] == -1) {
                return probes;  // Unsuccessful
            }
            if (hashTable[index] == key) {
                return probes;  // Successful
            }
            index = (index + 1) % size;
        }
    }


    public static void main(String[] args) {
        double[] loadFactors = {0.1, 0.3, 0.5, 0.7, 0.9};

        Random rand = new Random(12345); //Fixed seed for reproducibility

        for (double loadFactor : loadFactors) {
            
            int numTrials = 100000;

            int tableSize = 1000000; //fixed

            Generator generator = new Generator();

            linearprobing lp = new linearprobing(tableSize);
            int n = (int)Math.floor(tableSize * loadFactor);


            int[] keys = generator.generateUniqueKeys(n, rand);
            lp.insertListOfKeys(keys);

            int[] successfulSearchProbeValues = new int[numTrials];
            int[] unsuccessfulSearchProbeValues = generator.generateKeysNotInTable(numTrials, keys, rand);
            for (int i = 0; i < numTrials; i++) {
                successfulSearchProbeValues[i] = keys[rand.nextInt(n)]; //Randomly select a key from the inserted keys
            }

            int totalProbesSuccessful = 0;
            for (int key : successfulSearchProbeValues) {
                totalProbesSuccessful += lp.search(key);
            }
            double averageProbesSuccessful = (double) totalProbesSuccessful / numTrials;
            System.out.println("Average probes for successful search using load factor: "+ loadFactor+ " : " + averageProbesSuccessful);

            int totalProbesUnsuccessful = 0;
            for (int key : unsuccessfulSearchProbeValues) {
                totalProbesUnsuccessful += lp.search(key);
            }
            double averageProbesUnsuccessful = (double) totalProbesUnsuccessful / numTrials;
            System.out.println("Average probes for unsuccessful search using load factor: "+ loadFactor+ " : " + averageProbesUnsuccessful + "\n");

            // Run time 
            long startTime = System.nanoTime();
            for (int key : successfulSearchProbeValues) {
                 lp.search(key);
            }
            long endTime = System.nanoTime();
            long durationSuccessful = endTime - startTime;
            //System.out.println("Total time for successful searches with load factor " + loadFactor + ": " + durationSuccessful + " nanoseconds");
            System.out.println(durationSuccessful/(double)numTrials + " nanoseconds per successful search, for load factor: " + loadFactor);

            startTime = System.nanoTime();
            for (int key : unsuccessfulSearchProbeValues) {
                lp.search(key);
            }
            endTime = System.nanoTime();
            long durationUnsuccessful = endTime - startTime;
            //System.out.println("Total time for unsuccessful searches with load factor " + loadFactor + ": " + durationUnsuccessful + " nanoseconds\n");
            System.out.println(durationUnsuccessful/(double)numTrials + " nanoseconds per unsuccessful search, for load factor: " + loadFactor + "\n");
            }

        
    }
}
