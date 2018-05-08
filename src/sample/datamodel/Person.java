package sample.datamodel;

/**
 * Created by КазанцевМЮ on 28.02.18.
 */
public class Person implements Comparable<Person>{
    private String name;
    private String position;
    private String phone;
    private String comment;

    public Person(String name) {
        this.name = name;
        this.position = "";
    }

//    public Person(String name, String position) {
//        this.name = name;
//        this.position = position;
//    }

    public Person(String name, String position, String phone, String comment) {
        this.name = name;
        this.position = position;
        this.phone = phone;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getPhone() {
        return phone;
    }

    public String getComment() {
        return comment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }

        if((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }

        String objName = ((Person) obj).getName();
        return this.name.equals(objName);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode() + 31;
    }

    @Override
    public int compareTo(Person person) {
        int comparison = this.name.compareTo(person.getName());
        return comparison;
    }

    @Override
    public String toString() {
        return name;
    }
}