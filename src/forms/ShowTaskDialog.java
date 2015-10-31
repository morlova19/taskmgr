package forms;

import controller.IController;
import model.IModel;
import model.Task;
import observer.TaskObserver;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Графический интерфейся для просмотра информации о задаче.
 */
public class ShowTaskDialog extends JDialog implements ActionListener, TaskObserver {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField name_tf;
    private JTextArea description_tf;
    private JSpinner date_spinner;
    private JTextArea contacts_tf;

    IController c;
    IModel m;

    public ShowTaskDialog(IController c, IModel m) {
        this.c = c;
        this.m = m;
        m.registerTaskObserver(this);
        setContentPane(contentPane);
        date_spinner.setModel(new SpinnerDateModel());
        buttonOK.addActionListener(this);
        buttonCancel.addActionListener(this);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == buttonOK)
        {
            dispose();
        }
        else if(e.getSource() == buttonCancel)
        {
            dispose();
        }
    }
    @Override
    public void update(int id) {
        Task t = m.get(id);
        if(t != null)
        {
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
