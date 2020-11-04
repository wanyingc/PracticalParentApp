package ca.cmpt276.practicalparent.model;
import java.util.Random;
public class Coin {
    public static final int HEADS = 1;
    public static final int TAILS = 2;
    private Random rand;
    private int result;

    private Coin() {
        rand = new Random();
        result = 0;
    }
    /*
        Singleton Support
     */
    private static Coin instance;
    public static Coin getInstance() {
        if (instance == null) {
            instance = new Coin();
        }
        return instance;
    }

    public void flip() {
        // randomly generates either a 1 or a 2
        result = rand.nextInt(3);
    }
    public int getCoin() {
        return result;
    }
}
