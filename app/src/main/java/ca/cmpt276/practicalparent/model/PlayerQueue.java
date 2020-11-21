package ca.cmpt276.practicalparent.model;

import java.util.ArrayList;
import java.util.List;


public class PlayerQueue {
    private ChildManager childManager;
    private List<String> playerQueue;
    private static PlayerQueue instance;
    private PlayerQueue() {
        playerQueue = new ArrayList<String>();
        childManager = ChildManager.getInstance();
        fillQueue();
    }
    public static PlayerQueue getInstance() {
        if (instance == null) {
            instance = new PlayerQueue();
        }
        return instance;
    }

    private void fillQueue() {
        for (int i = 0; i < childManager.size(); i++) {
            playerQueue.add(childManager.getChild(i));
        }
    }

    /**
     * Gets the front element of the queue, and rotates the order to the right
     */
    public String frontRotate() {
        String front = playerQueue.remove(0);
        playerQueue.add(front);
        return front;
    }

    /**
     * Gets the element at the index of queue, and pushes to back
     */
    public String elementRotate(int i) {
        String element = playerQueue.remove(i);
        playerQueue.add(element);
        return element;
    }

    public String getPlayer(int i) {
        return playerQueue.get(i);
    }

    public List<String> list() {
        return playerQueue;
    }


}
