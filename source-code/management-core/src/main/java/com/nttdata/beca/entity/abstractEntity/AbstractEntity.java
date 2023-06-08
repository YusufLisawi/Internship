package com.nttdata.beca.entity.abstractEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@MappedSuperclass
@EntityListeners(EntityListener.class)
public abstract class AbstractEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "created_at",nullable = false,updatable = false)
    @JsonIgnore
    protected Date createdAt;

    @Column(name = "modified_at",nullable = false)
    @JsonIgnore
    protected Date modifiedAt;

    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(final Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }
    public void setModifiedAt(final Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}