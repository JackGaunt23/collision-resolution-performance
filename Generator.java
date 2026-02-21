import java.util.HashSet;
import java.util.Random;

public class Generator {
    public int[] generateUniqueKeys(int n, Random rand) {
        HashSet<Integer> seen = new HashSet<>(n * 2);
        int[] keys = new int[n];
        int i = 0;

        while (i < n) {
            int k = rand.nextInt();
            if (k == -1) continue;
            if (seen.add(k)) { //Only add unique keys
                keys[i] = k;
                i++;
            }
        }
        return keys;
    }

    public int[] generateKeysNotInTable(int numOfKeysNotInTable, int[] keys, Random rand) {
        int[] absent = new int[numOfKeysNotInTable];
        int i = 0;
        HashSet<Integer> inserted = new HashSet<>();
        for (int key : keys) {
            inserted.add(key);
        }

        while (i < numOfKeysNotInTable) {
            int k = rand.nextInt();
            if (k == -1) continue;
            if (!inserted.contains(k)) {
                absent[i] = k;
                i++;
            }
        }
        return absent;
    }
}
