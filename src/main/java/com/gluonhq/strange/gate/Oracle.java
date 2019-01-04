package com.gluonhq.strange.gate;


import com.gluonhq.strange.Complex;
import com.gluonhq.strange.Gate;

import java.util.LinkedList;
import java.util.List;


public class Oracle implements Gate {

    private int mainQubit = 0;
    private List<Integer> affected = new LinkedList<>();
    private Complex[][] matrix;
    private String caption = "Oracle";
    private int span = 1;

    public Oracle (int i) {
        this.mainQubit = i;
    }

    public Oracle(Complex[][] matrix) {
        this.matrix = matrix;
        for (int i = 1; i < matrix.length;i++) {
            setAdditionalQubit(i,i);
        }
        span = (int)(Math.log(matrix.length)/Math.log(2));
    }

    public void setCaption(String c) {
        this.caption = c;
    }

    @Override
    public void setMainQubit(int idx) {
        this.mainQubit = 0;
    }

    @Override
    public int getMainQubitIndex() {
        return mainQubit;
    }

    @Override
    public void setAdditionalQubit(int idx, int cnt) {
        this.affected.add(idx);
    }

    public int getQubits() {
        return span;
    }

    @Override
    public List<Integer> getAffectedQubitIndex() {
        return this.affected;
    }

    @Override
    public String getCaption() {
        return this.caption;
    }

    @Override
    public String getName() {
        return "Oracle";
    }

    @Override
    public String getGroup() {
        return "Oracle";
    }

    @Override
    public Complex[][] getMatrix() {
        return matrix;
    }
}
