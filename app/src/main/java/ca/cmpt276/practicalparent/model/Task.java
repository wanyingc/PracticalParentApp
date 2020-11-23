package ca.cmpt276.practicalparent.model;

/**
 * Used to store attributes of a task
 */

public class Task {
    private String taskName;
    private String childTurn;

    //Constructor
    public Task(String taskName, String childTurn) {
        this.taskName = taskName;
        this.childTurn = childTurn;
    }

    public String getTaskName() {
        return taskName;
    }
    public String getChildTurn() {
        return childTurn;
    }


    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public void setChildTurn(String childTurn) {
        this.childTurn = childTurn;
    }


}
