package ns;

import model.Journal;
import observer.TaskObserver;

import java.util.*;

/**
 * Part of taskmgr.
 */
public class NotificationSystem { //test
    private class NotifyTask extends TimerTask {
        private TaskObserver observer;
        private int id;

        NotifyTask(int id, TaskObserver o) {
            this.id = id;
            this.observer = o;
        }
        public void  run() {
            if(this.observer != null) {
                this.observer.update(id);
            }
        }
    }
    private TaskObserver mObserver;
    private Map<Integer, Timer> map;
    private Journal journal;

    public NotificationSystem(Journal journal) {
        map = new HashMap<>();
        this.journal = journal;
    }

    public void startAllCurrentTasks() {
        if(this.journal != null) {
            this.journal.getCurrentTasks()
                    .stream()
                    .forEach(t -> startTask(t.getID()));
        }
    }

    public void startTask(int id) {
        Timer t = new Timer();
        map.put(id,t);
        if(mObserver != null) {
            NotifyTask nTask = new NotifyTask(id, mObserver);
            t.schedule(nTask, journal.getTask(id).getDate());
        }
    }

    public void cancelTask(int id) {
        System.out.println("id deleted task = " + id);
        Timer timer = map.get(id);
        timer.cancel();
        map.remove(id);
    }

    public void delayTask(int id)
    {
        Timer timer =  map.get(id);
        System.out.println("id delay task = " + id);
        NotifyTask nTask = new NotifyTask(id, mObserver);
        timer.schedule(nTask, journal.getTask(id).getDate());
    }
    public void registerObserver(TaskObserver o) {
        this.mObserver = o;
    }
}
