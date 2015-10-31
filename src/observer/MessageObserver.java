package observer;

import model.Task;

/**
 * Интерфейс наблюдателя.
 * Следит за исполнением задач.
 * Содержит единственный метод update.
 */
public interface MessageObserver {
    /**
     * Вызывается, когда задача произошла.
     * @param t задача, которая произошла.
     */
    void update(Task t);
}
