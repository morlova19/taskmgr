package observer;

/**
 * Interface of observer.
 * Notifications come to observer from observable object
 * when some actions should be done with the task.
 * For example, displaying information of the task or
 * need to make notification about the task.
 */
public interface TaskObserver {
    /**
     * Makes some action with the task.
     * @param id identifier of the task.
     */
    void update(int id);
}
