package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.datamodel.Person;

/**
 * Created by КазанцевМЮ on 10.04.18.
 */
public class PersonController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField positionField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField commentField;


    public Person processResult() {
        String name = nameField.getText().trim();
        String position = positionField.getText().trim();
        String phone = phoneField.getText().trim();
        String comment = commentField.getText().trim();
        Person newPerson = new Person(name, position, phone, comment);
        return newPerson;
    }

    public void editPerson(Person person) {
        //company.printCompany();
        nameField.setText(person.getName());
        positionField.setText(person.getPosition());
        phoneField.setText(person.getPhone());
        commentField.setText(person.getComment());
    }

    public void updatePerson(Person person) {
        //System.out.println(regionField.getText());
        person.setName(nameField.getText());
        person.setPosition(positionField.getText());
        person.setPhone(phoneField.getText());
        person.setComment(commentField.getText());
    }

}
