package start;

import controller.Controller;
import controller.IController;
import model.IModel;
import model.Journal;
import journalmgr.JournalManager;
import model.Model;
import ns.NotificationSystem;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
            JOptionPane.showMessageDialog(new JFrame(),"Task manager is already running ");
            System.exit(1);
        }
        String path = "files/";
        Journal journal = null;
        JournalManager manager = null;
        try {
            manager = new JournalManager(path);
            journal = manager.readJournal();
        } catch (ParserConfigurationException | SAXException | ParseException | IOException | InvalidPathException e) {
             JOptionPane.showMessageDialog(new JFrame(),
                    e.getMessage(), "Error",
                     JOptionPane.ERROR_MESSAGE);
                System.exit(1);

        }
        if(journal != null) {
            journal.reload();
            NotificationSystem nSystem = new NotificationSystem(journal);
            IModel model = new Model(manager, journal);
            IController controller = new Controller(model, nSystem);
        }
    }

}
