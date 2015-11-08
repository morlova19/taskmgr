package forms;

import controller.IController;
import to.GeneratorID;
import to.TransferObject;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

/**
 * GUI for creating new task.
 * Contains components for entering the name, description, date and contacts of new task.
 */
public class CreateTaskDialog extends JDialog implements ActionListener {
    /**
     * Constant for error message that will be displayed if the entered name is empty.
     */
    public static final String ENTER_NAME_MSG = "Please enter name of the task";
    /**
     * Constant for error message that will be displayed if the entered date is not correct.
     */
    public static final String ENTER_DATE_MSG = "Please enter correct date of task";
    /**
     * Constant for actionCommand.
     */
    private final String OK_ACTION = "OK";
    /**
     * Constant for actionCommand.
     */
    private final String CANCEL_ACTION = "Cancel";
    /**
     * Container for components.
     */
    private JPanel contentPane;
    /**
     * Button to confirm adding of new task.
     */
    private JButton buttonOK;
    /**
     * Button to cancel adding of new task.
     */
    private JButton buttonCancel;
    /**
     * Field for entering name of task.
     */
    private JTextField name_tf;
    /**
     * Field for entering description of task.
     */
    private JTextArea description_tf;
    /**
     * Field for entering date of execution of task.
     */
    private JSpinner date_spinner;
    /**
     * Field for entering contacts.
     */
    private JTextArea contacts_tf;
    /**
     * Component to display error message
     * if date of the task is not correct.
     */
    private JLabel date_err_label;
    /**
     * Component to display error message
     * if name of the task is empty.
     */
    private JLabel name_err_label;
    /**
     * Provides methods to work with data.
     */
    private IController controller;
    /**
     * Constructs and displays dialog window.
     * @param controller controller.
     */
    public CreateTaskDialog(IController controller) {
        if(controller != null) {
            this.controller = controller;
        }
        setContentPane(contentPane);
        setTitle("New task");

        configButton(buttonOK, OK_ACTION);
        configButton(buttonCancel, CANCEL_ACTION);

        date_spinner.setModel(new SpinnerDateModel());

        setLocationRelativeTo(null);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }

    /**
     * Configures given button.
     * Adds ActionListener and sets actionCommand.
     * @param button button that will be configured.
     * @param action actionCommand.
     */
    private void configButton(AbstractButton button, String action) {
        button.addActionListener(this);
        button.setActionCommand(action);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand())
        {
            case OK_ACTION:
                add();
                break;
            case CANCEL_ACTION:
                dispose();
                break;
        }
    }
    /**
     * Validates the entered data.
     * If the data is correct then invokes method
     * of the {@link #controller} and transfers to it the entered data.
     */
    private void add() {
        String name = name_tf.getText().trim();
        Date date = (Date) date_spinner.getValue();
        String desc = description_tf.getText().trim();
        String contacts = contacts_tf.getText().trim();

        boolean isCorrectName = isCorrectName(name);
        boolean isCorrectDate = isCorrectDate(date);

        if(isCorrectName && isCorrectDate)
        {
            changeView(isCorrectName, isCorrectDate);
            if(controller != null) {
                controller.add(createTransferObject(name, date, desc, contacts));
            }
            dispose();
        }
        else {
            changeView(isCorrectName, isCorrectDate);
        }
        pack();
    }

    /**
     * Invokes methods to change view.
     * @param isCorrectName result of {@link #isCorrectName(String)} method.
     * @param isCorrectDate result of {@link #isCorrectDate(Date)} method.
     * @see #changeViewName(boolean)
     * @see #changeViewDate(boolean)
     */
    private void changeView(boolean isCorrectName, boolean isCorrectDate) {
        changeViewName(isCorrectName);
        changeViewDate(isCorrectDate);
    }

    /**
     * Invokes method {@link #configDate(String, Border)}  with parameters that
     * depend on that is the entered date is correct or not.
     * @param isCorrectDate result of the {@link #isCorrectDate(Date)} method.
     */
    private void changeViewDate(boolean isCorrectDate) {
        if (isCorrectDate) {
            configDate("", UIManager.getBorder("Spinner.border"));
        } else {
            configDate(ENTER_DATE_MSG, BorderFactory.createLineBorder(Color.red));
        }
    }
    /**
     * Invokes method {@link #configName(String, Border)} with parameters that
     * depend on that is the entered name is correct or not.
     * @param isCorrectName result of the {@link #isCorrectName(String)} method.
     */
    private void changeViewName(boolean isCorrectName) {
        if (isCorrectName) {
            configName("", UIManager.getBorder("TextField.border"));
        } else {
            configName(ENTER_NAME_MSG, BorderFactory.createLineBorder(Color.red));
        }
    }
    /**
     * Validates the entered date.
     * @param date the entered date.
     * @return true if the date is correct, else - false.
     */
    private boolean isCorrectDate(Date date) {
        long delta = date.getTime() - Calendar.getInstance().getTimeInMillis();
        return (delta > 0);
    }
    /**
     * Validates the entered name.
     * @param name the entered name.
     * @return true if the name is correct, else - false.
     */
    private boolean isCorrectName(String name) {
        return (!name.isEmpty());
    }
    /**
     * Creates TransferObject with given parameters.
     * @param name the entered name.
     * @param date the entered date.
     * @param desc the entered description.
     * @param contacts the entered contacts.
     * @return object of TransferObject class.
     */
    private TransferObject createTransferObject(String name, Date date,
                                                String desc, String contacts) {
        TransferObject to = new TransferObject();
        to.setName(name);
        to.setDescription(desc);
        to.setDate(date);
        to.setContacts(contacts);
        to.setId(GeneratorID.getGeneratedID());
        return to;
    }
    /**
     * Changes appearance of the {@link #name_tf} and {@link #name_err_label}.
     * @param text text that will be set into {@link #name_err_label}.
     * @param border border that will be set into {@link #name_tf}.
     */
    private void configName(String text, Border border) {
        name_err_label.setText(text);
        name_err_label.setForeground(Color.red);
        name_tf.setBorder(border);
    }
    /**
     * Changes appearance of the {@link #date_spinner} and {@link #date_err_label}.
     * @param text text that will be set into {@link #date_err_label}.
     * @param border border that will be set into {@link #date_spinner}.
     */
    private void configDate(String text, Border border) {
        date_err_label.setForeground(Color.red);
        date_err_label.setText(text);
        date_spinner.setBorder(border);
    }
}
