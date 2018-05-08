package sample.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.Alert;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by КазанцевМЮ on 28.02.18.
 */
public class CompanyList implements SaveCompanies {

    private static CompanyList instance = new CompanyList();
    private ObservableList<Company> companies;
    public static DateTimeFormatter formatter;
    private ObservableList<String> regions;

    public static CompanyList getInstance() {
        return instance;
    }

    static {
        System.out.println("Let's start!");
    }

    public CompanyList() {
        this.companies = FXCollections.observableArrayList();
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        regions = FXCollections.observableArrayList();
    }

    public ObservableList<Company> getCompanies() {
        Collections.sort(companies);
        return this.companies;
    }

    public ObservableList<String> getRegions() {
        return regions;
    }

    public DateTimeFormatter getFormatter() { return formatter; }

    public boolean addCompany(Company newCompany) {
        if (newCompany != null) {
            for (Company company : companies) {
                if (company.getName().equals(newCompany.getName())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Компания '" + company.getName() + "' уже имеется в базе!");
                    alert.showAndWait();
                    //System.out.println("Компания '" + company.getName() + "' уже имеется в базе!");
                    return false;
                }
            }
            if (!newCompany.getRegion().equals("")) {
                addRegion(newCompany.getRegion());
            }
            companies.add(newCompany);
            return true;
        }
        return false;
    }

    public boolean addRegion(String region) {
        //System.out.println("added region: " + region);
        if (!checkRegion(region)) {
            regions.add(region);
            return true;
        }
        return false;
    }

    public boolean checkRegion (String region) {
        for (String thisRegion : regions) {
            if (thisRegion.equals(region)) {
                return true;
            }
        }
        return false;
    }

    public void updateRegions() {
        Collections.sort(regions);
        regions.add("архив");
        Collections.reverse(regions);
        regions.add("все");
        Collections.reverse(regions);
    }

    public void cleanRegions() {
        while (regions.contains("архив")) {
            regions.remove("архив");
        }
        while (regions.contains("все")) {
            regions.remove("все");
        }
    }

    public void deleteCompany(Company company) {
        companies.remove(company);
    }

    public boolean deleteRegion(String region) {
        int i = 0;
        //System.out.println("deleted: " + region);
        for (Company thisCompany : companies) {
            if (thisCompany.getRegion().equals(region)) {
                i++;
            }
        }
        if (i > 1) {
            return false;
        }
        //System.out.println("removed: " + region);
        regions.remove(region);
        return true;
    }

    public void readCompanies() {
        Scanner scanner = null;

        //read companies data
        try {
            scanner = new Scanner(new BufferedReader(new FileReader("companies.txt")));
            //regions.add("все");
            while(scanner.hasNextLine()) {
                String input = scanner.nextLine();
                String[] result = input.split("/_");
                String name = result[0];
                Company newCompany = new Company(name);
                try {
                    String region = result[1];
                    addRegion(region);
                    newCompany.setRegion(region);
                } catch (ArrayIndexOutOfBoundsException e){
                    System.out.println("there is no region for " + name);
                }
                try {
                    String address = result[2];
                    newCompany.setAddress(address);
                } catch (ArrayIndexOutOfBoundsException e){
                    //System.out.println("there is no address for " + name);
                }
                try {
                    String comment = result[3];
                    newCompany.setComment(comment);
                } catch (ArrayIndexOutOfBoundsException e){
                    //System.out.println("there is no comment for " + name);
                }
                addCompany(newCompany);
            }
            try {
                regions.remove("архив");
            } catch (Exception e) {
                e.printStackTrace();
            }
            updateRegions();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if(scanner != null) {
                scanner.close();
            }
        }

        //read persons data
        try {
            scanner = new Scanner(new BufferedReader(new FileReader("persons.txt")));
            //scanner.useDelimiter(",");
            while(scanner.hasNextLine()) {
                String input = scanner.nextLine();
                //System.out.println(input);
                String[] result = input.split("/_");
                String companyName = result[0];
                String name = "";
                String position = "";
                String phone = "";
                String comment = "";
                for(Company company : companies) {
                    if (company.getName().equals(companyName)) {
                        try {
                            name = result[1];
                        } catch (ArrayIndexOutOfBoundsException e) {
                            //System.out.println("there is no name" );
                        }
                        try {
                            position = result[2];
                        } catch (ArrayIndexOutOfBoundsException e) {
                            //System.out.println("there is no position for " + name);
                        }
                        try {
                            phone = result[3];
                        } catch (ArrayIndexOutOfBoundsException e) {
                            //System.out.println("there is no phone for " + name);
                        }
                        try {
                            comment = result[4];
                        } catch (ArrayIndexOutOfBoundsException e) {
                            //System.out.println("there is no comment for " + name);
                        }
                        company.addPerson(new Person(name, position, phone, comment));
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if(scanner != null) {
                scanner.close();
            }
        }

        //read contacts
        try {
            scanner = new Scanner(new BufferedReader(new FileReader("contacts.txt")));
            while(scanner.hasNextLine()) {
                String input = scanner.nextLine();
                //System.out.println(input);
                String[] result = input.split("/_");
                String companyName = result[0];
                LocalDate date = LocalDate.parse(result[1], formatter);
                String content = "";
                for(Company company : companies) {
                    if (company.getName().equals(companyName)) {
                        try {
                            content = result[2];
                        } catch (ArrayIndexOutOfBoundsException e) {
                            //System.out.println("there is no content for " + companyName);
                        }
                        try {
                            String person = result[3];
                            for (Person thisPerson : company.getPersons()) {
                                if (thisPerson.getName().equals(person)) {
                                    company.addContact(new Contact(date, content, thisPerson));
                                }
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            //System.out.println("there is no contact person for " + companyName);
                            company.addContact(new Contact(date, content));
                        }
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if(scanner != null) {
                scanner.close();
            }
        }
    }

    //write all data
    public void saveCompanies() {
        System.out.println("saving data...");
        Collections.sort(companies);
        FileWriter companyFile = null;
        FileWriter personFile = null;
        FileWriter contactFile = null;
        try {
            companyFile = new FileWriter("companies.txt");
            personFile = new FileWriter("persons.txt");
            contactFile = new FileWriter("contacts.txt");
            for(Company company : companies) {

                companyFile.write(company.getName() + "/_" + company.getRegion() + "/_" + company.getAddress() + "/_" + company.getComment() + "\n");

                if (company.getPersons() != null) {
                    Collections.sort(company.getPersons());
                    for (Person person : company.getPersons()) {
                        personFile.write(company.getName() + "/_" + person.getName() + "/_" + person.getPosition() + "/_" + person.getPhone() + "/_" + person.getComment() + "\n");
                    }
                }

                if (company.getContacts() != null) {
                    Collections.sort(company.getContacts());
                    Collections.reverse(company.getContacts());
                    for (Contact contact : company.getContacts()) {
                        if(contact.getPerson() != null) {
                            contactFile.write(company.getName() + "/_" + contact.getDate().format(formatter) + "/_" + contact.getContent() + "/_" + contact.getPerson().getName() + "\n");
                        } else {
                            contactFile.write(company.getName() + "/_" + contact.getDate().format(formatter) + "/_" + contact.getContent() + "\n");
                        }
                    }
                }
            }
            companyFile.flush();
            personFile.flush();
            contactFile.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void printBase() {
        for(Company company : companies) {
            company.printCompany();
        }
    }
}