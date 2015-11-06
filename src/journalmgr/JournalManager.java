package journalmgr;

import model.Journal;
import model.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import to.TransferObject;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.InvalidPathException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Part of taskmgr.
 */

public class JournalManager {

    public static final String TASK_TAG = "task";
    public static final String TASKS_TAG = "tasks";
    public static final String NAME_TAG = "name";
    public static final String DESC_TAG = "desc";
    public static final String DATE_TAG = "date";
    public static final String CONTACTS_TAG = "contacts";
    public static final String ID_TAG = "id";
    private String dir;
    private String cur_filename;
    private String comp_filename;

    public JournalManager(String path) {
        //TODO: check if path is directory
        if(new File(path).isDirectory()) {
            if (!new File(path).exists()) {
                File dir = new File(path);
                dir.mkdir();
            }
            this.dir = path;
            cur_filename = dir +"tasks_current.xml";
            comp_filename = dir +"tasks_completed.xml";
        }
        else {
            throw new InvalidPathException(path," Path is not directory");
        }

    }

    public void writeJournal(Journal journal) throws TransformerException, ParserConfigurationException {
        Vector<Task> tasks = journal.getCurrentTasks();
        write(new File(cur_filename), tasks);
        tasks = journal.getCompletedTasks();
        write(new File(comp_filename), tasks);
    }

    public Journal readJournal() throws ParserConfigurationException, SAXException, ParseException, IOException {
        Journal journal = new Journal();
       // try {
            Vector<Task> cur_tasks = read(new File(cur_filename));
            journal.setCurrentTasks(cur_tasks);
            Vector<Task> completed_tasks = read(new File(comp_filename));
            journal.setCompletedTasks(completed_tasks);
            int max1 = 0;
            int max2 = 0;
            if(!completed_tasks.isEmpty())
            {
                max1 = journal.getCompletedTasks()
                        .stream()
                        .mapToInt(Task::getID)
                        .max().getAsInt();
            }
            if (!cur_tasks.isEmpty()) {
                max2 = journal.getCurrentTasks()
                        .stream()
                        .mapToInt(Task::getID).max().getAsInt();
            }
            Journal.setGeneratedID(Math.max(max1,max2) + 1);

       // } catch (Exception e) {

            //TODO: handle exception
            //обрабатывать здесь либо в Model
      //  }
       // finally {

            return journal;
       // }
    }

    void write(File file, Vector<Task> tasks) throws ParserConfigurationException, TransformerException {
       // try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document document = builder.newDocument();
            Element rootElement = document.createElement(TASKS_TAG);
            document.appendChild(rootElement);
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, new Locale("ru","RU"));
            for(Task t: tasks) {
                Element taskElement = document.createElement(TASK_TAG);
                Element nameEl = document.createElement(NAME_TAG);
                Element descEl = document.createElement(DESC_TAG);
                Element dateEl = document.createElement(DATE_TAG);
                Element contactsEl = document.createElement(CONTACTS_TAG);
                Element idEl = document.createElement(ID_TAG);
                nameEl.setTextContent(t.getName());
                descEl.setTextContent(t.getDescription());
                dateEl.setTextContent(df.format(t.getDate()));
                contactsEl.setTextContent(t.getContacts());
                idEl.setTextContent(String.valueOf(t.getID()));
                taskElement.appendChild(nameEl);
                taskElement.appendChild(descEl);
                taskElement.appendChild(dateEl);
                taskElement.appendChild(contactsEl);
                taskElement.appendChild(idEl);
                rootElement.appendChild(taskElement);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
       // } catch (ParserConfigurationException | TransformerException e) {
            //TODO: handle exception
            //e.printStackTrace();
        //}
    }

    Vector<Task> read(File file) throws ParserConfigurationException, IOException, SAXException, ParseException {
        Vector<Task> tasks = new Vector<>();
        if(!file.exists())
        {
            return tasks;
        }

        //try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            NodeList taskList = document.getElementsByTagName(TASK_TAG);
            for (int i = 0; i < taskList.getLength(); i++) {
                NodeList taskFields = taskList.item(i).getChildNodes();
                String name = null;
                String desc = null;
                String contacts = null;
                Date date = null;
                int id = 0;
                DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, new Locale("ru","RU"));
                for (int j = 0; j < taskFields.getLength(); j++) {
                    switch (taskFields.item(j).getNodeName()) {
                        case NAME_TAG:
                            name = taskFields.item(j).getTextContent();
                            break;
                        case DESC_TAG:
                            desc = taskFields.item(j).getTextContent();
                            break;
                        case DATE_TAG:
                            date = df.parse(taskFields.item(j).getTextContent());
                            break;
                        case CONTACTS_TAG:
                            contacts = taskFields.item(j).getTextContent();
                            break;
                        case ID_TAG:
                             id = Integer.parseInt(taskFields.item(j).getTextContent());
                            break;
                        default:
                            throw new ParseException("Invalid name of element", 0);
                    }
                }
                TransferObject to = new TransferObject();
                to.setName(name);
                to.setDescription(desc);
                to.setDate(date);
                to.setContacts(contacts);
                to.setId(id);
                tasks.add(new Task(to));
            }

      //  }
        return tasks;

    }

}
