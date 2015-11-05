package start;

import controller.Controller;
import controller.IController;
import model.IModel;
import model.Journal;
import journalmgr.JournalManager;
import model.Model;
import ns.NotificationSystem;

public class Main {
    public static final int NOTCOMPLETED = 1;
    public static final int COMPLETED = 2;
    public static int CURRENT = NOTCOMPLETED;
    public static void main(String[] args) {
        String path = "files/";
        JournalManager manager = new JournalManager(path);
        Journal journal = manager.readJournal();
        journal.reload();
        NotificationSystem nSystem = new NotificationSystem(journal);
        IModel model = new Model(manager,journal);
        IController controller = new Controller(model, nSystem);



    }

}
