package model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
import java.text.SimpleDateFormat;
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
       // dir = path;

    }

    void writeJournal(Journal journal) throws IOException {
        Vector<Task> tasks = journal.getCurrentTasks();
        write(new File(cur_filename), tasks);
        tasks = journal.getCompletedTasks();
        write(new File(comp_filename), tasks);
    }

    Journal readJournal() {
        Journal journal = new Journal();
        try {
            Vector<Task> tasks = read(new File(cur_filename));
            journal.setCurrentTasks(tasks);
            tasks = read(new File(comp_filename));
            journal.setCompletedTasks(tasks);
        } catch (Exception e) {
            //обрабатывать здесь либо в Model
        }
        return journal;
    }

    void write(File file, Vector<Task> tasks) throws IOException {
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
                nameEl.setTextContent(t.getName());
                descEl.setTextContent(t.getDescription());
                dateEl.setTextContent(df.format(t.getDate()));
                contactsEl.setTextContent(t.getContacts());
                taskElement.appendChild(nameEl);
                taskElement.appendChild(descEl);
                taskElement.appendChild(dateEl);
                taskElement.appendChild(contactsEl);
                rootElement.appendChild(taskElement);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e){
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }


    }

    Vector<Task> read(File file) throws ParseException {
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
                        default:
                            throw new ParseException("Invalid name of element", 0);
                    }
                }
                tasks.add(new Task(name, desc, date, contacts));
            }

        } catch (ParserConfigurationException e) {

        } catch (IOException e) {

        } catch (SAXException e) {

        }

        return tasks;
    }

}
