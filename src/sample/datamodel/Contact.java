package sample.datamodel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by КазанцевМЮ on 28.02.18.
 */
public class Contact implements Comparable<Contact>{

    private LocalDate date;
    Person person;
    private String content;

    public Contact(LocalDate date, String content) {
        this.date = date;
        this.content = content;
    }

    public Contact(LocalDate date,  String content, Person person) {
        this.date = date;
        this.content = content;
        this.person = person;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public int compareTo(Contact contact) {
        int comparison = this.date.compareTo(contact.getDate());
        return comparison;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        String thisDate = date.format(formatter);
        //String result = thisDate + " " + content;
        return thisDate + " " + content;
    }
}
