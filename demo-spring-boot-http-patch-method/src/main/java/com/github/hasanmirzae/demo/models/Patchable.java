package com.github.hasanmirzae.demo.models;

import org.apache.commons.beanutils.BeanUtils;

import java.util.Collection;
import java.util.Collections;

public abstract class Patchable {
    protected Collection<String> updatingFields = Collections.EMPTY_SET;

    public Collection<String> getUpdatingFields() {
        return updatingFields;
    }

    public void setUpdatingFields(Collection<String> updatingFields) {
        this.updatingFields = updatingFields;
    }

    public void patchBy(Patchable obj) {
        obj.getUpdatingFields().forEach(field -> updateField(field,obj));
    }

    private void updateField(String name, Patchable obj){
        try {
            BeanUtils.setProperty(this,name,BeanUtils.getProperty(obj,name));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
