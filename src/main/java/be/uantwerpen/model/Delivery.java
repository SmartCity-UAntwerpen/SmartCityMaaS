package be.uantwerpen.model;

import be.uantwerpen.sc.models.MyAbstractPersistable;

import javax.persistence.*;

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
     private String username = "";
    private String firstname = "";
    private String lastname = "";
    private String pointA = "";
    private String pointB = "";
    private int mapA =0;
    private int mapB = 0;
    private int passengers = 0;
    private String type = "";
    private String date = "";
    private boolean complete = false;
    private int backboneID = 0;

    public Delivery() {

    }

    public Delivery(String userName, String firstName, String lastName, String pointA, String pointB, int mapA, int mapB, int passengers, String type, String date, boolean complete, int backboneId) {
        this.username = userName;
        this.firstname = firstName;
        this.lastname = lastName;
        this.pointA = pointA;
        this.pointB = pointB;
        this.mapA = mapA;
        this.mapB = mapB;
        this.passengers = passengers;
        this.type = type;
        this.date = date;
        this.complete = complete;
        this.backboneID = backboneId;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPointA() {
        return pointA;
    }

    public String getPointB() {
        return pointB;
    }

    public int getMapA() {
        return mapA;
    }

    public int getMapB() {
        return mapB;
    }

    public int getPassengers() {
        return passengers;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public boolean isComplete() {
        return complete;
    }

    public int getBackboneID() {
        return backboneID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setPointA(String pointA) {
        this.pointA = pointA;
    }

    public void setPointB(String pointB) {
        this.pointB = pointB;
    }

    public void setMapA(int mapA) {
        this.mapA = mapA;
    }

    public void setMapB(int mapB) {
        this.mapB = mapB;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void setBackboneID(int backboneID) {
        this.backboneID = backboneID;
    }
}
