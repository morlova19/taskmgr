package ns;

import observer.TaskObserver;

import java.awt.Toolkit;
import java.util.*;

public class NotificationSystem implements INotificationSystem{
    private class NotifyTask extends TimerTask {
        private TaskObserver observer;
        private int id;

        NotifyTask(int id, TaskObserver o) {
            this.id = id;
            this.observer = o;
        }
        public void  run() {
            Toolkit.getDefaultToolkit().beep();
            if(this.observer != null) {
                this.observer.update(id);
            }
        }
    }
    private TaskObserver mObserver;
    private Map<Integer, Timer> map;

    public NotificationSystem() {
        map = new HashMap<>();
    }
    @Override
    public void startTask(int id, Date time) {
        Timer t = new Timer();
        map.put(id,t);
        if(mObserver != null) {
            NotifyTask nTask = new NotifyTask(id, mObserver);
            t.schedule(nTask, time);
        }
    }
    @Override
    public void cancelTask(int id) {
        Timer timer = map.get(id);
        timer.cancel();
        map.remove(id);
    }

    @Override
    public void delayTask(int id, Date time) {
        Timer timer =  map.get(id);
        NotifyTask nTask = new NotifyTask(id, mObserver);
        timer.schedule(nTask, time);
    }

    @Override
    public void registerObserver(TaskObserver o) {
        this.mObserver = o;
    }
}
