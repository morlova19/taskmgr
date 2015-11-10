package ns;

import observer.TaskObserver;

import java.awt.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Part of taskmgr.
 */
public class CustomNotificationSystem implements INotificationSystem, Runnable{

    private TaskObserver observer;
    private ConcurrentHashMap<Integer, Long> map;
    private Thread thread;

    public CustomNotificationSystem() {
        map = new ConcurrentHashMap<>();
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void startTask(int id, Date time) {
        map.put(id, time.getTime());
    }
    @Override
    public void cancelTask(int id) {
        map.remove(id);
    }

    @Override
    public void delayTask(int id, Date time) {
        map.remove(id);
        startTask(id,time);
    }
    @Override
    public void registerObserver(TaskObserver o) {
        observer = o;
    }

    private void checkTimer() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        Iterator<Integer> iterator = map.keySet().iterator();
        if (iterator.hasNext()) {
            Integer id = iterator.next();
            while (iterator.hasNext() && (map.get(id) > currentTime)) {
                id = iterator.next();
            }
            if (map.get(id) <= currentTime) {
                observer.update(id);
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
    @Override
    public void run() {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        while(!Thread.currentThread().isInterrupted()) {
                checkTimer();
            try {
                Thread.currentThread().sleep(5000); //wait 5 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
