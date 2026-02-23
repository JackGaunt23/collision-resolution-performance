import java.util.LinkedList;
import java.util.Random;

public class chaining {
    private LinkedList<Integer>[] hashTable;
    private int size;

    public chaining(int size) {
        this.size = size;
        hashTable = new LinkedList[size];
        for (int i = 0; i < size; i++) {
            hashTable[i] = new LinkedList<>();
        }
    }

    private int hashFunction(int key) {
        return Math.abs(key) % size;
    }

    public void insertListOfKeys(int[] keys) {
        for (int key : keys) {
            int index = hashFunction(key);
                hashTable[index].add(key);
            
        }
    }

    //returns number of probes required to find the key
    public int search(int key) {
        int index = hashFunction(key);
        LinkedList<Integer> bucket = hashTable[index];
        int probes = 1;
        for (int k : bucket) {
            if (k == key) {
                return probes; //Key found
            }
            probes++;
        }
        return probes; //Key not found
    }

    public static void main(String[] args) {
        double[] loadFactors = {0.1, 0.3, 0.5, 0.7, 0.9};

        Random rand = new Random(12345); //Fixed seed for reproducibility

        for (double loadFactor : loadFactors) {
            
            int numTrials = 100000;

            int tableSize = 1000000; //fixed

            Generator generator = new Generator();

            chaining ch = new chaining(tableSize);
            int n = (int)Math.floor(tableSize * loadFactor);


            int[] keys = generator.generateUniqueKeys(n, rand);
            ch.insertListOfKeys(keys);

            int[] successfulSearchProbeValues = new int[numTrials];
            int[] unsuccessfulSearchProbeValues = generator.generateKeysNotInTable(numTrials, keys, rand);
            for (int i = 0; i < numTrials; i++) {
                successfulSearchProbeValues[i] = keys[rand.nextInt(n)]; //Randomly select a key from the inserted keys
            }

            int totalProbesSuccessful = 0;
            for (int key : successfulSearchProbeValues) {
                totalProbesSuccessful += ch.search(key);
            }
            double averageProbesSuccessful = (double) totalProbesSuccessful / numTrials;
            System.out.println("Average probes for successful search using load factor: "+ loadFactor+ " : " + averageProbesSuccessful);

            int totalProbesUnsuccessful = 0;
            for (int key : unsuccessfulSearchProbeValues) {
                totalProbesUnsuccessful += ch.search(key);
            }
            double averageProbesUnsuccessful = (double) totalProbesUnsuccessful / numTrials;
            System.out.println("Average probes for unsuccessful search using load factor: "+ loadFactor+ " : " + averageProbesUnsuccessful + "\n");

            //Run time trials
            long startTime = System.nanoTime();
            for (int key : successfulSearchProbeValues) {
                ch.search(key);
            }
            long endTime = System.nanoTime();
            long durationSuccessful = endTime - startTime;
            //System.out.println("Total time for successful searches with load factor " + loadFactor + ": " + durationSuccessful + " nanoseconds");
            System.out.println(durationSuccessful/(double)numTrials + " nanoseconds per successful search, for load factor: " + loadFactor);

            startTime = System.nanoTime();
            for (int key : unsuccessfulSearchProbeValues) {
                ch.search(key);
            }
            endTime = System.nanoTime();
            long durationUnsuccessful = endTime - startTime;
            //System.out.println("Total time for unsuccessful searches with load factor " + loadFactor + ": " + durationUnsuccessful + " nanoseconds\n");
            System.out.println(durationUnsuccessful/(double)numTrials + " nanoseconds per unsuccessful search, for load factor: " + loadFactor + "\n");
            }

        
    }
}
