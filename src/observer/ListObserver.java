package observer;

import model.Task;
/**
 * Интерфейс наблюдателя.
 * Оповещение наблюдателю приходит от наблюдаемого объекта
 * в случаем, если со списком задач произошли изменения,
 * а также при наступлении задачи.
 */
public interface ListObserver {
    void update();
    void update(Task task);
}
