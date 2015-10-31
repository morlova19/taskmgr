package forms;

import controller.IController;
import model.IModel;
import model.Task;
import observer.ListObserver;
import start.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * StartForm - графический интерфейс для отображения, добавления и удаления задач.
 * Экземпляр этого класса отображается при запуске приложения.
 */
public class StartForm extends JFrame implements ActionListener, MouseListener, ListObserver {
    /**
     * Отображает список задач.
     * По умолчанию, отображает список текущих задач.
     */
    private JList taskList;
    /**
     * Кнопка для вызова окна добавления задачи.
     */
    private JButton addButton;
    /**
     * Компонент для удаления задачи.
     */
    private JButton deleteButton;
    /**
     * Компонент для указания, какой список задач будет отображаться.
     * Если значение свойства isSelected равно true, то отображается список текущих задач.
     * По умолчанию, значения свойства isSelected равно true.
     */
    private JRadioButton current_rb;
    /**
     * Если значение свойства isSelected равно true, то отображается список завершенных задач.
     */
    private JRadioButton completed_rb;
    /**
     * Контейнер для компонент.
     */
    private JPanel mainPanel;
    /**
     * Управляет формой.
     */
    IController c ;
    /**
     * Предоставляет данные для отображения.
     */
    IModel m;
    /**
     * Создает и отображает форму.
     * Инициализирует IController и IModel.
     * @param c контроллер
     * @param m модель
     * @throws HeadlessException
     */
    public StartForm(IController c, IModel m) throws HeadlessException {
        setContentPane(mainPanel);
        setTitle("Task manager");
        addButton.addActionListener(this);
        deleteButton.addActionListener(this);
        taskList.addMouseListener(this);
        current_rb.addActionListener(this);
        completed_rb.addActionListener(this);

        this.c = c;
        this.m = m;
        m.registerListObserver(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }

    /**
     * Вызывается, когда произошло действие.
     * @param e событие.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == current_rb)
        {
            addButton.setEnabled(true);
            Main.CURRENT = Main.NOTCOMPLETED;
            c.load();
        }
        else if (e.getSource() == completed_rb)
        {
            addButton.setEnabled(false);
            Main.CURRENT = Main.COMPLETED;
            c.load();
        }
        else if(e.getSource() == addButton)
        {
            new CreateTaskDialog(c).setVisible(true);
        }
        else if(e.getSource() == deleteButton)
        {
            int index = taskList.getSelectedIndex();
            if(index != -1)
            {
                int option = JOptionPane.showConfirmDialog(getContentPane(),"Are you sure?", " Delete", JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.YES_OPTION) {
                    c.delete(index);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(getContentPane(),"Please, select task");
            }
        }
    }

    /**
     * Вызывается, когда кликнули на мышь.
     * @param e событие мыши.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == taskList && e.getClickCount() == 2)
        {
            int index = taskList.getSelectedIndex();
            ShowTaskDialog dialog = new ShowTaskDialog(c, m);
            c.show(index);
            dialog.setEnabled(true);
        }
    }
    /**
     * Обрабатывает событие нажатия на мышь.
     * @param e событие.
     */
    @Override
    public void mousePressed(MouseEvent e) {

    }
    /**
     *
     * @param e событие.
     */
    @Override
    public void mouseReleased(MouseEvent e) {

    }
    /**
     *
     * @param e event
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    /**
     *
     * @param e event
     */
    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * Обновляет список задач на форме.
     * Вызывается, когда произошли какие-либо изменения, например, добавление задачи.
     */
    @Override
    public void update() {
        if(m != null)
        {
            ArrayList<String> tasks = m.getData();
            if(tasks != null) {
                DefaultListModel listModel = new DefaultListModel();
                for (String s : tasks) {
                    listModel.addElement(s);
                }
                taskList.setModel(listModel);
            }
        }
    }

    /**
     * Показывает диалоговое окно с сообщением о выполнении задачи.
     * @param task задача, которая произошла.
     */
    @Override
    public void update(Task task) {
        if(m != null)
        {
            String message = "Name: " + task.getName() + "\n" +
                    "Description : " + task.getDescription() +
                    "\n" + "Contacts :" + task.getContacts();
            String[] options = {"delay", "finish"};

            int answer = JOptionPane.showOptionDialog(new JFrame(), message,
                    "Notification",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[1]);
            if(answer == JOptionPane.YES_OPTION)
            {
                c.delay(task);
            }
            else {
                c.load();
            }
        }
    }


}
