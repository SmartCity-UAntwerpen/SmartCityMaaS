package be.uantwerpen.model;


import org.springframework.data.jpa.domain.AbstractPersistable;

import java.io.Serializable;

/**
 * Created by Edwin on 28/10/2015.
 */
public class MyAbstractPersistable<Long extends Serializable> extends AbstractPersistable<Long>{
    @Override
    public void setId(Long id) {
        super.setId(id);
    }
}
