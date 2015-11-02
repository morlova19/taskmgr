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
 * Графический интерфейс для просмотра информации о задаче.
 * Содержит компоненты для отображения имени, описания, даты и контактов задачи.
 */
public class ShowTaskDialog extends JDialog implements ActionListener, TaskObserver {
    /**
     * Контейнер для компонентов.
     */
    private JPanel contentPane;
    /**
     * Кнопка для подтверждения.
     */
    private JButton buttonOK;
    /**
     * Кнопка для отмены.
     */
    private JButton buttonCancel;
    /**
     * Поле для отображения имени задачи.
     */
    private JTextField name_tf;
    /**
     * Поле для отображения описания задачи.
     */
    private JTextArea description_tf;
    /**
     * Поле для отображения даты выполнения задачи.
     */
    private JSpinner date_spinner;
    /**
     * Поле для отображения контактов задачи.
     */
    private JTextArea contacts_tf;

    /**
     * Управляет диалоговым окном.
     */
    IController c;
    /**
     * Предоставляет данные о задаче для отображения.
     */
    IModel m;
    /**
     * Создает и отображает форму.
     * Инициализирует поля IController и IModel.
     * @param c контроллер
     * @param m модель
     */
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

    /**
     * Вызывается, когда произошло действие.
     * Например, если была нажата кнопка ОК или Отмена.
     * @param e событие.
     */
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

    /**
     * Получает информацию о задаче и заполняет поля формы этими данными.
     * @param id номер задачи. по которому она будет найдена в списке задач.
     */
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
