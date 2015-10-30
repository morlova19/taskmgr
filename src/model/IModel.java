package model;

import observer.ListObserver;
import observer.TaskObserver;

import java.util.ArrayList;
import java.util.Date;

public interface IModel {
    void add(String name, String desc, Date date, String contacts);
    void delete(int id);
    ArrayList<String> getData();
    void registerListObserver(ListObserver o);
    void registerTaskObserver(TaskObserver o);
    Task get(int id);
    void show(int id);
    void load();
    void delay(Task t);
}
