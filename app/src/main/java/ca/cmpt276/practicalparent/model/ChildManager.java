package ca.cmpt276.practicalparent.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Used to manage a list of children.
 */

public class ChildManager implements Iterable<String> {
    // Creates an ArrayList to store lenses
    private List<String> children = new ArrayList<>();

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

    // Method to add lenses
    public void add(String child) {
        children.add(child);
    }

    // Getters
    public int size() {
        return children.size();
    }
    public String getChild(int index) {
        return children.get(index);
    }

    // Setters
    public void setChild(int index, String name) {
        children.set(index, name);
        return;
    }

    public void deleteChild(int index) {
        children.remove(index);
        return;
    }

    public void clear() {
        children.clear();
    }

    @Override
    public Iterator<String> iterator() {
        return children.iterator(); // passes ArrayList's iterator method
    }

}
