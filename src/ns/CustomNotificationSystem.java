package ns;

import observer.TaskObserver;

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
    }

    @Override
    public void startTask(int id, Date time) {
        map.put(id, time.getTime());
        if(!thread.isAlive()) {

            thread.start();
        }
        switch (thread.getState()) {
            case TERMINATED:
                thread = new Thread(this);
            case NEW:
                thread.start();
        }

    }
    @Override
    public void cancelTask(int id) {
        map.remove(id);
        switch (thread.getState()) {
            case TERMINATED:
                thread = new Thread(this);
            case NEW:
                thread.start();
        }
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
        for (Integer id : map.keySet()) {
            if (map.get(id) < currentTime) {
               observer.update(id);
            }
        }
    }
    @Override
    public void run() {
        thread.setPriority(Thread.MIN_PRIORITY);
        while(!thread.isInterrupted()) {
            checkTimer();
            try {
                thread.sleep(10000); //wait 10 seconds
            } catch (InterruptedException e) {
                thread.interrupt();
            }
        }
        thread = new Thread(this);
    }
}
