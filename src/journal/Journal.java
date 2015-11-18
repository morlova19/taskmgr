package journal;

import start.Main;

import java.io.Serializable;
import java.util.*;

/**
 * Part of taskmgr.
 */
public class Journal implements Serializable {
    /**
     * List of current tasks.
     */
    private Vector<Task> currentTasks = new Vector<>();
    /**
     * List of completed tasks.
     */
    private Vector<Task> completedTasks = new Vector<>();

    /**
     * Gets task by identifier.
     * @param id task's id.
     * @return task.
     */
    public Task getTask(int id) {

        for(Task t: completedTasks) {
            if(t.getID() == id) {
                return t;
            }
        }
        for(Task t: currentTasks) {
            if(t.getID() == id) {
                return t;
            }
        }
       return null;
    }

    /**
     * Adds task in list.
     * @param newTask new task.
     */
    void addTask(Task newTask) {
        if(currentTasks != null) {
            currentTasks.add(newTask);
        }
    }
    /**
     * Deletes task.
     * @param id identifier of task that will be deleted.
     */
    void deleteTask(int id) {
        for(Task t: completedTasks)
        {
            if(t.getID() == id)
            {
                completedTasks.remove(t);
                return;
            }
        }
        for(Task t: currentTasks)
        {
            if(t.getID() == id)
            {
                currentTasks.remove(t);
                return;
            }
        }
    }
    /**
     * Delays task.
     * @param id identifier of task that will be delayed.
     * @param newDate new date of task.
     */
    void delayTask(int id, Date newDate) {
        Task t = currentTasks
                .stream()
                .filter(task -> task.getID() == id)
                .findFirst().get();
        t.setDate(newDate);
    }

    /**
     * Makes task completed.
     * Removes from current tasks and adds into completed tasks.
     * @param task completed task.
     */
    void setCompleted(Task task) {
        currentTasks.remove(task);
        completedTasks.add(task);
    }

    /**
     * Gets list of current tasks.
     * @return current tasks.
     */
    public Vector<Task> getCurrentTasks() {
        return currentTasks;
    }

    /**
     * Sets list of current tasks.
     * @param currentTasks current tasks.
     */
    public void setCurrentTasks(Vector<Task> currentTasks) {
        this.currentTasks = currentTasks;
    }
    /**
     * Gets list of completed tasks.
     * @return completed tasks.
     */
    public Vector<Task> getCompletedTasks() {
        return completedTasks;
    }
    /**
     * Sets list of completed tasks.
     * @param completedTasks completed tasks.
     */
    public void setCompletedTasks(Vector<Task> completedTasks) {
        this.completedTasks = completedTasks;
    }

    /**
     * Checks if there are among the current problems already completed tasks.
     * If finds such tasks, makes them completed.
     */
    public void reload() {
        if(!currentTasks.isEmpty())
        {
            Iterator iterator = currentTasks.listIterator();
            while(iterator.hasNext())
            {
                Task t = (Task) iterator.next();
                long delta = t.getDate().getTime() - Calendar.getInstance().getTimeInMillis();
                if(delta <= 0)
                {
                    iterator.remove();
                    completedTasks.add(t);
                }
            }
        }
    }
    /**
     * Gets list of the task.
     * @return tasks.
     */
    Vector<Task> getTasks() {
       if (Main.CURRENT == Main.COMPLETED) return completedTasks;
       else return currentTasks;
    }

}
