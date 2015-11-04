package journalmgr;

import model.Journal;
import model.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import to.TransferObject;

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
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Part of taskmgr.
 */

public class JournalManager {

    private String dir;
    private String cur_filename;
    private String comp_filename;

    public JournalManager(String path) {
        if(!new File(path).exists())
        {
            File dir = new File(path);
            dir.mkdir();
        }
        this.dir = path;
        cur_filename = dir +"tasks_current.xml";
        comp_filename = dir +"tasks_completed.xml";

    }

    public void writeJournal(Journal journal) {
        Vector<Task> tasks = journal.getCurrentTasks();
        write(new File(cur_filename), tasks);
        tasks = journal.getCompletedTasks();
        write(new File(comp_filename), tasks);

        //пока последний использованный id записывается в отдельный файл
        try {
            PrintWriter fw = new PrintWriter(new File(dir + "lastID.txt"));
            fw.print(Journal.getLastGeneratedID());
            fw.close();
        } catch (IOException e) {

        }
    }

    public Journal readJournal() {
        Journal journal = new Journal();
        try {
            Vector<Task> tasks = read(new File(cur_filename));
            journal.setCurrentTasks(tasks);
            tasks = read(new File(comp_filename));
            journal.setCompletedTasks(tasks);
            if(new File(dir + "lastID.txt").exists())
            {
                Scanner sc = new Scanner(new File(dir + "lastID.txt"));
                Journal.setGeneratedID(sc.nextInt());
                sc.close();
            }
            else {
                Journal.setGeneratedID(1);
            }
        } catch (Exception e) {
            //обрабатывать здесь либо в Model
        }
        return journal;
    }

    void write(File file, Vector<Task> tasks) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document document = builder.newDocument();
            Element rootElement = document.createElement("tasks");
            document.appendChild(rootElement);
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, new Locale("ru","RU"));
            for(Task t: tasks) {
                Element taskElement = document.createElement("task");
                Element nameEl = document.createElement("name");
                Element descEl = document.createElement("desc");
                Element dateEl = document.createElement("date");
                Element contactsEl = document.createElement("contacts");
                Element idEl = document.createElement("id");
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
        } catch (ParserConfigurationException | TransformerConfigurationException e) {
            //e.printStackTrace();
        } catch (TransformerException e) {
           // e.printStackTrace();
        }
    }

    Vector<Task> read(File file)  {
        Vector<Task> tasks = new Vector<>();
        if(!file.exists())
        {
            return tasks;
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            NodeList taskList = document.getElementsByTagName("task");
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
                        case "name":
                            name = taskFields.item(j).getTextContent();
                            break;
                        case "desc":
                            desc = taskFields.item(j).getTextContent();
                            break;
                        case "date":
                            date = df.parse(taskFields.item(j).getTextContent());
                            break;
                        case "contacts":
                            contacts = taskFields.item(j).getTextContent();
                            break;
                        case "id":
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

        } catch (ParserConfigurationException e) {

        } catch (ParseException e) {

        } catch (SAXException e) {

        } catch (IOException e) {

        }

        return tasks;
    }

}
