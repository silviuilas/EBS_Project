package com.homework;

import java.util.List;
import java.util.Map;

public class Field {
    String name;
    List<Object> vals;
    Double rangeInf;
    Double rangeSup;
    double pond;
    Map<String, Double> op_pond;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getVals() {
        return vals;
    }

    public void setVals(List<Object> vals) {
        this.vals = vals;
    }

    public double getPond() {
        return pond;
    }

    public void setPond(double pond) {
        this.pond = pond;
    }

    public Map<String, Double> getOp_pond() {
        return op_pond;
    }

    public void setOpPond(Map<String, Double> op_pond) {
        this.op_pond = op_pond;
    }

    public Double getRangeInf() {
        return rangeInf;
    }

    public void setRangeInf(Double rangeInf) {
        this.rangeInf = rangeInf;
    }

    public Double getRangeSup() {
        return rangeSup;
    }

    public void setRangeSup(Double rangeSup) {
        this.rangeSup = rangeSup;
    }
}
