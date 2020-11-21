package ca.cmpt276.practicalparent.model;

/**
 * Used to store attributes of a task
 */

public class Task {
    private String taskName;

    //Constructor
    public Task(String taskName) {
        this.taskName = taskName;
    }

    //Getter
    public String getTaskName() {
        return taskName;
    }

    //Setter
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
