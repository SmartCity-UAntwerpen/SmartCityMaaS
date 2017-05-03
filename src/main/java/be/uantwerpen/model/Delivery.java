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

    public Delivery() {
    }

    public Delivery(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pointA = "";
        this.pointB = "";
        this.passengers = 0;
    }

    public Delivery(String firstName, String lastName, String pointA, String pointB, int passengers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pointA = pointA;
        this.pointB = pointB;
        this.passengers = passengers;
    }

    public Delivery(String firstName, String lastName, String pointA, String pointB) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pointA = pointA;
        this.pointB = pointB;
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
}
