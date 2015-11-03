package start;

import controller.Controller;
import controller.IController;
import model.IModel;
import model.JournalManager;
import model.Model;
import java.io.IOException;

public class Main {
    public static final int NOTCOMPLETED = 1;
    public static final int COMPLETED = 2;
    public static int CURRENT = NOTCOMPLETED;
    public static void main(String[] args) {
            String path = "files/";
            try {
                IModel model = new Model(new JournalManager(path));
                IController controller = new Controller(model);
            } catch (IOException e) {

            }
    }

}
