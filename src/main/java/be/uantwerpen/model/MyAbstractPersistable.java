package be.uantwerpen.model;


import org.springframework.data.jpa.domain.AbstractPersistable;

import java.io.Serializable;

/**
 * NV 2018
 */
public class MyAbstractPersistable<Long extends Serializable> extends AbstractPersistable<Long>{
    @Override
    public void setId(Long id) {
        super.setId(id);
    }
}
