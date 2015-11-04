package model;

import journalmgr.JournalManager;
import observer.ListObserver;
import observer.TaskObserver;

import java.util.Date;
import java.util.Vector;

/**
 * Part of taskmgr.
 */
public class Model implements IModel {

    private Journal journal;
    private JournalManager manager;
    private ListObserver lObserver;
    private TaskObserver tObserver;

    public Model(JournalManager manager, Journal journal) {
        this.manager = manager;
        this.journal = journal;
        load();
    }

    public void add(Task task) {
        journal.addTask(task);
        manager.writeJournal(journal);
        notifyListObserver();
    }

    public void delete(int id) {
        Task t = journal.getTask(id);
        if(t != null) {
            journal.deleteTask(t);
            manager.writeJournal(journal);
        }
        notifyListObserver();
    }

    public Vector<Task> getData() {
        return journal.getTasks();
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
        notifyListObserver();
    }
    @Override
    public void delay(Task t, Date newDate) {
        journal.delayTask(t, newDate);
        manager.writeJournal(journal);
    }
    @Override
    public void complete(Task t)
    {
        journal.setCompleted(t);
        manager.writeJournal(journal);
        notifyListObserver();
    }
    private void notifyListObserver() {
        if (lObserver != null) lObserver.update();
    }

    private void notifyTaskObserver(int id) {
        if (tObserver != null) tObserver.update(id);
    }


}
