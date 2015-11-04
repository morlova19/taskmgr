package forms;

import controller.IController;
import model.Journal;
import to.TransferObject;

import javax.swing.*;
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
     * Controls this dialog.
     */
    private IController controller;

    /**
     * Constructs and displays dialog window.
     * @param controller controller that controls this form.
     */
    public CreateTaskDialog(IController controller) {
        this.controller = controller;
        setContentPane(contentPane);
        setTitle("New task");
        buttonOK.addActionListener(this);
        buttonCancel.addActionListener(this);
        date_spinner.setModel(new SpinnerDateModel());
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == buttonOK)
        {
            if(name_tf.getText().equals(""))
            {
                JOptionPane.showMessageDialog(new JFrame(),"Please enter name of task");
            }
            else {
                Date date = (Date) date_spinner.getValue();
                if(date.getTime() - Calendar.getInstance().getTimeInMillis() < 0)
                {
                    JOptionPane.showMessageDialog(new JFrame(),"Please enter correct date of task");
                }
                else {
                    TransferObject to = new TransferObject();
                    to.setName(name_tf.getText());
                    to.setDescription(description_tf.getText());
                    to.setDate(date);
                    to.setContacts(contacts_tf.getText());
                    to.setId(Journal.getGeneratedID());
                    controller.add(to);
                    dispose();
                }

            }
        }
        else if(e.getSource() == buttonCancel)
        {
            dispose();
        }
    }
}
