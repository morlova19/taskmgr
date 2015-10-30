package observer;

import model.Task;

public interface ListObserver {
    void update();
    void update(Task task);
}
