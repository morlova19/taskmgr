package model;

import journalmgr.JournalManager;
import observer.ListObserver;
import observer.TaskObserver;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
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
    }
    @Override
    public void add(Task task) throws TransformerException, ParserConfigurationException {
        if(task != null) {
            journal.addTask(task);
            manager.writeJournal(journal);
            notifyListObserver();
        }
    }

    @Override
    public void delete(int id) throws TransformerException, ParserConfigurationException {
        Task t = journal.getTask(id);
        if(t != null) {
            journal.deleteTask(t);
            manager.writeJournal(journal);
            notifyListObserver();
        }
    }

    @Override
    public Vector<Task> getData() {
        return journal.getTasks();
    }

    @Override
    public void registerListObserver(ListObserver o) {
        lObserver = o;
    }

    @Override
    public void registerTaskObserver(TaskObserver o) {
        tObserver = o;
    }

    @Override
    public Task get(int id) {
        return journal.getTask(id);
    }
    @Override
    public Task getCurrentTask(int id) {
        return journal.getCurrentTask(id);
    }

    @Override
    public void show(int id) {
        notifyTaskObserver(id);
    }

    @Override
    public void load() {
        notifyListObserver();
    }

    @Override
    public void delay(int id, Date newDate) throws TransformerException, ParserConfigurationException {
        journal.delayTask(id, newDate);
        manager.writeJournal(journal);
    }

    @Override
    public void complete(Task t) throws TransformerException, ParserConfigurationException {
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
