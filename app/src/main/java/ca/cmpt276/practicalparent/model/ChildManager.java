package ca.cmpt276.practicalparent.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * Used to manage a list of children.
 */

public class ChildManager implements Iterable<Child> {
    // Creates an ArrayList to store lenses
    private List<Child> children = new ArrayList<>();
    public final static Child NO_CHILD = new Child("");
    // Singleton support
    private static ChildManager instance;
    private ChildManager() {
        // Private to prevent other instances.
    }
    public static ChildManager getInstance() {
        if (instance == null) {
            instance = new ChildManager();
        }
        return instance;
    }

    // Method to add children
    public void add(Child child) {
        children.add(child);
    }

    // Getters
    public int size() {
        return children.size();
    }
    public Child getChild(int index) {
        if (index < 0) {
            return NO_CHILD;
        }
        return children.get(index);
    }
    public List children() {
        return children;
    }

    public void deleteChild(int index) {
        children.remove(index);
        return;
    }

    public int indexOfChild(Child child) {
        int index = -1;
        for (int i = 0; i < size(); i++) {
            if (getChild(i).getName().equals(child.getName())) {
                index = i;
            }
        }
        return index;
    }

    public void clear() {
        children.clear();
    }

    public List<Child> list() {
        return children;
    }

    @Override
    public Iterator<Child> iterator() {
        return children.iterator(); // passes ArrayList's iterator method
    }

}
