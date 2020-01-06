package be.uantwerpen.model;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class DBOrder extends MyAbstractPersistable<Long> {
    public String userName;
    public String firstName;
    public String lastName;
    public String description;
    public Date date;

    public DBOrder() {
        this.date = new Date();
    }
}
