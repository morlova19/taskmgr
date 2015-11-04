package model;

import observer.ListObserver;
import observer.TaskObserver;
import to.TransferObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 * The Model provides methods for working with data, such as
 * adding, deleting and changing data.
 */
public interface IModel {
    /**
     * Adding data.
     * @param task data for adding.
     */
    void add(Task task);
    /**
     * Deleting data.
     * @param id id of task.
     */
    void delete(int id);
    /**
     * Gets list of data
     * @return list of strings.
     */
    Vector<Task> getData();
    /**
     * Method to register ListObserver object.
     * @param o observer.
     * @see ListObserver
     */
    void registerListObserver(ListObserver o);
    /**
     * Method to register TaskObserver object.
     * @param o observer.
     * @see TaskObserver
     */
    void registerTaskObserver(TaskObserver o);
    /**
     * Gets the task by identifier.
     * @param id identifier of the task.
     * @return task that was found.
     */
    Task get(int id);
    /**
     * Loads information about the task which has the given identifier.
     * @param id identifier of the task.
     */
    void show(int id);
    /**
     * Loads the list of the tasks.
     */
    void load();
    /**
     * Invokes for delaying the task.
     * @param t task that will be delayed.
     * @param newDate new date of execution of the task.
     */
    void delay(Task t, Date newDate);
    /**
     * Invokes to complete the task.
     * @param t task that will be complered.
     */
    void complete(Task t);
}
