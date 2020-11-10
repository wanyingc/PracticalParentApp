package ca.cmpt276.practicalparent.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HistoryManager implements Iterable<HistoryEntry> {
    private List<HistoryEntry> historyList;

    // Singleton support
    private static HistoryManager instance;
    private HistoryManager() {
        historyList = new ArrayList<HistoryEntry>();
    }
    public static HistoryManager getInstance() {
        if (instance == null) {
            instance = new HistoryManager();
        }
        return instance;
    }

    public void addEntry(HistoryEntry entry) {
        historyList.add(entry);
    }
    public HistoryEntry getEntry(int index) {
        return historyList.get(index);
    }

    public int size() {
        return historyList.size();
    }
    public void clear() {historyList.clear();}
    public List<HistoryEntry> list() {
        return historyList;
    }


    @Override
    public Iterator<HistoryEntry> iterator() {
        return historyList.iterator();
    }
}
