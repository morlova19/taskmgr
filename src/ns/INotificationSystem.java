package ns;

import observer.TaskObserver;

import java.util.Date;

/**
 * Interface of notification system.
 * A timer. Notifies observer when
 * the task's time comes.
 */
public interface INotificationSystem {

    /**
     * Registers the task with identifier @param id
     * in the check list
     * @param id task's identifier.
     * @param time task's time.
     */
    void startTask(int id, Date time);
    /**
     * Deletes the task with identifier @param id
     * from the check list
     */
    void cancelTask(int id);
    /**
     * Updates the time of task with identifier @param id
     * and registers in the check list
     * @param id task's identifier.
     * @param time new task's time.
     */
    void delayTask(int id, Date time);
    /**
     * Registers an observer which would be notified
     */
    void registerObserver(TaskObserver o);
}
