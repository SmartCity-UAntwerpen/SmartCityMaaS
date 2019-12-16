package be.uantwerpen.model;

import be.uantwerpen.sc.models.MyAbstractPersistable;

import java.io.Serializable;

public class Order extends MyAbstractPersistable<Long> {

    public String userName;
    public String firstName;
    public String lastName;

}
