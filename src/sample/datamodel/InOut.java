package sample.datamodel;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by КазанцевМЮ on 28.02.18.
 */
public class InOut {
    private static InOut instance = new InOut();
    private List<Company> companies = new ArrayList<>();

    private DateTimeFormatter formatter;

    public static InOut getInstance() {
        return instance;
    }

    private InOut() {
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public  List<Company> getCompanies() {
        return companies;
    }

    public void loadCompanies() {
        companies.add(new Company("ОКБМ"));
        companies.add(new Company("Гидромаш"));
        companies.add(new Company("Сокол"));
        System.out.println(companies.size());
        //Collections.sort(companies);
    }
}