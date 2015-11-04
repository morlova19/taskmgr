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
     * invoked when a button to add was pressed.
     *
     * Принимает введенные пользователем параметры новой задачи.
     */
    void add(TransferObject data);
    /**
     * Вызывается, если на форме была нажата кнопка удалить.
     * Принимает номер выбранное задачи в списке задач.
     * @param id номер задачи.
     */
    void delete(int id);
    /**
     * Вызывается, когда необходимо загрузить список задач в форму.
     */
    void load();
    /**
     * Вызывается, когда нужно отображить информацию о задаче.
     * @param id номер задачи в списке задач.
     */
    void show(int id);
    /**
     * Вызывается, когда нужно отложить задачу.
     * @param t задача, которую нужно отложить.
     */
    void delay(Task t, Date newDate);
    /**
     * Вызывается, когда нужно завершить задачу.
     * @param t задача, которую нужно завершить.
     */
    void complete(Task t);
}
