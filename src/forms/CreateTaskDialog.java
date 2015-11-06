package forms;

import controller.IController;
import model.Journal;
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
    public static final String ENTER_NAME_MSG = "Please enter name of the task";
    public static final String ENTER_DATE_MSG = "Please enter correct date of task";
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
    private JLabel nel;
    private JLabel del;
    /**
     * Controls this dialog.
     */
    private IController controller;

    private final String OK_ACTION = "OK";
    private final String CANCEL_ACTION = "Cancel";

    /**
     * Constructs and displays dialog window.
     * @param controller controller that controls this form.
     */
    public CreateTaskDialog(IController controller) {
        this.controller = controller;
        setContentPane(contentPane);
        setTitle("New task");
        buttonOK.addActionListener(this);
        buttonOK.setActionCommand(OK_ACTION);
        buttonCancel.addActionListener(this);
        buttonCancel.setActionCommand(CANCEL_ACTION);
        date_spinner.setModel(new SpinnerDateModel());
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
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

    private void add() {
        String name = name_tf.getText().trim();
        Date date = (Date) date_spinner.getValue();
        String desc = description_tf.getText();
        String contacts = contacts_tf.getText();

        boolean isCorrectName = isCorrectName(name);
        boolean isCorrectDate = isCorrectDate(date);

        if(isCorrectName && isCorrectDate)
        {
            changeView(isCorrectName, isCorrectDate);
            controller.add(createTransferObject(name, date, desc, contacts));
            dispose();
        }
        else {
            changeView(isCorrectName, isCorrectDate);
        }

        pack();
    }

    private void changeView(boolean isCorrectName, boolean isCorrectDate) {
        changeViewName(isCorrectName);
        changeViewDate(isCorrectDate);
    }

    private void changeViewDate(boolean isCorrectDate) {
        if (isCorrectDate) {
            configDate("", UIManager.getBorder("Spinner.border"));
        } else {
            configDate(ENTER_DATE_MSG, BorderFactory.createLineBorder(Color.red));
        }
    }

    private void changeViewName(boolean isCorrectName) {
        if (isCorrectName) {
            configName("", UIManager.getBorder("TextField.border"));
        } else {
            configName(ENTER_NAME_MSG, BorderFactory.createLineBorder(Color.red));
        }
    }

    private boolean isCorrectDate(Date date) {
        long delta = date.getTime() - Calendar.getInstance().getTimeInMillis();
        return (delta > 0);

    }

    private boolean isCorrectName(String name) {
        return (!name.equals(""));
    }

    private TransferObject createTransferObject(String name,
                                                Date date,
                                                String desc,
                                                String contacts) {
        TransferObject to = new TransferObject();
        to.setName(name);
        to.setDescription(desc);
        to.setDate(date);
        to.setContacts(contacts);
        to.setId(Journal.getGeneratedID());
        return to;
    }

    private void configName(String text, Border border) {
        nel.setText(text);
        nel.setForeground(Color.red);
        name_tf.setBorder(border);
    }

    private void configDate(String text, Border border) {
        del.setForeground(Color.red);
        del.setText(text);
        date_spinner.setBorder(border);
    }
}
