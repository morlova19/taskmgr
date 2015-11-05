package controller;

import model.Task;
import to.TransferObject;

import java.io.IOException;
import java.util.Date;

/**
 * Interface of controller.
 * Controller controls the work with form and model.
 */
public interface IController {
    /**
     * Invokes when a button to add was pressed.
     *@param data object that contains parameters of new data.
     */
    void add(TransferObject data);
    /**
     * Invokes when a button to delete was pressed.
     * @param id identifier of the task.
     */
    void delete(int id);
    /**
     * Invokes to display the list of the tasks.
     */
    void load();
    /**
     * Invokes to display the details of the task.
     * @param id identifier of the task.
     */
    void show(int id);
    /**
     * Invokes to delay the task.
     * @param id identifier of the task that will be delayed.
     */
    void delay(int id, Date newDate);
    /**
     * Invokes to complete the task.
     * @param t identifier of the task that will be completed.
     */
    void complete(Task t);
}
