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
import java.io.IOException;
import java.text.ParseException;

public class Main {
    public static final int NOTCOMPLETED = 1;
    public static final int COMPLETED = 2;
    public static int CURRENT = NOTCOMPLETED;
    public static void main(String[] args) {
        String path = "files/";
        JournalManager manager = new JournalManager(path);
        Journal journal = null;
        try {
            journal = manager.readJournal();
        } catch (ParserConfigurationException | SAXException | ParseException | IOException e) {
            int option = JOptionPane.showOptionDialog(new JFrame(),
                    e.getMessage(),
                    "Error",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new String[]{"OK"},
                    null);
            if(option == JOptionPane.YES_OPTION)
            {
                System.exit(1);
            }
        }
        if(journal != null) {
            journal.reload();
            NotificationSystem nSystem = new NotificationSystem(journal);
            IModel model = new Model(manager, journal);
            IController controller = new Controller(model, nSystem);
        }
    }

}
