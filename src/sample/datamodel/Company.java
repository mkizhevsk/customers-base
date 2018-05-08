package sample.datamodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by КазанцевМЮ on 28.02.18.
 */
public class Company implements Comparable<Company> {
    private SimpleStringProperty name = new SimpleStringProperty("");
    private SimpleStringProperty address = new SimpleStringProperty("");
    private SimpleStringProperty comment = new SimpleStringProperty("");
    private ObservableList<Person> persons;
    private ObservableList<Contact> contacts;
    private String region;

    public Company(String name) {
        //System.out.println(name);
        this.name.set(name);
        this.persons = FXCollections.observableArrayList();
        this.contacts = FXCollections.observableArrayList();
        this.region = "";
    }

    public Company(String name, String region) {
        //System.out.println(name);
        this.name.set(name);
        this.persons = FXCollections.observableArrayList();
        this.contacts = FXCollections.observableArrayList();
        this.region = region;
    }

    public Company(String name, String region, String address, String comment) {
        //System.out.println(name);
        this.name.set(name);
        this.address.set(address);
        this.comment.set(comment);
        this.persons = FXCollections.observableArrayList();
        this.contacts = FXCollections.observableArrayList();
        this.region = region;
    }

    public String getName() {
        return name.get();
    }

    public ObservableList<Person> getPersons() { return persons; }

    public ObservableList<Contact> getContacts() {
        return contacts;
    }

    public String getRegion() {
        return region;
    }

    public void setPersons(ObservableList<Person> persons) {
        this.persons = persons;
    }

    public String getAddress() {
        return address.get();
    }

    public String getComment() {
        return comment.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setContacts(ObservableList<Contact> contacts) {
        this.contacts = contacts;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    public boolean addPerson(Person newPerson) {
        if (newPerson != null) {
            for (Person person : persons) {
                if (person.getName().equals(newPerson.getName())) {
                    System.out.println("Сотрудник '" + person.getName() + "' уже имеется в базе!");
                    return false;
                }
            }
            persons.add(newPerson);
            return true;
        }
        return false;
    }

    public boolean deletePerson(Person person) {
        if (persons.contains(person)) {
            persons.remove(person);
            return true;
        }
        return false;
    }

    public boolean deleteContact(Contact contact) {
        if (contacts.contains(contact)) {
            contacts.remove(contact);
            return true;
        }
        return false;
    }

    public boolean addContact(Contact newContact) {
        if (newContact != null) {
            contacts.add(newContact);
            return true;
        }
        return false;
    }

    public void printCompany() {
        System.out.println(name.getValue() + ": " + region + " " + address.getValue() );
        if (persons != null) {
            for (Person person : persons) {
                System.out.println("  " + person.getName() + " - " + person.getPosition());
            }
        }
        if (contacts != null) {
            for (Contact contact : contacts) {
                String printedPerson = "";
                if (contact.getPerson() != null) {
                    printedPerson = contact.getPerson().getName();
                }
                System.out.println("    " + contact.getDate() + " " + contact.getContent() + " " + printedPerson);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        //System.out.println("Entering StockItem.equals");
        if(obj == this) {
            return true;
        }

        if((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }

        String objName = ((Company) obj).getName();
        return this.name.equals(objName);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode() + 31;
    }

    @Override
    public int compareTo(Company company) {
        //System.out.println("Entering compareTo");
        int comparison = this.getName().compareTo(company.getName());
        return comparison;
    }

    @Override
    public String toString() {
        return name.get();
    }
}
