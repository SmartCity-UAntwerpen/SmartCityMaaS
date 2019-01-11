package be.uantwerpen.model;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;

/**
 * Created by NV 2018
 * The delivery is linked to a specific user.
 * It contains all the information of the delivery that is required to perform the Astart algorithm.
 * PointA is the start point of the delivery, while pointB is the end point of the delivery.
 * Its ID is retrieved from the MongoDB database.
 *
 */
@Entity
public class Delivery extends MyAbstractPersistable<Long> {

    private static final Logger logger = LogManager.getLogger(Delivery.class);

    private String userName;
    private String firstName;
    private String lastName;
    private String pointA;
    private String pointB;
    private int mapA;
    private int mapB;
    private int passengers;
    private String type;
    private String date;
    private String idDelivery;
    private boolean complete = false;
    private int backboneId;

    public Delivery() {
    }

    public Delivery(String userName, String firstName, String lastName) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pointA = "";
        this.pointB = "";
        this.mapA = -1;
        this.mapB = -1;
        this.passengers = 0;
        this.date = "0000-01-01 01:00:00.000 000";
        this.type = "HumanTransport";
        this.idDelivery = "0";
        this.backboneId = 0;
    }
    public Delivery(String idDelivery, String type, String firstName, String lastName, String pointA, String pointB, int passengers, String date) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pointA = pointA;
        this.pointB = pointB;
        this.mapA = -1;
        this.mapB = -1;
        this.passengers = passengers;
        this.date = date;
        this.type = type;
        this.idDelivery = idDelivery;
        this.backboneId = 0;
    }
    public Delivery(String userName, String firstName, String lastName, String pointA, String pointB, int passengers) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pointA = pointA;
        this.pointB = pointB;
        this.mapA = -1;
        this.mapB = -1;
        this.passengers = passengers;
        this.date = "0000-01-01 01:00:00.000 000";
        this.type = "HumanTransport";
        this.idDelivery = "0";
        this.backboneId = 0;
    }
    public Delivery(String firstName, String lastName, String pointA, String pointB, int passengers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pointA = pointA;
        this.pointB = pointB;
        this.mapA = -1;
        this.mapB = -1;
        this.passengers = passengers;
        this.date = "0000-01-01 01:00:00.000 000";
        this.type = "HumanTransport";
        this.idDelivery = "0";
        this.backboneId = 0;
    }

    public Delivery(String firstName, String lastName, String pointA, String pointB) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pointA = pointA;
        this.pointB = pointB;
        this.mapA = -1;
        this.mapB = -1;
        this.passengers = 0;
        this.date = "0000-01-01 01:00:00.000 000";
        this.type = "HumanTransport";
        this.idDelivery = "0";
        this.backboneId = 0;
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
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public boolean isComplete() {
        return complete;
    }
    public void setComplete(boolean complete) {
        this.complete = complete;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public int getBackboneId() {
        return backboneId;
    }

    public void setBackboneId(int backboneId) {
        this.backboneId = backboneId;
    }

    public int getMapA() {
        return mapA;
    }

    public void setMapA(int mapA) {
        this.mapA = mapA;
    }

    public int getMapB() {
        return mapB;
    }

    public void setMapB(int mapB) {
        this.mapB = mapB;
    }

    /**
     * Print all the information of the delivery.
     */
    public void print() {
        logger.info("Delivery: idDelivery " + idDelivery + ", username " + userName + ", firstName " + firstName +
                ", lastName " + lastName + ", pointA " + pointA + ", pointB " + pointB + ", mapA " + mapA + ", mapB " + mapB +
                ", passengers " + passengers + ", type " + type + ", date " + date + ", backboneId " + backboneId);
    }
}
