package com.nttdata.beca.entity.abstractEntity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

public class EntityListener {

    public EntityListener(){}

    @PrePersist
    public void setCreatedAt(final AbstractEntity entity) {
        final Date date = new Date();
        entity.setCreatedAt(date);
        entity.setModifiedAt(date);
    }

    @PreUpdate
    public void setModifiedAt(AbstractEntity entity) {
        final Date date = new Date();
        entity.setModifiedAt(new Date());
    }

}
