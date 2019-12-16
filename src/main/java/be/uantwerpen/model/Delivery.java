package be.uantwerpen.model;

import be.uantwerpen.sc.models.MyAbstractPersistable;

import javax.persistence.*;

/**
 * Created by NV 2018
 * The delivery is solely used as a data model which (in serialized form) is send to the backbone.
 * It contains all the information of the delivery that is required to perform the Astart algorithm.
 * PointA is the start point of the delivery, while pointB is the end point of the delivery.
 * The orderID links to the order which is saved in the MySQL database.
 *
 */
@Entity
public class Delivery extends MyAbstractPersistable<Long> {
    private int pointA;
    private int pointB;
    private int mapA;
    private int mapB;
    private int passengers;
    private String type;
    private String date;
    private boolean complete = false;
    private int orderID;

    public Delivery() {

    }

    public Delivery(int pointA, int pointB, int mapA, int mapB, int passengers, String type, String date, boolean complete, int orderID) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.mapA = mapA;
        this.mapB = mapB;
        this.passengers = passengers;
        this.type = type;
        this.date = date;
        this.complete = complete;
        this.orderID = orderID;
    }

    public int getPointA() {
        return pointA;
    }

    public int getPointB() {
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

    public int getOrderID() {
        return orderID;
    }

    public void setPointA(int pointA) {
        this.pointA = pointA;
    }

    public void setPointB(int pointB) {
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

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }
}
