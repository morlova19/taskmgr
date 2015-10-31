package forms;

import controller.IController;
import model.IModel;
import model.Task;
import observer.ListObserver;
import start.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class StartForm extends JFrame implements ActionListener, MouseListener, ListObserver {
    private JList taskList;
    private JButton addButton;
    private JButton deleteButton;
    private JRadioButton current_rb;
    private JRadioButton completed_rb;
    private JPanel mainPanel;

    IController c ;
    IModel m;

    public StartForm(IController c, IModel m) throws HeadlessException {
        setContentPane(mainPanel);
        setTitle("Task manager");
        addButton.addActionListener(this);
        deleteButton.addActionListener(this);
        taskList.addMouseListener(this);
        current_rb.addActionListener(this);
        completed_rb.addActionListener(this);

        this.c = c;
        this.m = m;
        m.registerListObserver(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == current_rb)
        {
            addButton.setEnabled(true);
            Main.CURRENT = Main.NOTCOMPLETED;
            c.load();
        }
        else if (e.getSource() == completed_rb)
        {
            addButton.setEnabled(false);
            Main.CURRENT = Main.COMPLETED;
            c.load();
        }
        else if(e.getSource() == addButton)
        {
            new CreateTaskDialog(c).setVisible(true);
        }
        else if(e.getSource() == deleteButton)
        {
            int index = taskList.getSelectedIndex();
            if(index != -1)
            {
                int option = JOptionPane.showConfirmDialog(getContentPane(),"Are you sure?", " Delete", JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.YES_OPTION) {
                    c.delete(index);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(getContentPane(),"Please, select task");
            }
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == taskList && e.getClickCount() == 2)
        {
            int index = taskList.getSelectedIndex();
            ShowTaskDialog dialog = new ShowTaskDialog(c, m);
            c.show(index);
            dialog.setEnabled(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void update() {
        if(m != null)
        {
            ArrayList<String> tasks = m.getData();
            if(tasks != null) {
                DefaultListModel listModel = new DefaultListModel();
                for (String s : tasks) {
                    listModel.addElement(s);
                }
                taskList.setModel(listModel);
            }
        }
    }

    @Override
    public void update(Task task) {
        if(m != null)
        {
            String message = "Name: " + task.getName() + "\n" +
                    "Description : " + task.getDescription() +
                    "\n" + "Contacts :" + task.getContacts();
            String[] options = {"delay", "finish"};

            int answer = JOptionPane.showOptionDialog(new JFrame(), message,
                    "Notification",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[1]);
            if(answer == JOptionPane.YES_OPTION)
            {
                c.delay(task);
            }
            else {
                c.load();
            }
        }
    }


}
