package model;

import observer.ListObserver;
import observer.TaskObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Интерфейс модели.
 * Предоставляет методы для работы с данными такие, как
 * добавление, удаление и изменение данных.
 * А также сообщает форме (своему наблюдателю) о том, что данные изменились.
 */
public interface IModel {
    /**
     * Добавление данных.
     * @param name имя.
     * @param desc описание.
     * @param date дата.
     * @param contacts контакты.
     */
    void add(String name, String desc, Date date, String contacts);
    /**
     * Удаление данных.
     * @param id номер задачи в списке задач.
     */
    void delete(int id);
    /**
     * Возвращает список имен задач.
     * @return список имен.
     */
    ArrayList<String> getData();
    /**
     * Метод для регистарции наблюдателя.
     * @param o наблюдатель.
     */
    void registerListObserver(ListObserver o);
    /**
     * Метод для регистарции наблюдателя.
     * @param o наблюдатель.
     */
    void registerTaskObserver(TaskObserver o);
    /**
     * Возвращает задачу по номеру из списка задач.
     * @param id номер задачи.
     * @return найденная задача.
     */
    Task get(int id);
    /**
     * Загружает информацию о задаче, полученной по номеру из списка.
     * @param id номер задачи, информация о которой будет загружена.
     */
    void show(int id);
    /**
     * Загружает список задач.
     */
    void load() throws IOException;
    /**
     * Вызывается, когда задачу нужно отложить.
     * @param t задача, которая откладывается.
     */
    void delay(Task t);
    /**
     * Вызывается, когда задачу нужно завершить.
     * @param t задача, которая завершена.
     */
    void complete(Task t);
}
