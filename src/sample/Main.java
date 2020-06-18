package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    //public static CompanyList companyList = new CompanyList();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        primaryStage.setTitle("cb");
        primaryStage.setScene(new Scene(root, 1000, 750));
        primaryStage.show();

        //companyList.readCompanies();


        //LocalDate date = LocalDate.parse("30-01-2012", companyList.getFormatter());
        //companyList.getCompanies().get("ОКБМ").addContact(new Contact(date, "ghghgh"));
        //companyList.getCompanies().get("ОКБМ").getContacts().get(0).setPerson(companyList.getCompanies().get("ОКБМ").getPersons().get("Александрин"));


        //companyList.printBase();
        //companyList.saveCompanies();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
