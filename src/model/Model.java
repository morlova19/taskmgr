package model;

import observer.ListObserver;
import observer.MessageObserver;
import observer.TaskObserver;

import java.util.ArrayList;
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

    public Model(JournalManager manager) {
        this.manager = manager;
        load();
        notifyListObserver();
        nSystem = new NotificationSystem(journal);
        nSystem.registerObserver(this);
    }

    public void add(String name, String desc, Date date, String contacts) {
        journal.addTask(new Task(name, desc, date, contacts));
        notifyListObserver();
    }

    public void delete(int id) {
        Task t = journal.getTask(id);
        journal.deleteTask(t);
        notifyListObserver();
        nSystem.cancelTask(t);
    }

    public void update(Task task) {

        notifyListObserver(task);
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

    public void load() {
        journal = manager.readJournal();
    }

    public void delay(Task t) {
        journal.delayTask(t);
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


}
