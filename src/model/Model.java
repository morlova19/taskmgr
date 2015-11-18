package model;

import journal.JournalManager;
import journal.Task;
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

    private JournalManager manager;
    private ListObserver lObserver;
    private TaskObserver tObserver;

    public Model(JournalManager manager) {
        this.manager = manager;
    }
    @Override
    public void add(Task task) throws TransformerException, ParserConfigurationException {
        if(task != null) {
            manager.add(task);
            notifyListObserver();
        }
    }
    @Override
    public void delete(int id) throws TransformerException, ParserConfigurationException {
        manager.delete(id);
        notifyListObserver();
    }
    @Override
    public Vector<Task> getData() {
        return manager.getTasks();
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
        return manager.get(id);
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
        manager.delay(id, newDate);
    }

    @Override
    public void complete(int id) throws TransformerException, ParserConfigurationException {
        manager.complete(id);
        notifyListObserver();

    }
    @Override
    public Vector<Integer> getIDs() {
        return manager.getIDs();
    }

    private void notifyListObserver() {
        if (lObserver != null) lObserver.update();
    }

    private void notifyTaskObserver(int id) {
        if (tObserver != null) tObserver.update(id);
    }


}
