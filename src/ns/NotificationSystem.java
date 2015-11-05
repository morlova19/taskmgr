package ns;

import model.Journal;
import model.Task;
import observer.TaskObserver;

import java.util.*;

/**
 * Part of taskmgr.
 */
public class NotificationSystem {
    private class NotifyTask extends TimerTask {
        private TaskObserver observer;
        private int id;

        NotifyTask(int id, TaskObserver o) {
            this.id = id;
            observer = o;
        }
        public void  run() {
            observer.update(id);
        }
    }
    private TaskObserver mObserver;
    private Map<Integer, Timer> map;
    private Journal journal;

    public NotificationSystem(Journal journal) {
        map = new HashMap<>();
        this.journal = journal;
       /* Iterator iterator = journal.getCurrentTasks().iterator();
        while(iterator.hasNext())
        {
            startTask(((Task) iterator.next()).getID());
        }*/
        journal.getCurrentTasks()
                .parallelStream()
                //.stream()
                .forEach(t -> startTask(t.getID()));
    }

    public void startTask(int id) {
        Timer t = new Timer();
        map.put(id,t);
        NotifyTask nTask = new NotifyTask(id, mObserver);
        t.schedule(nTask, journal.getTask(id).getDate());
    }

    public void cancelTask(int id) {
            Timer timer = map.get(id);
            timer.cancel();
            map.remove(id);
    }

    public void delayTask(int id)
    {
        Timer timer =  map.get(id);
        NotifyTask nTask = new NotifyTask(id, mObserver);
        timer.schedule(nTask, journal.getTask(id).getDate());
    }
    public void registerObserver(TaskObserver o) {
        mObserver = o;
    }
}
