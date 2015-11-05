package forms;

import controller.IController;
import model.IModel;
import model.Task;
import observer.TaskObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GUI to view information about the task.
 * It contains components for displaying task's details.
 */
public class ShowTaskDialog extends JDialog implements ActionListener, TaskObserver {
    /**
     * Containter for components.
     */
    private JPanel contentPane;
    /**
     * Button to confirm.
     */
    private JButton buttonOK;
    /**
     * Button to cancel.
     */
    private JButton buttonCancel;
    /**
     * Field to display task's name.
     */
    private JTextField name_tf;
    /**
     *  Field to display task's description.
     */
    private JTextArea description_tf;
    /**
     *  Field to display task's date.
     */
    private JSpinner date_spinner;
    /**
     * Field to display contacts.
     */
    private JTextArea contacts_tf;
    /**
     * Controls this dialog.
     */
    IController c;
    /**
     * Provides task's details to display.
     */
    IModel m;

    private final String OK_ACTION = "OK";
    private final String CANCEL_ACTION = "Cancel";

    /**
     * Creates and displays dialog.
     * @param c controller.
     * @param m model.
     */
    public ShowTaskDialog(IController c, IModel m, int id) {
        this.c = c;
        this.m = m;
        m.registerTaskObserver(this);
        setContentPane(contentPane);
        date_spinner.setModel(new SpinnerDateModel());
        buttonOK.addActionListener(this);
        buttonOK.setActionCommand(OK_ACTION);
        buttonCancel.addActionListener(this);
        buttonCancel.setActionCommand(CANCEL_ACTION);
        c.show(id);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand())
        {
            case OK_ACTION:
                dispose();
                break;
            case CANCEL_ACTION:
                dispose();
                break;
        }
    }
    /**
     * Gets task's details and fills fields of this form.
     * @param id id of the task whose details will be displayed.
     */
    @Override
    public void update(int id) {
        Task t = m.get(id);
        if(t != null) {
            name_tf.setText(t.getName());
            description_tf.setText(t.getDescription());
            date_spinner.setValue(t.getDate());
            contacts_tf.setText(t.getContacts());
            name_tf.setEditable(false);
            description_tf.setEditable(false);
            ((JSpinner.DefaultEditor) date_spinner.getEditor()).getTextField().setEditable(false);
            contacts_tf.setEditable(false);
        }
        setVisible(true);
    }
}
