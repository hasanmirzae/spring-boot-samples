package com.github.hasanmirzae.demo.models;

import org.apache.commons.beanutils.BeanUtils;
import java.util.Collection;
import java.util.Collections;

/**
 * Abstract class is supposed to be extended by model classes (entities/DTOs).
 * Once a class extends this class it becomes Patchable.
 * It means that the current state of the object could be updated with the one of another patchable instance.
 * Only those fields of the destination object would be updated that are mentioned in updatingFields of the source object.
 * Example: distObj.patchBy(srcObj)
 * @author Hassan Mirzaee
 */
public abstract class Patchable {
    protected Collection<String> updatingFields = Collections.EMPTY_SET;

    /**
     * Get name of the fields which should be updated.
     */
    public Collection<String> getUpdatingFields() {
        return updatingFields;
    }

    /**
     * Set field names which should be updated.
     */
    public void setUpdatingFields(Collection<String> updatingFields) {
        this.updatingFields = updatingFields;
    }

    /**
     * Update partially current object with source object.
     * Only those fields of the destination object would be updated that are mentioned in updatingFields of the source object.
     * @param obj source object with which current object will be updated
     * @throws IllegalStateException if updatingFields are empty
     */
    public void patchBy(Patchable obj) {
        if (obj.getUpdatingFields() == null || obj.getUpdatingFields().isEmpty())
            throw new IllegalStateException("updatingFields of the source object cannot be empty");
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
