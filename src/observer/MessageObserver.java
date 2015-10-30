package observer;

import model.Task;

public interface MessageObserver {
    void update(Task t);
}
