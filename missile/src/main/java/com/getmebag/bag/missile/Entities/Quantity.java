package com.getmebag.bag.missile.Entities;

import com.googlecode.objectify.annotation.Entity;

/**
 * Created by karthiktangirala on 12/11/14.
 */
@Entity
public class Quantity {

    float value;
    QuantityUnits units;

    public Quantity() {
    }

    public Quantity(float value, QuantityUnits units) {
        this.value = value;
        this.units = units;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public QuantityUnits getUnits() {
        return units;
    }

    public void setUnits(QuantityUnits units) {
        this.units = units;
    }
}
