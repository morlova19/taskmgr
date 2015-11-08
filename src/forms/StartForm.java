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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * GUI to display list of the tasks.
 */
public class StartForm extends JFrame implements ActionListener, CustomMouseListener, CustomWindowListener, ListObserver {
    /**
     * Constant for question of confirmation deleting the data.
     */
    public static final String ARE_YOU_SURE = "Are you sure?";
    /**
     * Constant for message that will be displayed if the task was not selected.
     */
    public static final String PLEASE_SELECT_TASK = "Please, select task";
    /**
     * Constant for title of the confirm dialog.
     */
    public static final String DELETE = "Delete";
    /**
     * Constant for title of the confirm dialog.
     */
    public static final String EXIT = "Exit";
    /**
     * Constant for name of the app.
     */
    public static final String TASK_MANAGER = "Task manager";
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
    private SystemTray tray = null;
    /**
     * Icon that will be displayed in system tray.
     */
    private TrayIcon trayIcon;

    /**
     * Constant for actionCommand.
     */
    private final String ADD_ACTION = "add data";
    /**
     * Constant for actionCommand.
     */
    private final String DELETE_ACTION = "delete data";
    /**
     * Constant for actionCommand.
     */
    private final String OPEN_ACTION = "open";
    /**
     * Constant for actionCommand.
     */
    private final String EXIT_ACTION = "exit";
    /**
     * Constant for actionCommand.
     */
    private final String DISPLAY_CURRENT_ACTION = "display current";
    /**
     * Constant for actionCommand.
     */
    private final String DISPLAY_COMPLETED_ACTION = "display completed";
    /**
     * Thread-safe array in which index this is number of the task in {@link #taskList}
     * and value of this element in array this is identifier of this task.
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
        setTitle(TASK_MANAGER);
        setIconImage(getIcon());
        taskList.setFixedCellWidth(200);
        taskList.setVisibleRowCount(15);
        configButton(addButton, ADD_ACTION);
        configButton(deleteButton, DELETE_ACTION);
        taskList.addMouseListener(this);
        configButton(current_rb, DISPLAY_CURRENT_ACTION);
        configButton(completed_rb, DISPLAY_COMPLETED_ACTION);

        if(c != null) {
            this.c = c;
        }
        if(m != null) {
            this.m = m;
            m.registerListObserver(this);
        }

        if(SystemTray.isSupported())
        {
            configTray();
        }
        addWindowListener(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void configButton(AbstractButton button, String action) {
        button.setActionCommand(action);
        button.addActionListener(this);
    }

    /**
     * Gets system tray and creates icon that will be displayed in tray.
     */
    private void configTray() {
        tray = SystemTray.getSystemTray();
        Image img = getIcon();
        PopupMenu popupMenu = new PopupMenu();
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem openItem = new MenuItem("Open");
        popupMenu.add(openItem);
        popupMenu.add(exitItem);
        exitItem.setActionCommand(EXIT_ACTION);
        exitItem.addActionListener(this);
        openItem.setActionCommand(OPEN_ACTION);
        openItem.addActionListener(this);
        trayIcon = new TrayIcon(img, TASK_MANAGER,popupMenu);
        trayIcon.setImageAutoSize(true);
    }

    /**
     * Creates icon of the app.
     * @return icon.
     */
    private Image getIcon() {
        Image img;
        String path = "images/icon.png";
        try {
            img = ImageIO.read(ClassLoader.getSystemResourceAsStream(path));
        } catch (IOException | IllegalArgumentException e) {
            img = drawIcon();
        }
        return img;
    }
    /**
     * Draws icon of app if it cannot be read from resource file.
     * @return icon.
     */
    private Image drawIcon() {
        Image img;
        int w = 35;
        int h = w;
        img = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        Graphics2D gr = ((BufferedImage)img).createGraphics();
        gr.setComposite(AlphaComposite.Clear);
        gr.fillRect(0, 0, w, h);
        gr.setComposite(AlphaComposite.SrcOver);
        gr.setColor(Color.BLUE);
        gr.fillOval(0,0,w,h);
        gr.setFont(new Font("Arial", Font.BOLD, 18));
        gr.setColor(Color.YELLOW);
        gr.drawString("TM",5,24);
        return img;
    }
    /**
     * Removes icon from system tray and sets this form visible.
     */
    private void removeFromTray() {
        setExtendedState(JFrame.NORMAL);
        setVisible(true);
        tray.remove(trayIcon);
    }
    /**
     * Asks to confirm deleting the task.
     * If user confirms, informs to the controller.
     */
    private void deleteAction() {
        int index = taskList.getSelectedIndex();
        if(index != -1)
        {
            int option = JOptionPane.showConfirmDialog(getContentPane(), ARE_YOU_SURE, DELETE, JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION) {
                if(c != null) {
                    c.delete(pairs.get(index));
                }
            }
        }
        else
        {
            JOptionPane.showMessageDialog(getContentPane(), PLEASE_SELECT_TASK);
        }
    }

    /**
     * Invokes when the user selects which tasks wants to see.
     * Changes the view and current state of the app.
     * For example, if the user wants to see completed tasks then
     * add button will be not available and
     * the new state will be {@link Main#COMPLETED}.
     * @param isEnabled property of the addButton.
     * @param newState new state.
     */
    private void changeState(boolean isEnabled, int newState) {
        addButton.setEnabled(isEnabled);
        Main.CURRENT = newState;
        if(c != null) {
            c.load();
        }
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
                removeFromTray();
                break;
            case EXIT_ACTION:
                System.exit(0);
                break;
            default:
                break;
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == taskList && e.getClickCount() == 2)
        {
            int index = taskList.getSelectedIndex();
            if(index != -1) {
                new ShowTaskDialog(c, m, pairs.get(index));
            }
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
                taskList.setModel(listModel);
            }
            pack();
        }
    }
    @Override
    public void windowIconified(WindowEvent e) {
        if(tray != null) {
            try {
                tray.add(trayIcon);
                trayIcon.displayMessage("Task manager", "Task manager is here.", TrayIcon.MessageType.INFO);
            } catch (AWTException e1) {
                System.exit(1);
            }
            setVisible(false);
        }
    }

}
