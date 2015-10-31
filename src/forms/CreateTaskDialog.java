package forms;

import controller.IController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

public class CreateTaskDialog extends JDialog implements ActionListener {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField name_tf;
    private JTextArea description_tf;
    private JSpinner date_spinner;
    private JTextArea contacts_tf;
    private IController controller;
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
                Date d = (Date) date_spinner.getValue();
                if(d.getTime() - Calendar.getInstance().getTimeInMillis() < 0)
                {
                    JOptionPane.showMessageDialog(new JFrame(),"Please enter correct date of task");
                }
                else {
                    controller.add(name_tf.getText(), description_tf.getText(), d, contacts_tf.getText());
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
