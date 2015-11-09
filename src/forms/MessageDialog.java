package forms;

import controller.IController;
import model.IModel;
import model.Task;
import observer.TaskObserver;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

/**
 * GUI to display notification about the task.
 */
public class MessageDialog extends JDialog implements TaskObserver, ActionListener {
    /**
     * Constant for actionCommand.
     */
    private static final String DELAY_ACTION = "delay";
    /**
     * Constant for actionCommand.
     */
    private static final String COMPLETE_ACTION = "complete";
    /**
     * Constant for error message that will be displayed if the entered date is not correct.
     */
    public static final String ENTER_DATE_MSG = "Please enter correct date of task";
    /**
     * Container for components of this form.
     */
    private JPanel contentPane;
    /**
     * Button to delay the task.
     */
    private JButton delayButton;
    /**
     * Button to complete the task.
     */
    private JButton completeButton;
    /**
     * Component for displaying the details of the task.
     */
    private JTextArea message;
    /**
     * Component for selecting new date of the task.
     */
    private JSpinner dateSpinner;
    /**
     * Label to display error message if the entered date is not correct.
     */
    private JLabel err_label;
    /**
     * Provides data for displaying.
     */
    private IModel model;
    /**
     * Provides methods to work with data.
     */
    private IController controller;

    private int id;
    /**
     * Creates new dialog window.
     * @param c controller.
     * @param model model.
     */
    public MessageDialog(IController c, IModel model) {
        setContentPane(contentPane);
        setTitle("Notification");

        message.setBackground(getBackground());

        dateSpinner.setModel(new SpinnerDateModel());

        delayButton.setActionCommand(DELAY_ACTION);
        delayButton.addActionListener(this);

        completeButton.addActionListener(this);
        completeButton.setActionCommand(COMPLETE_ACTION);

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
        message.setText(model.getCurrentTask(id).toString());
        this.id = id;
        setResizable(false);
        setAlwaysOnTop(true);
        setModal(true);
        pack();
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand())
        {
            case DELAY_ACTION:
                delay();
                break;
            case COMPLETE_ACTION:
                complete();
                break;
        }
    }
    /**
     * Validates the new date.
     * @param date the new date.
     * @return true if the date is correct, else - false.
     */
    private boolean isCorrectDate(Date date) {
        long delta = date.getTime() - Calendar.getInstance().getTimeInMillis();
        return (delta > 0);
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
     * Changes appearance of the {@link #dateSpinner} and {@link #err_label}.
     * @param text text that will be set into {@link #err_label}.
     * @param border border that will be set into {@link #dateSpinner}.
     */
    private void configDate(String text, Border border) {
        err_label.setForeground(Color.red);
        err_label.setText(text);
        dateSpinner.setBorder(border);

    }
    /**
     * Informs controller about that user wants to delay the task.
     */
    private void delay() {
        Date newDate = (Date) dateSpinner.getValue();
        boolean isCorrectDate = isCorrectDate(newDate);

        changeViewDate(isCorrectDate);

        if(isCorrectDate)
        {
            if(controller != null) {
                controller.delay(id, (Date) dateSpinner.getValue());
            }
            dispose();
        }
    }
    /**
     * Informs controller about that user wants to complete the task.
     */
    private void complete() {
        if(controller != null) {
            controller.complete(id);
        }
        dispose();
    }
}
