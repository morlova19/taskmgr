package controller;

import to.TransferObject;

import java.util.Date;

/**
 * Interface of controller.
 * Controller provides methods to work with data for view.
 * It invokes methods of the Model and transfers the entered data to it.
 */
public interface IController {
    /**
     * Invokes when a button to add was clicked.
     * @param data object that contains parameters of new data.
     */
    void add(TransferObject data);
    /**
     * Invokes when a button to delete was clicked.
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
     * @param id identifier of the task that will be completed.
     */
    void complete(int id);
}
