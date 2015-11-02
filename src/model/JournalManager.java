package model;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

/**
 * Part of taskmgr.
 */

public class JournalManager {

    private String filepath;
    private String filename; // Зачем?

    JournalManager(String path) {
        filepath = path;
        filename = "tasks";
    }

    void writeJournal(Journal journal) {
        Vector<Task> tasks = journal.getCurrentTasks();
        write(new File(filepath+filename+".xml"), tasks);
        tasks = journal.getCompletedTasks();
        write(new File(filepath+filename+"_completed.xml"), tasks);
    }

    Journal readJournal() {
        Journal journal = new Journal();
        try {
            Vector<Task> tasks = read(new File(filepath + filename + ".xml"));
            journal.setCurrentTasks(tasks);
            tasks = read(new File(filepath + filename + "_completed.xml"));
            journal.setCompletedTasks(tasks);
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
            Element taskElement = document.createElement("task");
            Element nameEl = document.createElement("name");
            Element descEl = document.createElement("desc");
            Element dateEl = document.createElement("task");
            Element contactsEl = document.createElement("contacts");
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, new Locale("ru","RU"));
            for(Task t: tasks) {
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
        Vector<Task> tasks = new Vector<Task>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("test.xml");
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return tasks;
    }

}
