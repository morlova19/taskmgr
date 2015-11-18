package model;

import journal.Task;
import observer.ListObserver;
import observer.TaskObserver;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.util.Date;
import java.util.Vector;

/**
 * The Model provides methods for working with data, such as
 * adding, deleting and changing data.
 */
public interface IModel {
    /**
     * Adds data.
     * @param task data for adding.
     * @throws TransformerException thrown if something was wrong during the transformation process.
     * @throws ParserConfigurationException thrown if there was a serious configuration error.
     * @see TransformerException
     * @see ParserConfigurationException
     */
    void add(Task task) throws TransformerException, ParserConfigurationException;
    /**
     * Deletes data.
     * @param id identifier of task.
     * @throws TransformerException thrown if something was wrong during the transformation process.
     * @throws ParserConfigurationException thrown if there was a serious configuration error.
     * @see TransformerException
     * @see ParserConfigurationException
     */
    void delete(int id) throws TransformerException, ParserConfigurationException;
    /**
     * Gets list of data.
     * @return list of data.
     */
    Vector<Task> getData();
    /**
     * Registers ListObserver object.
     * @param o observer.
     * @see ListObserver
     */
    void registerListObserver(ListObserver o);
    /**
     * Registers TaskObserver object.
     * @param o observer.
     * @see TaskObserver
     */
    void registerTaskObserver(TaskObserver o);
    /**
     * Gets the task by identifier.
     * Returns null if the task was not found.
     * @param id identifier of the task.
     * @return task that was found.
     */
    Task get(int id);

    /**
     * Invokes when need to display task's details.
     * @param id task's identifier.
     */
    void show(int id);
    /**
     * Loads the list of the tasks.
     */
    void load();
    /**
     * Invokes for delaying the task.
     * @param id identifier of the task that will be delayed.
     * @param newDate new date of execution of the task.
     * @throws TransformerException thrown if something was wrong during the transformation process.
     * @throws ParserConfigurationException thrown if there was a serious configuration error.
     * @see TransformerException
     * @see ParserConfigurationException
     */
    void delay(int id, Date newDate) throws TransformerException, ParserConfigurationException;
    /**
     * Invokes to complete the task.
     * @throws TransformerException thrown if something was wrong during the transformation process.
     * @throws ParserConfigurationException thrown if there was a serious configuration error.
     * @see TransformerException
     * @see ParserConfigurationException
     */
    void complete(int id) throws TransformerException, ParserConfigurationException;

    Vector<Integer> getIDs();
}
