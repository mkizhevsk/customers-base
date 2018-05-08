package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import sample.datamodel.Company;
import sample.datamodel.Contact;
import sample.datamodel.Person;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Created by КазанцевМЮ on 10.04.18.
 */
public class ContactController {

    @FXML
    private DialogPane contactDialogPanel;

    @FXML
    private DatePicker dateField;

    @FXML
    private ComboBox<Person> persons;

    @FXML
    private Button add;

    @FXML
    private TextArea contentField;

    Company company;

    public void fillPersons(Company thisCompany) {

        company = thisCompany;
        persons.getItems().addAll(company.getPersons());
        dateField.setValue(LocalDate.now());
    }


    public Contact processResult() {
        dateField.setEditable(true);
        LocalDate date = dateField.getValue();
        String content = contentField.getText().trim();
        Contact newContact = new Contact(date, content);
        if (persons.getValue() != null) {
            newContact.setPerson(persons.getValue());
        }
        return newContact;
    }

    public void editContact(Contact contact, Company thisCompany) {
        company = thisCompany;
        dateField.setValue(contact.getDate());
        contentField.setText(contact.getContent());
        persons.getItems().addAll(company.getPersons());
        if (contact.getPerson() != null) {
            persons.setValue(contact.getPerson());
        }
    }

    public void updateContact(Contact contact) {
        contact.setDate(dateField.getValue());
        contact.setContent(contentField.getText());
        contact.setPerson(persons.getValue());
    }

    public void newPerson() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(contactDialogPanel.getScene().getWindow());
        dialog.setTitle("Add new person");
        dialog.setHeaderText("Use this dialog to add a new person");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("persondialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            PersonController controller = fxmlLoader.getController();
            Person newPerson = controller.processResult();
            if (newPerson != null) {
                System.out.println("new");
                company.addPerson(newPerson);
                persons.setValue(newPerson);
                //saver.saveCompanies();
            }
        }
    }
}