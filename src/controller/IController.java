package controller;

import model.Task;

import java.util.Date;

public interface IController {
    void add(String name, String desc, Date date, String contacts);
    void delete(int id);
    void load();
    void show(int id);
    void delay(Task t);
}
