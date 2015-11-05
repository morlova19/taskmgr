package observer;
/**
 * Interface of observer.
 * Notifications come to observer from observable object
 * when some changes were done with the list of the tasks.
 */
public interface ListObserver {
    /**
     * Invoked when changes occurred and
     * need to update the list of tasks on the form.
     */
    void update();
}
