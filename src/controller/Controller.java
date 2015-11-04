package controller;

import forms.MessageDialog;
import forms.StartForm;
import model.IModel;
import model.Task;
import ns.NotificationSystem;
import observer.TaskObserver;
import start.Main;
import to.TransferObject;

import java.util.Calendar;
import java.util.Date;

public class Controller implements IController, TaskObserver {
    /**
     * Starting GUI of application.
     */
    private StartForm startForm;
    /**
     * Model that works with data.
     */
    private IModel model;

    private NotificationSystem nSystem;
    
    private TaskObserver mObserver;
    /**
     * Constructs new controller.
     * Creates and displays GUI.
     * @param model model.
     */
    public Controller(IModel model, NotificationSystem nSystem)  {
        if(model != null)
        {
            this.model = model;
        }
        startForm = new StartForm(this,model);
        load();
        if(nSystem != null) {
            this.nSystem = nSystem;
            nSystem.registerObserver(this);
        }
       // model.load();
        startForm.setVisible(true);
    }

    @Override
    public void add(TransferObject data) {
        Task t = new Task(data);
        model.add(t);
        nSystem.startTask(t.getID());
    }

    @Override
    public void delete(int id)
    {
        //Task t = model.get(id);
        model.delete(id);
        if(Main.CURRENT == Main.NOTCOMPLETED) {
            nSystem.cancelTask(id);
        }

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
    public void delay(Task t, Date newDate) {
        model.delay(t,newDate);
        nSystem.delayTask(t.getID());
    }
    @Override
    public void complete(Task t)
    {
        model.complete(t);
    }

    @Override
    public void update(int id) {
        mObserver = new MessageDialog(this,model);
        mObserver.update(id);
    }
}
