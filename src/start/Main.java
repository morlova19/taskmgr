package start;

import controller.Controller;
import controller.IController;
import model.IModel;
import model.Journal;
import journalmgr.JournalManager;
import model.Model;
import ns.CustomNotificationSystem;
import ns.INotificationSystem;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.InvalidPathException;
import java.text.ParseException;

public class Main {
    public static final int NOTCOMPLETED = 1;
    public static final int COMPLETED = 2;
    public static int CURRENT = NOTCOMPLETED;
    public static void main(String[] args) {

        try {
            ServerSocket socket = new ServerSocket(9999);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(new JFrame(),"Task manager is already running.");
            System.exit(1);
        }
        String path = System.getProperty("user.home") + "/taskmgr";
        Journal journal = null;
        JournalManager manager = null;
        try {
            manager = new JournalManager(path);
            journal = manager.readJournal();
        }
        catch (SAXException | ParseException | ParserConfigurationException e) {
            showErrorMessage("Cannot read file with the tasks.");
        }
        catch (IOException e) {
            showErrorMessage("I/O Error");
        }
        catch (InvalidPathException e)
        {
           showErrorMessage(e.getMessage());
        }
        if(journal != null) {
            journal.reload();
            try {
                manager.writeJournal(journal);
            } catch (TransformerException | ParserConfigurationException e) {
                showErrorMessage("Cannot write to file with the tasks.");
            }
            IModel model = new Model(manager, journal);
            INotificationSystem nSystem = new CustomNotificationSystem();
            IController controller = new Controller(model, nSystem);
           // nSystem.setController(controller);
        }
    }
    private static void showErrorMessage(String err) {
        JOptionPane.showMessageDialog(new JFrame(),
                err, "Error", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

}
