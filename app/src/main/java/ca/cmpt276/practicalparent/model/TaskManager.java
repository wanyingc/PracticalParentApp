package ca.cmpt276.practicalparent.model;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Used to manage a list of tasks.
 */

public class TaskManager implements Iterable<Task>  {

    private List<Task> taskList = new ArrayList<>();

    //Singleton support
    private static TaskManager instance;
    private TaskManager() {
        // Private to prevent other instances.
    }
    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    public void addTask(Task task) {
        taskList.add(task);
    }
    public boolean emptyTask() {return taskList.isEmpty();}

    public Task getTask(int index) {
        return taskList.get(index);
    }
    public List<Task> TaskList() {
        return taskList;
    }
    public void clear() {
        taskList.clear();
    }
    public boolean contains(String taskName) {
        return (taskList.contains(taskName));
    }
    public int size(){return taskList.size();}
    public void deleteTask(int index) {
        taskList.remove(index);
    }

    @Override
    public Iterator<Task> iterator() {
        return taskList.iterator();
    }
}
