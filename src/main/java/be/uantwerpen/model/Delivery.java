package be.uantwerpen.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;

/**
 * Created by Frédéric Melaerts on 2/05/2017.
 */
@Entity
public class Delivery extends MyAbstractPersistable<Long> {
    @NotBlank(message = "***")
    private String firstName;
    @NotBlank(message = "***")
    private String lastName;
    private String pointA;
    private String pointB;
    private int passengers;
    private String type;
    private String date;
    private String idDelivery;

    public Delivery() {
    }

    public Delivery(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pointA = "";
        this.pointB = "";
        this.passengers = 0;
        this.date = "0000-01-01 01:00:00.000 000";
        this.type = "HumanTransport";
        this.idDelivery = "0";
    }
    public Delivery(String idDelivery, String type, String firstName, String lastName, String pointA, String pointB, int passengers, String date) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pointA = pointA;
        this.pointB = pointB;
        this.passengers = passengers;
        this.date = date;
        this.type = type;
        this.idDelivery = idDelivery;
    }
    public Delivery(String firstName, String lastName, String pointA, String pointB, int passengers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pointA = pointA;
        this.pointB = pointB;
        this.passengers = passengers;
        this.date = "0000-01-01 01:00:00.000 000";
        this.type = "HumanTransport";
        this.idDelivery = "0";
    }

    public Delivery(String firstName, String lastName, String pointA, String pointB) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pointA = pointA;
        this.pointB = pointB;
        this.passengers = 0;
        this.date = "0000-01-01 01:00:00.000 000";
        this.type = "HumanTransport";
        this.idDelivery = "0";
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPointA() {
        return pointA;
    }

    public void setPointA(String pointA) {
        this.pointA = pointA;
    }

    public String getPointB() {
        return pointB;
    }

    public void setPointB(String pointB) {
        this.pointB = pointB;
    }
    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getIdDelivery() {
        return idDelivery;
    }

    public void setIdDelivery(String idDelivery) {
        this.idDelivery = idDelivery;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void print() {
        System.out.print(" idDelivery: "+ idDelivery+ ", ");
        System.out.print(" firstName: "+ firstName+ ", ");
        System.out.print(" lastName: "+ lastName+ ", ");
        System.out.print(" pointA: "+ pointA+ ", ");
        System.out.print(" pointB: "+ pointB+ ", ");
        System.out.print(" passengers: "+ passengers+ ", ");
        System.out.print(" type: "+ type+ ", ");
        System.out.println(" date: "+ date);
    }

}
