package ca.cmpt276.practicalparent.model;

import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.practicalparent.view.CoinFlipActivity;

public class ChildQueue {
    private static ChildQueue instance;
    public static final Child EMPTY_PLAYER = new Child("");
    private ChildManager childManager;
    private List<Child> queue;
    private ChildQueue() {
        childManager = ChildManager.getInstance();
        queue = new ArrayList<Child>();
//        for (int i = 0; i < ChildManager.getInstance().size(); i++) {
//            queue.add(childManager.getChild(i));
//        }
    }
    public static ChildQueue getInstance() {
        if (instance == null) {
            instance = new ChildQueue();
        }
        return instance;
    }

    public void enqueue(Child child) {
        queue.add(child);
    }

    public void enqueue(Child child, int pos) {
        try {
            queue.add(pos, child);
        } catch (Exception e) {
            enqueue(child);
        }
    }

    public Child rotate() {
        try {
            Child child = queue.remove(0);
            queue.add(child);
            return queue.get(0);
        } catch (Exception e) {
            return ChildManager.NO_CHILD;
        }
    }

    public Child moveToFront(int i) {
        try {
            Child child = queue.remove(i);
            queue.add(0, child);
            return queue.get(0);
        } catch (Exception e) {
            return ChildManager.NO_CHILD;
        }
    }

    public void addEmptyPlayer() {
        queue.add(0, EMPTY_PLAYER);
    }

    public void removeEmptyPlayer() {
        if (queue.contains(EMPTY_PLAYER)) {
            queue.remove(EMPTY_PLAYER);
        }
    }

    public Child peek() {
        try {
            return queue.get(0);
        } catch (Exception e) {
            return ChildManager.NO_CHILD;
        }
    }

    public Child getChild(int i) {
        return queue.get(i);
    }

    public int getPosition(Child child) {
        try {
            return queue.indexOf(child);
        } catch(Exception e) {
            return -1;
        }
    }

    public void update() {
        // Check if any children have been removed from ChildManager
        for (int i = 0; i < queue.size(); i++) {
            if (!childManager.list().contains(queue.get(i)) && !queue.get(i).equals(EMPTY_PLAYER)) {
                queue.remove(i);
            }
        }

        // Check if any children have been added to ChildManager
        for (int i = 0; i < childManager.size(); i++) {
            if (!queue.contains(childManager.list().get(i))) {
                queue.add(childManager.list().get(i));
            }
        }

        updatePlayerPortraits();
    }

    private void updatePlayerPortraits() {
        if (childManager.size() > 0) {
            Child child;
            for (int i = 0; i < queue.size(); i++) {
                child = queue.get(i);
                queue.set(i, childManager.getChild(childManager.indexOfChild(child)));
            }
        }
    }

    public void clearQueue() {
        queue.clear();
    }

    public List<Child> list() {
        return queue;
    }
}
