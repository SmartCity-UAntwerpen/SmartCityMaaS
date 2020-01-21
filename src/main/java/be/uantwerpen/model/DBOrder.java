package be.uantwerpen.model;

import org.thymeleaf.util.DateUtils;

import javax.persistence.Entity;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class DBOrder extends MyAbstractPersistable<Long> {
    public String userName;
    public String firstName;
    public String lastName;
    public String description;
    public Date date;
    public String type;


    public DBOrder() {
        this.date = new Date();
    }

    public String getDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String S = sdf.format(date);
        return  S;
    }
}
