package model;

import observer.MessageObserver;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

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

    private Map<Task, Timer> map;
    private MessageObserver mObserver;

    NotificationSystem(Journal journal) {
        Vector<Task> tasks = journal.getCurrentTasks();
        for (Task task: tasks) {
            startTask(task);
        }
    }

    void startTask(Task task) {
        Timer t = new Timer();
        NotifyTask nTask = new NotifyTask(task, mObserver);
        t.schedule(nTask, task.getDate());
    }

    void cancelTask(Task task) {
        map.get(task).cancel();
    }

    void registerObserver(MessageObserver o) {
        mObserver = o;
    }

    /*void notifyObserver(Task task) {
        if (mObserver != null) mObserver.update(task);
    }*/
}
