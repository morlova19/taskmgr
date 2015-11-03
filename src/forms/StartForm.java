package forms;

import controller.IController;
import model.IModel;
import model.Task;
import observer.ListObserver;
import start.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Графический интерфейс для отображения, добавления и удаления задач.
 * Экземпляр этого класса отображается при запуске приложения.
 */
public class StartForm extends JFrame implements ActionListener, MouseListener, WindowListener, ListObserver {
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
     * Компонент для удаления выбранной задачи.
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
     * Контейнер для компонентов формы.
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
     * Системный трей.
     */
    private SystemTray tray;
    /**
     * Иконка, которая будет отображаться в системной трее.
     */
    private TrayIcon trayIcon;
    /**
     * Элемент меню для открытия формы.
     */
    private MenuItem openItem;
    /**
     * Элемент меню для закрытия приложения.
     */
    private MenuItem exitItem;
    /**
     * Создает и отображает форму.
     * Инициализирует поля IController и IModel.
     * @param c контроллер
     * @param m модель
     * @throws HeadlessException будет брошено, когда код, который зависит от клавиатуры, дисплея или мыши
     * вызывается в среде, которая не поддерживает клавиатуру, дисплей или мышь.
     * @throws IOException будет брошено, если произошла ошибка ввода/вывода.
     */
    public StartForm(IController c, IModel m) throws HeadlessException, IOException {

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

        if(SystemTray.isSupported())
        {
            tray = SystemTray.getSystemTray();
            Image img = ImageIO.read(new File("images/bulb.gif"));
            PopupMenu popupMenu = new PopupMenu();
            exitItem = new MenuItem("Exit");
            openItem = new MenuItem("Open");
            popupMenu.add(openItem);
            popupMenu.add(exitItem);
            exitItem.addActionListener(this);
            openItem.addActionListener(this);
            trayIcon = new TrayIcon(img, "Task manager",popupMenu);
            trayIcon.setImageAutoSize(true);
        }
        addWindowListener(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }

    /**
     * Вызывается, когда произошло действие.
     * Например, была нажата кнопка Добавить, Удалить и т.д.
     * @param e событие.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == current_rb)
        {
            addButton.setEnabled(true);
            Main.CURRENT = Main.NOTCOMPLETED;
            try {
                c.load();
            } catch (IOException e1) {

            }
        }
        else if (e.getSource() == completed_rb)
        {
            addButton.setEnabled(false);
            Main.CURRENT = Main.COMPLETED;
            try {
                c.load();
            } catch (IOException e1) {

            }
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
        else if(e.getSource() == openItem)
        {
            setVisible(true);
            tray.remove(trayIcon);
        }
        else if(e.getSource() == exitItem)
        {
            System.exit(0);
        }
    }

    /**
     * Вызывается, когда кнопка мыши была нажата (нажата и отпущена) на компоненте.
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
     * Вызывается, когда кнопка мыши была нажата на компоненте.
     * @param e событие.
     */
    @Override
    public void mousePressed(MouseEvent e) {

    }
    /**
     * Вызывается, когда кнопка мыши была отпущена на компоненте.
     * @param e событие.
     */
    @Override
    public void mouseReleased(MouseEvent e) {

    }
    /**
     * Вызывается, когда курсор мыши был наведен на компонент.
     * @param e событие
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    /**
     * Вызывается, когда курсор мыши был убран с компонента.
     * @param e событие
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
     * Отображает диалоговое окно о выполнении задачи.
     * Предоставляет возможность завершить или отложить задачу.
     * @param task задача, которая произошла.
     */
    @Override
    public void update(Task task) {
        if(m != null)
        {
            String message = "Name: " + task.getName() + "\n" +
                    "Description : " + task.getDescription() +
                    "\n" + "Contacts :" + task.getContacts();
            String[] options = {"delay", "complete"};

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
                c.complete(task);
            }
        }
    }

    /**
     * Вызывается, когда окно видимо.
     * @param e
     */
    @Override
    public void windowOpened(WindowEvent e) {

    }
    /**
     * Вызывается при закрытии окна.
     * @param e событие
     */
    @Override
    public void windowClosing(WindowEvent e) {
       /* try {
            tray.add(trayIcon);
        } catch (AWTException e1) {

        }
        setVisible(false);*/
    }
    /**
     * Вызывается, когда окно закрыто.
     * @param e событие
     */
    @Override
    public void windowClosed(WindowEvent e) {

    }
    /**
     * Вызывается при сворачивании окна.
     * @param e событие
     */
    @Override
    public void windowIconified(WindowEvent e) {
        try {
            tray.add(trayIcon);
        } catch (AWTException e1) {

        }
        setVisible(false);
    }
    /**
     * Вызывается при возвращении окна из свернутого состояния в нормальное.
     * @param e событие
     */
    @Override
    public void windowDeiconified(WindowEvent e) {

    }
    /**
     * Вызывается, когда окно активно.
     * @param e
     */
    @Override
    public void windowActivated(WindowEvent e) {

    }
    /**
     * Вызывается, когда окно не активно.
     * @param e
     */
    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
