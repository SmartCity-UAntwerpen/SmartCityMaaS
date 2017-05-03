package be.uantwerpen.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;

/**
 * Created by Frédéric Melaerts on 2/05/2017.
 */
@Entity
public class Segment extends MyAbstractPersistable<Long>{
    @NotBlank(message = "***")
    private String name;


    public Segment() {
    }
    public Segment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
