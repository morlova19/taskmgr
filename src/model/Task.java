package model;


import java.io.Serializable;
import java.util.Date;

/**
 * Задача для выполнения.
 * Имеет 4 параметра: имя, описание, дата выполнения, контакты.
 */

public class Task {
    /**
     * Имя задачи.
     */
    private String name;
    /**
     * Описание задачи.
     */
    private String description;
    /**
     * Время выполнения задачи.
     */
    private Date date;
    /**
     * Контакты.
     */
    private String contacts;

    /**
     * Создает задачу с заданными параметрами.
     * @param name имя
     * @param description описание
     * @param date дата
     * @param contacts контакты
     */
    public Task(String name, String description, Date date, String contacts) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.contacts = contacts;
    }

    /**
     * Возвращает имя задачи.
     * @return имя задачи.
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает описание задачи.
     * @return описание.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Возвращает дату выполнения.
     * @return дата.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Возвращает контакты.
     * @return контакты.
     */
    public String getContacts() {
        return contacts;
    }

    /**
     * Устанавливает новую дату выполнения задачи.
      * @param date новая дата выполнения.
     */
    public void setDate(Date date) {
        this.date = date;
    }
}
