import java.math.BigInteger;

/**
 * Created by Cody on 6/15/2016.
 */
public class Stats {
    public static int nodesEval = 0;
    public static long aiMoveStart = 0;
    public static long aiMoveEnd = 0;
    public static long sortTot = 0;
    public static long sortAmt = 0;
    public static long getMove = 0;
    public static long getMoveAmt = 0;
    public static long cloneTime = 0;
    public static long cloneAmt = 0;
    public static long getValueTime = 0;
    public static long getValueAmt = 0;
    public static long hashTableHits = 0;
    public static long hashTableSuccess = 0;
    public static void reset() {
        nodesEval = 0;
        aiMoveStart = 0;
        aiMoveEnd = 0;
        sortTot = 0;
        sortAmt = 0;
        getMove = 0;
        getMoveAmt = 0;
        cloneTime = 0;
        cloneAmt = 0;
        getValueTime = 0;
        getValueAmt = 0;
        hashTableHits = 0;
        hashTableSuccess = 0;
    }
}
