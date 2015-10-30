package controller;

import forms.StartForm;
import model.IModel;
import model.Task;

import java.util.Date;

public class Controller implements IController {
    private StartForm startForm;
    private IModel model;


    public Controller(IModel model) {
        this.model = model;
        startForm = new StartForm(this, model);
        model.load();
        startForm.setVisible(true);
    }
    @Override
    public void add(String name, String desc, Date date, String contacts) {
        model.add(name, desc, date, contacts);
    }
    @Override
    public void delete(int id)
    {
         model.delete(id);
    }

    @Override
    public void load() {
        model.load();
    }

    @Override
    public void show(int id) {
       model.show(id);
    }

    @Override
    public void delay(Task t) {
        model.delay(t);
    }


}
