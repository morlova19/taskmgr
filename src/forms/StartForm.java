package forms;

import controller.IController;
import listeners.CustomMouseListener;
import listeners.CustomWindowListener;
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
import java.util.Vector;

/**
 * GUI to display list of the tasks.
 */
public class StartForm extends JFrame implements ActionListener, CustomMouseListener, CustomWindowListener, ListObserver {
    /**
     * Displays list of the tasks.
     * By default list of the current tasks.
     */
    private JList taskList;
    /**
     * Button to open the window to add a new task.
     */
    private JButton addButton;
    /**
     * Button for deleting the selected task..
     */
    private JButton deleteButton;
    /**
     * Component to specify a list of tasks to be displayed.
     * If the value of property isSelected is true, then the current tasks will be displayed.
     * The value of property isSelected is true by default;
     */
    private JRadioButton current_rb;
    /**
     * Component to specify a list of tasks to be displayed.
     * If the value of property isSelected is true, then the completed tasks will be displayed.
     */
    private JRadioButton completed_rb;
    /**
     * Container for components of this form.
     */
    private JPanel mainPanel;
    /**
     * Controls this form.
     */
    IController c ;
    /**
     * Provides the data for displaying.
     */
    IModel m;
    /**
     * System tray.
     */
    private SystemTray tray;
    /**
     * Icon that will be displayed in system tray.
     */
    private TrayIcon trayIcon;
    /**
     * Menu item to open form.
     */
    private MenuItem openItem;
    /**
     * Menu item to exit app.
     */
    private MenuItem exitItem;

    /**
     * Array in which index this is number of the task in JList
     * and pairs[index] this is identifier of the task.
     */
    private int[] pairs;
    /**
     * Creates and displays form.
     * @param c controller
     * @param m model
     * @throws HeadlessException thrown when code that is dependent on a keyboard, display, or mouse
     * is called in an environment that does not support a keyboard, display,
     * or mouse.
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

        if(SystemTray.isSupported())
        {
            tray = SystemTray.getSystemTray();
            Image img = null;
            try {
                img = ImageIO.read(new File("images/bulb.gif"));
            } catch (IOException e) {
                //draw default img
            }
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

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == current_rb)
        {
            changeState(true, Main.NOTCOMPLETED);

        }
        else if (e.getSource() == completed_rb)
        {
            changeState(false, Main.COMPLETED);
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
                    c.delete(pairs[index]);
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

    private void changeState(boolean isEnabled, int newState) {
        addButton.setEnabled(isEnabled);
        Main.CURRENT = newState;
        c.load();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == taskList && e.getClickCount() == 2)
        {
            int index = taskList.getSelectedIndex();
            new ShowTaskDialog(c, m, pairs[index]);
        }
    }

    @Override
    public void update() {
        if(m != null)
        {
            Vector<Task> tasks = m.getData();
            if(tasks != null) {
                pairs = new int[tasks.size()];
                DefaultListModel listModel = new DefaultListModel();
                for (int i = 0; i < pairs.length; i++) {

                    Task current = tasks.get(i);
                    listModel.addElement(current.getName()/* + " : " + current.getID()*/);
                    pairs[i] = current.getID();
                }
                taskList.setModel(listModel);
            }
        }
    }

    @Override
    public void windowIconified(WindowEvent e) {
        try {
            tray.add(trayIcon);
        } catch (AWTException e1) {

        }
        setVisible(false);
    }

}
