package controller;

import forms.StartForm;
import model.IModel;
import model.Task;

import java.io.IOException;
import java.util.Date;

public class Controller implements IController {
    /**
     * Стартовый графический интерфейс приложения.
     */
    private StartForm startForm;
    /**
     * Модель, которая работает с данными.
     */
    private IModel model;

    /**
     * Создает экземпляр класса, инициализирует поля.
     * Запускает стартовую форму.
     * @param model модель.
     * @throws IOException ошибка ввода/вывода.
     */
    public Controller(IModel model) throws IOException {
        this.model = model;
        startForm = new StartForm(this,model);
        load();
       // model.load();
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
    public void load() throws IOException {
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
    @Override
    public void complete(Task t)
    {
        model.complete(t);
    }

}
