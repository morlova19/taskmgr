package ns;

import controller.IController;
import observer.TaskObserver;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * Part of taskmgr.
 */
public class CustomNotificationSystem implements INotificationSystem, Runnable{

    private TaskObserver observer;
    private IController controller;
    private Map<Integer, Long> map;
    private Thread thread;

    public CustomNotificationSystem() {
        map = new HashMap<>();
        thread = new Thread(this);
    }

    @Override
    public void setController(IController controller) {
        this.controller = controller;
    }

    @Override
    public void startTask(int id) {
        synchronized (map) {
            map.put(id, controller.getTaskDate(id).getTime());
        }
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
        synchronized (map) {
            map.remove(id);
        }
        switch (thread.getState()) {
            case TERMINATED:
                thread = new Thread(this);
            case NEW:
                thread.start();
        }
    }
    @Override
    public void delayTask(int id) {
        synchronized (map) {
            map.remove(id);
        }
        startTask(id);
    }
    @Override
    public void registerObserver(TaskObserver o) {
        observer = o;
    }

    private void checkTimer() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        synchronized (map) {
            for (Integer id : map.keySet()) {
                if (map.get(id) < currentTime) {
                    observer.update(id);
                    thread.interrupt();
                }
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
