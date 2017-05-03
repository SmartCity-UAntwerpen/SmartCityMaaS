package be.uantwerpen.model;

import javax.persistence.Entity;


/**
 * Created by Frédéric Melaerts on 26/04/2017.
 */
@Entity
public class Permission extends MyAbstractPersistable<Long> {
    private String name;
    public Permission() {
    }
    public Permission(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
