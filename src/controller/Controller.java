package controller;

import forms.MessageDialog;
import forms.StartForm;
import model.IModel;
import model.Task;
import ns.INotificationSystem;
import observer.TaskObserver;
import start.Main;
import to.TransferObject;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.util.Date;
import java.util.Vector;

public class Controller implements IController, TaskObserver {
    /**
     * Starting GUI of application.
     */
    private StartForm startForm;
    /**
     * Model that works with data.
     */
    private IModel model;

    private INotificationSystem nSystem;
    
    private TaskObserver mObserver;
    /**
     * Constructs new controller.
     * Creates and displays GUI.
     * @param model model.
     */
    public Controller(IModel model, INotificationSystem nSystem)  {
        if(model != null)
        {
            this.model = model;
        }
        if(nSystem != null) {
            this.nSystem = nSystem;
            nSystem.registerObserver(this);
            int tempCurrent = Main.CURRENT;
            Main.CURRENT = Main.NOTCOMPLETED;
            Vector<Integer> currentTasksID = model.getIDs();
            Main.CURRENT = tempCurrent;
            for(int i: currentTasksID) {
                nSystem.startTask(i);
            }
        }
        startForm = new StartForm(this,model);
        load();
        startForm.setVisible(true);
    }

    @Override
    public void add(TransferObject data) {
        Task t = new Task(data);
        try {
            model.add(t);
        } catch (TransformerException | ParserConfigurationException e) {
            displayErrorMessage();
        }
        finally {
            nSystem.startTask(t.getID());
        }
    }

    @Override
    public void delete(int id)
    {
        try {
            model.delete(id);

        } catch (TransformerException | ParserConfigurationException e) {
            displayErrorMessage();
        }
        finally {
            if(Main.CURRENT == Main.NOTCOMPLETED) {
                nSystem.cancelTask(id);
            }
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
    public void delay(int id, Date newDate) {
        Task t = model.get(id);
        try {
            model.delay(id, newDate);
        } catch (TransformerException | ParserConfigurationException e) {
            displayErrorMessage();
        }
        finally {
            nSystem.delayTask(t.getID());
        }
    }
    @Override
    public void complete(int id)
    {
        try {
            model.complete(id);
        } catch (TransformerException | ParserConfigurationException e) {
            displayErrorMessage();
        }
        finally {
            nSystem.cancelTask(id);
        }
    }

    @Override
    public Date getTaskDate(int id) {
        return model.get(id).getDate();
    }

    @Override
    public void update(int id) {
        mObserver = new MessageDialog(this,model);
        mObserver.update(id);
    }

    /**
     * Displays error message.
     */
    private void displayErrorMessage()
    {
        JOptionPane.showMessageDialog(new JFrame(), "cannot write into the file");
    }
}
