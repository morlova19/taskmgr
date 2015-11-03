package model;

import observer.ListObserver;
import observer.MessageObserver;
import observer.TaskObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Part of taskmgr.
 */
public class Model implements IModel, MessageObserver {

    private Journal journal;
    private JournalManager manager;
    private NotificationSystem nSystem;
    private ListObserver lObserver;
    private TaskObserver tObserver;

    public Model(JournalManager manager) throws IOException {
        this.manager = manager;
        journal = manager.readJournal();
        journal.reload();
        load();
       // notifyListObserver();
        nSystem = new NotificationSystem(journal);
        nSystem.registerObserver(this);
    }

    public void add(String name, String desc, Date date, String contacts) {
        Task task = new Task(name, desc, date, contacts);
        journal.addTask(task);
        try {
            manager.writeJournal(journal);
        } catch (IOException e) {
           // e.printStackTrace();
        }
        nSystem.startTask(task);
        notifyListObserver();
    }

    public void delete(int id) {
        Task t = journal.getTask(id);
        journal.deleteTask(t);
        try {
            manager.writeJournal(journal);
        } catch (IOException e) {

        }
        long delta = t.getDate().getTime() - Calendar.getInstance().getTimeInMillis();
        if(delta > 0) {
            nSystem.cancelTask(t);
        }
        notifyListObserver();
    }

    public ArrayList<String> getData() {
        return journal.getNames();
    }

    public void registerListObserver(ListObserver o) {
        lObserver = o;
    }

    public void registerTaskObserver(TaskObserver o) {
        tObserver = o;
    }

    public Task get(int id) {
        return journal.getTask(id);
    }

    public void show(int id) {
        notifyTaskObserver(id);
    }
    public void load() throws IOException {
        notifyListObserver();
    }
    @Override
    public void delay(Task t) {
        journal.delayTask(t);
        try {
            manager.writeJournal(journal);
        } catch (IOException e) {

        }
        nSystem.delayTask(t);
    }
    @Override
    public void complete(Task t)
    {
        journal.setCompleted(t);
        try {
            manager.writeJournal(journal);
        } catch (IOException e) {

        }
        notifyListObserver();
    }
    private void notifyListObserver() {
        if (lObserver != null) lObserver.update();
    }

    private void notifyListObserver(Task task) {
        if (lObserver != null) lObserver.update(task);
    }

    private void notifyTaskObserver(int id) {
        if (tObserver != null) tObserver.update(id);
    }

    public void update(Task task) {

        notifyListObserver(task);
    }

}
