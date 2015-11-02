package forms;

import controller.IController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

/**
 * Графический интерфейся для создания задачи.
 * Содержит комненты для вводы имени, описания, даты и контактов задачи.
 */
public class CreateTaskDialog extends JDialog implements ActionListener {
    /**
     * Контейнер для компонентов.
     */
    private JPanel contentPane;
    /**
     * Кнопка для подтверждения добавления.
     */
    private JButton buttonOK;
    /**
     * Кнопка для отмены добавления.
     */
    private JButton buttonCancel;
    /**
     * Поле для ввода имени задачи.
     */
    private JTextField name_tf;
    /**
     * Поле для ввода описания задачи.
     */
    private JTextArea description_tf;
    /**
     * Компонент для ввода даты выполнения задачи.
     */
    private JSpinner date_spinner;
    /**
     * Поле для ввода контактов задачи.
     */
    private JTextArea contacts_tf;
    /**
     * Управляет диалоговым окном.
     */
    private IController controller;

    /**
     * Создает и отображает форму.
     * Инициализирует поле IController.
     * @param controller контроллер
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
    /**
     * Вызывается, когда произошло действие.
     * Например, если была нажата кнопка ОК или Отмена.
     * @param e событие.
     */
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
