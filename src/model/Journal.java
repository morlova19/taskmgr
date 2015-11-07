package model;

import start.Main;

import java.io.Serializable;
import java.util.*;

/**
 * Part of taskmgr.
 */
public class Journal implements Serializable {

    private Vector<Task> currentTasks = new Vector<>();
    private Vector<Task> completedTasks = new Vector<>();

    public Task getTask(int index) {

        if (Main.CURRENT == Main.COMPLETED)
        {
            return completedTasks.stream()
                    .filter(task -> task.getID() == index)
                    .findFirst().get();
        }
        else
        {
            return currentTasks.stream()
                    .filter(task -> task.getID() == index)
                    .findFirst().get();
        }
    }

    void addTask(Task newTask) {
        if(currentTasks != null) {
            currentTasks.add(newTask);
        }
    }

    void deleteTask(Task task) {
        if (Main.CURRENT == Main.COMPLETED) completedTasks.remove(task);
        else currentTasks.remove(task);
    }

    void delayTask(int id, Date newDate) {// откладывает задачу на одну минуту
        Task t = currentTasks
                .stream()
                .filter(task -> task.getID() == id)
                .findFirst().get();
        t.setDate(newDate);
    }

    void setCompleted(Task task) {
        currentTasks.remove(task);
        completedTasks.add(task);
    }

    public Vector<Task> getCurrentTasks() {
        return currentTasks;
    }

    public void setCurrentTasks(Vector<Task> currentTasks) {
        this.currentTasks = currentTasks;
    }

    public Vector<Task> getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(Vector<Task> completedTasks) {
        this.completedTasks = completedTasks;
    }

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

   Vector<Task> getTasks() { //Нужно следить за порядком следования
       if (Main.CURRENT == Main.COMPLETED) return completedTasks;
       else return currentTasks;
    }
}
