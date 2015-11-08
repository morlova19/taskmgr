package model;

import start.Main;

import java.io.Serializable;
import java.util.*;

/**
 * Part of taskmgr.
 */
public class Journal implements Serializable {
    /**
     * List of the current tasks.
     */
    private Vector<Task> currentTasks = new Vector<>();
    /**
     * List of the completed tasks.
     */
    private Vector<Task> completedTasks = new Vector<>();
    /**
     * Gets the task by identifier.
     * @param id identifier.
     * @return the found task.
     */
    public Task getTask(int id) {

        if (Main.CURRENT == Main.COMPLETED)
        {
            return getTaskFromCollection(completedTasks,id);
        }
        else
        {
            return getTaskFromCollection(currentTasks,id);
        }
    }
    /**
     * Adds the current tasks.
     * @param newTask new task.
     */
    void addTask(Task newTask) {
        if(currentTasks != null) {
            currentTasks.add(newTask);
        }
    }
    /**
     * Gets the current task by identifier to display the notification.
     * @param id identifier
     * @return found task.
     */
    Task getCurrentTask(int id)
    {
        return getTaskFromCollection(currentTasks,id);
    }
    /**
     * Deletes the task.
     * @param task task that will be deleted.
     */
    void deleteTask(Task task) {
        if (Main.CURRENT == Main.COMPLETED) completedTasks.remove(task);
        else currentTasks.remove(task);
    }
    /**
     * Sets the new date of execution of the task.
     * @param id identifier of task.
     * @param newDate new date of execution.
     */
    void delayTask(int id, Date newDate) {// откладывает задачу на одну минуту
        Task t = getTaskFromCollection(currentTasks,id);
        t.setDate(newDate);
    }
    /**
     * Finds the task in list of the tasks by identifier.
     * @param tasks list in which searches task.
     * @param id identifier of the task.
     * @return the found task.
     */
    private Task getTaskFromCollection(Vector<Task> tasks, int id)
    {
        return tasks.stream()
                .filter(task -> task.getID() == id)
                .findFirst().get();
    }
    /**
     * Sets the task completed.
     * @param task task that will be completed.
     */
    void setCompleted(Task task) {
        currentTasks.remove(task);
        completedTasks.add(task);
    }
    /**
     * Gets the current tasks.
     * @return list of the current tasks.
     */
    public Vector<Task> getCurrentTasks() {
        return currentTasks;
    }
    /**
     * Sets the current tasks.
     * @param currentTasks list of the current tasks.
     */
    public void setCurrentTasks(Vector<Task> currentTasks) {
        this.currentTasks = currentTasks;
    }
    /**
     * Gets the completed tasks.
     * @return list of the completed tasks.
     */
    public Vector<Task> getCompletedTasks() {
        return completedTasks;
    }
    /**
     * Sets the completed tasks.
     * @param completedTasks list of the completed tasks.
     */
    public void setCompletedTasks(Vector<Task> completedTasks) {
        this.completedTasks = completedTasks;
    }
    /**
     * Reloads lists of the tasks.
     * Invokes when the user closed the app and then opened the app again.
     * Checks if there are already completed tasks among the current tasks.
     */
    public void reload() {
        if(currentTasks != null) {
            if (!currentTasks.isEmpty()) {
                Iterator iterator = currentTasks.listIterator();
                while (iterator.hasNext()) {
                    Task t = (Task) iterator.next();
                    long delta = t.getDate().getTime() - Calendar.getInstance().getTimeInMillis();
                    if (delta <= 0) {
                        iterator.remove();
                        completedTasks.add(t);
                    }
                }
            }
        }
    }

    /**
     * Gets tasks.
     * @return list of the tasks.
     */
   Vector<Task> getTasks() {
       if (Main.CURRENT == Main.COMPLETED) return completedTasks;
       else return currentTasks;
    }
}
