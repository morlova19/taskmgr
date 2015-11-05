package forms;

import controller.IController;
import model.IModel;
import model.Task;
import observer.TaskObserver;

import javax.swing.*;
import java.util.Calendar;
import java.util.Date;

/**
 * GUI to display notification about the task.
 */
public class MessageDialog extends JDialog implements TaskObserver {
    /**
     * Container for components of this form.
     * @see JPanel
     */
    private JPanel contentPane;
    /**
     * Button to delay the task.
     * @see JButton
     */
    private JButton delayButton;
    /**
     * Button to complete the task.
     * @see JButton
     */
    private JButton completeButton;
    /**
     * Component for displaying the details of the task.
     * @see JLabel
     */
    private JTextArea message;
    /**
     * Component for selecting new date of the task.
     * @see JSpinner
     */
    private JSpinner dateSpinner;
    /**
     * Model that provides data for displaying.
     * @see IModel
     */
    private IModel model;
    /**
     * Controller that controls this form.
     * @see IController
     */
    private IController controller;
    /**
     * Creates new dialog window.
     * @param c controller.
     * @param model model.
     */
    public MessageDialog(IController c, IModel model) {
        setContentPane(contentPane);
        dateSpinner.setModel(new SpinnerDateModel());
        this.controller = c;
        this.model = model;
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
    /**
     * Update dialog window.
     * Sets task's details in it.
     * @param id identifier of the task.
     */
    @Override
    public void update(int id) {
        Task t = model.get(id);
        message.setText(t.toString());
        delayButton.addActionListener(e -> {

            long delta = ((Date) dateSpinner.getValue()).getTime() - Calendar.getInstance().getTimeInMillis();
            if(delta <= 0)
            {
                JOptionPane.showMessageDialog(new JFrame(),"Please enter correct new date");
            }
            else {
                controller.delay(id,(Date)dateSpinner.getValue());
                dispose();
            }
        });

        completeButton.addActionListener(e -> {
            controller.complete(t);
            dispose();
        });
        setResizable(false);
        pack();
        setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
