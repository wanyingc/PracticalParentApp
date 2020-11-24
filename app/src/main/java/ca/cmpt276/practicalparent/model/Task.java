package ca.cmpt276.practicalparent.model;

import androidx.annotation.Nullable;

/**
 * Used to store attributes of a task
 */

public class Task {
    private String taskName;
    private Child child;

    //Constructor
    public Task(String taskName, Child child) {
        this.taskName = taskName;
        this.child = child;
    }

    public String getTaskName() {
        return taskName;
    }
    public Child getChild() {
        return child;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public void setChild(Child child) {
        this.child = child;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Task t = (Task)obj;
        return this.getTaskName().equals(t.getTaskName());
    }
}
