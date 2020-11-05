package ca.cmpt276.practicalparent.model;
import java.util.Random;
public class Coin {
    public static final int HEADS = 0;
    public static final int TAILS = 1;
    private int result;

    private Coin() {
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
        // randomly generates either a 0 or a 1
        result = Math.random() > 0.5 ? 1 : 0;
    }
    public int getCoin() {
        return result;
    }
}
