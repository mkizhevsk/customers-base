package sample;

import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import sample.controller.CompanyController;
import sample.controller.ContactController;
import sample.controller.PersonController;
import sample.datamodel.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;

public class Controller {

    public static CompanyList companyList = new CompanyList();

    private FilteredList<Company> filteredList;
    private Predicate<Company> wantAllRegions;
    private Predicate<Company> wantSelectedRegion;
    String selectedRegion = "все";

    public SaveCompanies saver;

    @FXML
    private BorderPane mainPanel;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tabPersons;

    @FXML
    private Tab tabContacts;

    @FXML
    private TableView<Company> companiesTable;

    @FXML
    private TableView<Person> personsTable;

    @FXML
    private TableView<Contact> contactsTable;

    @FXML
    private TableColumn<Contact, LocalDate> contactDate;

    @FXML
    private ComboBox<String> regions;

    @FXML
    private Label addressLabel;

    @FXML
    private Label commentLabel;

    Company selectedCompany;

    public void initialize() throws IOException{

        companyList.readCompanies();
        saver = companyList;
        companiesTable.setItems(companyList.getCompanies());

        //regions.getItems().addAll(companyList.getRegions());
        regions.setItems(companyList.getRegions());
        regions.setValue(selectedRegion);

        addressLabel.setWrapText(true);
        commentLabel.setWrapText(true);

        companiesTable.setOnMousePressed((event) -> {
            selectedCompany = companiesTable.getSelectionModel().getSelectedItem();
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                if (selectedCompany != null) {
                    editThisCompany(selectedCompany);
                }
            } else if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {
                if (selectedCompany != null) {
                    updateCompany();
                }
            }
        });

        personsTable.setOnMousePressed((event) -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                editPerson();
            }
        });

        contactsTable.setOnMousePressed((event) -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                editContact();
            }
        });

        regions.setOnAction((event) -> {
            if (regions.getValue() != null) {
                selectedRegion = regions.getValue();
                //System.out.println("CheckBox Action (selected: " + selectedRegion + ")");
                showRegion();
            }
        });

        //tabPersons.setContent((Node) FXMLLoader.load(this.getClass().getResource("tab1view.fxml")));
        tabPersons.setContent(personsTable);
        tabContacts.setContent(contactsTable);

        wantAllRegions = new Predicate<Company>() {
            @Override
            public boolean test(Company company) {
                return true;
            }
        };

        wantSelectedRegion = new Predicate<Company>() {
            @Override
            public boolean test(Company company) {
                return (company.getRegion().equals(selectedRegion));
            }
        };

        contactDate.setCellFactory(col -> {
            TableCell<Contact, LocalDate> cell = new TableCell<Contact, LocalDate>() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        String formattedDate = DateTimeFormatter.ofPattern("dd.MM.yy").format(item);
                        this.setText(formattedDate);
                    }
                }
            };
            return cell;
        });
    }

    public void showRegion () {
        if (selectedRegion != null) {
            if (selectedRegion.equals("все")) {
                filteredList = new FilteredList<Company>(companyList.getCompanies(), wantAllRegions);

            } else {
                filteredList = new FilteredList<Company>(companyList.getCompanies(), wantSelectedRegion);
            }
            SortedList<Company> sortedList = new SortedList<Company>(filteredList,
                    new Comparator<Company>() {
                        @Override
                        public int compare(Company o1, Company o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
            companiesTable.setItems(sortedList);
            regions.setValue(selectedRegion);
        }
    }

    //edit company
    private void editThisCompany(Company company) {
        String oldRegion = company.getRegion();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPanel.getScene().getWindow());
        dialog.setTitle("Edit company: " + company.getName());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("companydialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        CompanyController companyController = fxmlLoader.getController();
        //System.out.println("before " + selectedRegion);
        companyController.editCompany(company, getCurrentRegions());
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            companyController.updateCompany(company);
            if (!company.getRegion().equals(oldRegion)) {
                //if (!oldRegion.equals("все")) {
                    companyList.deleteRegion(oldRegion);
                //}
                if (!company.getRegion().equals("архив")) {
                    companyList.addRegion(company.getRegion());
                }
                companyList.cleanRegions();
                companyList.updateRegions();
                selectedRegion = company.getRegion();
                showRegion();
                companiesTable.getSelectionModel().select(company);
            }
            updateCompany();
            saver.saveCompanies();
        }
    }

    @FXML
    public void addCompany() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPanel.getScene().getWindow());
        dialog.setTitle("Add new company");
        dialog.setHeaderText("Use this dialog to create a new company");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("companydialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        CompanyController controller = fxmlLoader.getController();
        controller.fillRegions(getCurrentRegions(), selectedRegion);
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            if (controller.processResult() != null) {
                Company newCompany = controller.processResult();
                if (companyList.addCompany(newCompany)) {
                    companyList.cleanRegions();
                    companyList.updateRegions();
                    if (!newCompany.getRegion().equals(selectedRegion)) {
                        selectedRegion = newCompany.getRegion();
                    }
                    showRegion();
                    companiesTable.getSelectionModel().select(newCompany);
                    selectedCompany = newCompany;
                    updateCompany();
                    saver.saveCompanies();
                }
            }
        }
    }

    @FXML
    public  void addItem() {
        if (tabPersons.isSelected()) {
            addPerson();
        } else if (tabContacts.isSelected()) {
            addContact();
        }
    }

    @FXML
    public void editItem() {
        if (tabPersons.isSelected()) {
            if (personsTable.getSelectionModel().getSelectedItem() != null) {
                editPerson();
            }
        } else if (tabContacts.isSelected()) {
            if (contactsTable.getSelectionModel().getSelectedItem() != null) {
                editContact();
            }
        }
    }

    @FXML
    public void editCompany() {
        Company selectedCompany = companiesTable.getSelectionModel().getSelectedItem();
        if (selectedCompany != null) {
            editThisCompany(selectedCompany);
        }
    }


    public void addContact() {
        if (selectedCompany == null) {
            System.out.println("please select a company");
            return;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPanel.getScene().getWindow());
        dialog.setTitle("Add new contact");
        dialog.setHeaderText("Use this dialog to add a new contact");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("contactdialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        ContactController controller = fxmlLoader.getController();
        controller.fillPersons(selectedCompany);
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            Contact newContact = controller.processResult();
            if (newContact != null) {
                selectedCompany.addContact(newContact);
                saver.saveCompanies();
            }
        }
    }

    public void addPerson() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPanel.getScene().getWindow());
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
                selectedCompany.addPerson(newPerson);
                saver.saveCompanies();
            }
        }
    }

    public void editContact() {
        Contact contact = contactsTable.getSelectionModel().getSelectedItem();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPanel.getScene().getWindow());
        dialog.setTitle("Edit contact");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("contactdialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        ContactController contactController = fxmlLoader.getController();
        contactController.editContact(contact, selectedCompany);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            contactController.updateContact(contact);
            contactsTable.refresh();
            saver.saveCompanies();
        }
    }

    public void editPerson() {
        Person person = personsTable.getSelectionModel().getSelectedItem();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPanel.getScene().getWindow());
        dialog.setTitle("Edit person");
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
        PersonController personController = fxmlLoader.getController();
        personController.editPerson(person);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            personController.updatePerson(person);
            personsTable.refresh();
            saver.saveCompanies();
        }
    }

    @FXML
    public void deleteCompany() {
        Company selectedCompany = companiesTable.getSelectionModel().getSelectedItem();
        if (selectedCompany == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No company selected");
            alert.setContentText("Please select the company you want to delete");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete company");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the selected company: " + selectedCompany.getName());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            int i = 0;
            companyList.deleteRegion(selectedCompany.getRegion());



//            for (Company company : companyList.getCompanies()) {
//                if (company.getRegion().equals(selectedCompany.getRegion())) {
//                    i++;
//                }
//            }
//
//            if (i == 1) {
//                //System.out.println("i " + i);
//                regions.getItems().remove(selectedCompany.getRegion());
//                companyList.deleteRegion(selectedCompany.getRegion());
//            }

            companyList.deleteCompany(selectedCompany);
            saver.saveCompanies();
        }
    }

    //delete items
    @FXML
    public void deleteItem() {
        if (tabPersons.isSelected()) {
            if (personsTable.getSelectionModel().getSelectedItem() != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete person");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete the selected person: " + personsTable.getSelectionModel().getSelectedItem().getName());
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    if (selectedCompany.deletePerson(personsTable.getSelectionModel().getSelectedItem())) {
                        saver.saveCompanies();
                    }
                }
            }
        } else if (tabContacts.isSelected()) {
            if (contactsTable.getSelectionModel().getSelectedItem() != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete contact");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete the selected contact: " + contactsTable.getSelectionModel().getSelectedItem());
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    if (selectedCompany.deleteContact(contactsTable.getSelectionModel().getSelectedItem())) {
                        saver.saveCompanies();
                    }
                }
            }
        }
    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            if ((selectedCompany != null) && (personsTable.getSelectionModel().getSelectedItem() == null) && (contactsTable.getSelectionModel().getSelectedItem() == null)) {
                editThisCompany(selectedCompany);
            } else if ((selectedCompany != null) && (personsTable.getSelectionModel().getSelectedItem() != null) && (tabPersons.isSelected())) {
                editPerson();
                //System.out.println("person");
            } else if ((selectedCompany != null) && (contactsTable.getSelectionModel().getSelectedItem() != null) && (tabContacts.isSelected())) {
                editContact();
                //System.out.println("contact");
            }
        }
        if ((keyEvent.getCode().equals(KeyCode.DELETE)) && (selectedCompany != null)) {
            deleteCompany();
        }
    }

    public void updateCompany() {
        companiesTable.refresh();
        addressLabel.setText(selectedCompany.getAddress());
        commentLabel.setText(selectedCompany.getComment());
        personsTable.setItems(selectedCompany.getPersons());
        contactsTable.setItems(selectedCompany.getContacts());
        contactsTable.refresh();
        personsTable.refresh();
    }

    public String[] getCurrentRegions() {
        String[] thisRegions = new String[companyList.getRegions().size()-1];
        int i = -1;
        for (String region : companyList.getRegions()) {
            if (i >= 0) {
                //System.out.println(region);
                thisRegions[i] = region;
            }
            i++;
        }
        return thisRegions;
    }

    @FXML
    public void helloWorld() {

        companyList.cleanRegions();

    }


    @FXML
    public void handleExit() throws IOException{
        //System.out.println(companyList.getCompanies().size());
        //saver.saveCompanies();
        Platform.exit();
    }
}
