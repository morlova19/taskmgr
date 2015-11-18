package forms;

import controller.IController;
import model.IModel;
import journal.Task;
import observer.TaskObserver;
import utils.DateUtil;
import utils.Icon;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * GUI to display the information about the task.
 * It contains components for displaying task's details.
 */
public class ShowTaskDialog extends JDialog implements ActionListener, TaskObserver {
    /**
     * Constant for actionCommand.
     */
    private static final String OK_ACTION = "OK";
    /**
     * Constant for actionCommand.
     */
    private static final String CANCEL_ACTION = "Cancel";
    /**
     * Container for components.
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
    private JFormattedTextField dateField;
    /**
     * Field to display contacts.
     */
    private JTextArea contacts_tf;


    private JLabel date_label;
    /**
     * Provides methods to work with data.
     */
    IController c;
    /**
     * Provides task's details to display.
     */
    IModel m;


    /**
     * Creates and displays dialog.
     * @param c controller.
     * @param m model.
     */
    public ShowTaskDialog(IController c, IModel m, int id) {
        setContentPane(contentPane);

        setTitle("Task dialog");
        setIconImage(Icon.getIcon());

        date_label.setText("<html>Date<br>(dd.mm.yyyy hh:mm)</html>");
        configDateField();

        configButton(buttonOK, OK_ACTION);
        configButton(buttonCancel, CANCEL_ACTION);

        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        if(m != null) {
            this.m = m;
            m.registerTaskObserver(this);
        }
        if(c != null) {
            this.c = c;
            c.show(id);
        }

    }

    /**
     * Configures {@link #dateField}.
     * Sets format.
     */
    private void configDateField() {
        MaskFormatter dateFormatter = null;
        try {
            dateFormatter = new MaskFormatter("##.##.#### ##:##");
            dateFormatter.setPlaceholderCharacter('_');

        } catch (ParseException e) {

        }
        DefaultFormatterFactory dateFormatterFactory = new
                DefaultFormatterFactory(dateFormatter);
        dateField.setFormatterFactory(dateFormatterFactory);
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
                dispose();
                break;
            case CANCEL_ACTION:
                dispose();
                break;
        }
    }
    /**
     * Gets task's details and fills fields of this form.
     * @param id identifier of the task whose details will be displayed.
     */
    @Override
    public void update(int id) {
        if(m != null) {
            Task t = m.get(id);
            if (t != null) {
                name_tf.setText(t.getName());
                description_tf.setText(t.getDescription());

                String date = DateUtil.format(t.getDate());
                dateField.setValue(date);
              //  date_spinner.setValue(t.getDate());
                contacts_tf.setText(t.getContacts());
                name_tf.setEditable(false);
                description_tf.setEditable(false);
                dateField.setEditable(false);
             //   ((JSpinner.DefaultEditor) date_spinner.getEditor()).getTextField().setEditable(false);
                contacts_tf.setEditable(false);
                pack();
                setVisible(true);
            }

        }
    }

}
