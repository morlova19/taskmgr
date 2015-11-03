package model;

import observer.MessageObserver;

import java.util.*;

/**
 * Part of taskmgr.
 */
public class NotificationSystem {
    private class NotifyTask extends TimerTask {
        private MessageObserver observer;
        private Task task;

        NotifyTask(Task t, MessageObserver o) {
            task = t;
            observer = o;
        }
        public void  run() {
            observer.update(task);
        }
    }
    private MessageObserver mObserver;
    private Map<Task, Timer> map;

    NotificationSystem(Journal journal) {
        map = new HashMap<>();
        Iterator iterator = journal.getCurrentTasks().iterator();
        while(iterator.hasNext())
        {
            startTask((Task) iterator.next());
        }
    }

    void startTask(Task task) {
        Timer t = new Timer();
        map.put(task,t);
        NotifyTask nTask = new NotifyTask(task, mObserver);
        t.schedule(nTask, task.getDate());
    }

    void cancelTask(Task task) {
        Timer timer =  map.get(task);
        timer.cancel();
        map.remove(task);
    }

    public void delayTask(Task t)
    {
        Timer timer =  map.get(t);
        NotifyTask nTask = new NotifyTask(t, mObserver);
        timer.schedule(nTask, t.getDate());
    }
    void registerObserver(MessageObserver o) {
        mObserver = o;
    }


    /*void notifyObserver(Task task) {
        if (mObserver != null) mObserver.update(task);
    }*/
}
