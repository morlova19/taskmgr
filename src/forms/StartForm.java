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
import java.util.concurrent.atomic.AtomicIntegerArray;

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

    private final String ADD_ACTION = "add data";

    private final String DELETE_ACTION = "delete data";

    private final String OPEN_ACTION = "open";

    private final String EXIT_ACTION = "exit";

    private final String DISPLAY_CURRENT_ACTION = "display current";

    private final String DISPLAY_COMPLETED_ACTION = "display completed";
    /**
     * Array in which index this is number of the task in JList
     * and pairs[index] this is identifier of the task.
     */
    private AtomicIntegerArray pairs;
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
        configButton(addButton, ADD_ACTION);
        configButton(deleteButton, DELETE_ACTION);
        taskList.addMouseListener(this);
        configButton(current_rb, DISPLAY_CURRENT_ACTION);
        configButton(completed_rb, DISPLAY_COMPLETED_ACTION);

        this.c = c;
        this.m = m;
        m.registerListObserver(this);

        if(SystemTray.isSupported())
        {
            configTray();
        }
        addWindowListener(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }

    private void configButton(AbstractButton button, String action) {
        button.setActionCommand(action);
        button.addActionListener(this);
    }

    private void configTray() {
        tray = SystemTray.getSystemTray();
        Image img = null;
        try {
            img = ImageIO.read(new File("images/bulb.gif"));
        } catch (IOException e) {
            //TODO: draw default img
        }
        PopupMenu popupMenu = new PopupMenu();
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem openItem = new MenuItem("Open");
        popupMenu.add(openItem);
        popupMenu.add(exitItem);
        exitItem.setActionCommand(EXIT_ACTION);
        exitItem.addActionListener(this);
        openItem.setActionCommand(OPEN_ACTION);
        openItem.addActionListener(this);
        trayIcon = new TrayIcon(img, "Task manager",popupMenu);
        trayIcon.setImageAutoSize(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand())
        {
            case ADD_ACTION:
                new CreateTaskDialog(c).setVisible(true);
                break;
            case DELETE_ACTION:
                deleteAction();
                break;
            case DISPLAY_COMPLETED_ACTION:
                changeState(false, Main.COMPLETED);
                break;
            case DISPLAY_CURRENT_ACTION:
                changeState(true, Main.NOTCOMPLETED);
                break;
            case OPEN_ACTION:
                removeFromSystemTray();
                break;
            case EXIT_ACTION:
                System.exit(0);
                break;
            default:
                break;
        }
    }

    private void removeFromSystemTray() {
        setVisible(true);
        tray.remove(trayIcon);
    }

    private void deleteAction() {
        int index = taskList.getSelectedIndex();
        if(index != -1)
        {
            int option = JOptionPane.showConfirmDialog(getContentPane(), "Are you sure?", " Delete", JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION) {
                c.delete(pairs.get(index));
            }
        }
        else
        {
            JOptionPane.showMessageDialog(getContentPane(),"Please, select task");
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
            new ShowTaskDialog(c, m, pairs.get(index));
        }
    }

    @Override
    public void update() {
        if(m != null)
        {
            Vector<Task> tasks = m.getData();

            if(tasks != null) {
                pairs = new AtomicIntegerArray(tasks.size());
                DefaultListModel listModel = new DefaultListModel();
                final int[] index = {0};
                tasks.stream().forEach((task -> {
                    listModel.addElement(task.getName());
                    pairs.set(index[0], task.getID());
                    index[0]++;
                }));
               /* for (int i = 0; i < pairs.length; i++) {
                    Task current = tasks.get(i);
                    listModel.addElement(current.getName()/* + " : " + current.getID());
                    pairs[i] = current.getID();
                }*/
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
