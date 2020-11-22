package ca.cmpt276.practicalparent.model;

import java.util.ArrayList;
import java.util.List;

public class ChildQueue {
    private static ChildQueue instance;
    private ChildManager childManager;
    private List<Child> queue;
    private ChildQueue() {
        childManager = ChildManager.getInstance();
        queue = new ArrayList<Child>();
        for (int i = 0; i < ChildManager.getInstance().size(); i++) {
            queue.add(childManager.getChild(i));
        }
    }
    public static ChildQueue getInstance() {
        if (instance == null) {
            instance = new ChildQueue();
        }
        return instance;
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

    public void update() {
        // Check if any children have been removed from ChildManager
        for (int i = 0; i < queue.size(); i++) {
            if (!childManager.list().contains(queue.get(i))) {
                queue.remove(i);
            }
        }

        // Check if any children have been added to ChildManager
        for (int i = 0; i < childManager.size(); i++) {
            if (!queue.contains(childManager.list().get(i))) {
                queue.add(childManager.list().get(i));
            }
        }
    }

    public List<Child> list() {
        return queue;
    }
}
