package be.uantwerpen.model;

import be.uantwerpen.sc.models.MyAbstractPersistable;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class Order extends MyAbstractPersistable<Long> {

    public String userName;
    public String firstName;
    public String lastName;

    private int pointA;
    private int pointB;
    private int mapA;
    private int mapB;
    private int passengers;
    private String type;
    private String date;
    private boolean complete = false;
    private Long orderID;

    public Order() {
        super();
    }

    public Order(DBOrder order, DBDelivery delivery) throws Exception {
        if (!order.getId().equals(delivery.getOrderID())) { throw new Exception("DBOrder ID and DBDelivery orderID are not equal"); }
        userName = order.userName;
        firstName = order.firstName;
        lastName = order.lastName;

        pointA = delivery.getPointA();
        pointB = delivery.getPointB();
        mapA = delivery.getMapA();
        mapB = delivery.getMapB();
        passengers = delivery.getPassengers();
        type = delivery.getType();
        date = delivery.getDate();
        complete = delivery.isComplete();
        orderID = delivery.getOrderID();
    }
}
