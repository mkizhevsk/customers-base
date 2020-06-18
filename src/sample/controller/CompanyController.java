package sample.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import sample.datamodel.Company;
import sample.datamodel.SaveCompanies;
import java.util.List;
import java.util.Optional;

public class CompanyController {

    public SaveCompanies saver;

    @FXML
    private TextField companyField;

    @FXML
    private ComboBox<String> boxRegions;

    @FXML
    private Button add;

    @FXML
    private TextField addressField;

    @FXML
    private TextField commentField;

    Company company;

    public void fillRegions(String[] regions, String selectedRegion) {
        boxRegions.getItems().addAll(regions);
        if (!selectedRegion.equals("все")) {
            boxRegions.setValue(selectedRegion);
        }
    }

    public Company processResult() {
        String company = companyField.getText().trim();
        String region = "";
        String address = addressField.getText().trim();
        String comment = commentField.getText().trim();
        if (companyField.getText().trim().isEmpty()) {
            return null;
        }
        if (boxRegions.getValue() != null) {
            region = boxRegions.getValue();
        }
        Company newCompany = new Company(company, region, address, comment);
        //newCompany.printCompany();
        //saver.saveCompanies();
        return newCompany;
    }

    public void editCompany(Company editedCompany, String[] regions) {
        company = editedCompany;
        companyField.setText(company.getName());
        //System.out.println(regions.size());
        boxRegions.getItems().addAll(regions);
        if (company.getRegion() != null) {
            boxRegions.setValue(company.getRegion());
        }
        addressField.setText(company.getAddress());
        commentField.setText(company.getComment());
    }

    public void updateCompany(Company company) {
        //System.out.println(regionField.getText());
        company.setName(companyField.getText());
        company.setRegion(boxRegions.getValue());
        company.setAddress(addressField.getText());
        company.setComment(commentField.getText());
        //company.printCompany();
    }

    public void newRegion() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("New region input dialog");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter region name:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            boxRegions.getItems().add(result.get());
            boxRegions.setValue(result.get());
        }
    }

}
