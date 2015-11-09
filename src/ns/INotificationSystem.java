package ns;


import controller.IController;
import observer.TaskObserver;

/**
 * Interface of notification system.
 * A timer. Notifies observer when
 * the task's time comes.
 */
public interface INotificationSystem {

    /**
     * Sets the link to controller connected
     * with this notification system
     */
    public void setController(IController controller);
    /**
     * Registers the task with identifier @param id
     * in the check list
     */
    public void startTask(int id);

    /**
     * Deletes the task with identifier @param id
     * from the check list
     */
    public void cancelTask(int id);

    /**
     * Updates the time of task with identifier @param id
     * and registers in the check list
     */
    public void delayTask(int id);

    /**
     * Registers an observer which would be notified
     */
    public void registerObserver(TaskObserver o);
}
