package model;

import start.Main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 * Part of taskmgr.
 */
public class Journal implements Serializable {

    private Vector<Task> currentTasks;
    private Vector<Task> completedTasks;


    Task getTask(int index) {
        if (Main.CURRENT == Main.COMPLETED) return completedTasks.elementAt(index);
        else return currentTasks.elementAt(index);
        //return new Task(currentTasks.elementAt(index));
    }

    void addTask(Task newTask) {
        //if (Main.CURRENT == Main.COMPLETED) completedTasks.add(newTask);
        //else
            currentTasks.add(newTask);
        //currentTasks.add(new Task(newTask)); // возможно, лишнее копирование
    }

    void deleteTask(Task task) {
        if (Main.CURRENT == Main.COMPLETED) completedTasks.remove(task);
        else currentTasks.remove(task);
    }

    void delayTask(Task task) {             // откладывает задачу на одну минуту
        Date date = task.getDate();
        date.setTime(date.getTime()+60000);
        task.setDate(date);
    }

    void setCompleted(Task task) {
        currentTasks.remove(task);
        completedTasks.add(task);
    }

    Vector<Task> getCurrentTasks() {
        return currentTasks;
    }

    void setCurrentTasks(Vector<Task> currentTasks) {
        this.currentTasks = currentTasks;
    }

    Vector<Task> getCompletedTasks() {
        return completedTasks;
    }

    void setCompletedTasks(Vector<Task> completedTasks) {
        this.completedTasks = completedTasks;
    }

    /*void reload() {   // для чего?

    }*/

    ArrayList<String> getNames() {                   //Нужно следить за порядком следования
        ArrayList<String> names = new ArrayList<>();
        if (Main.CURRENT == Main.COMPLETED)
            for(Task task: completedTasks) {
                names.add(task.getName());
            }
        else
            for(Task task: currentTasks) {
                names.add(task.getName());
            }
        return names;
    }
}
