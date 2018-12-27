package com.gluonhq.strange.gate;

import com.gluonhq.strange.*;

import java.util.*;

public class Oracle implements Gate {

    private int mainQubit = 0;
    private List<Integer> affected = new LinkedList<>();
    private Complex[][] matrix;

    public Oracle(Complex[][] matrix) {
        this.matrix = matrix;
        for (int i = 1; i < matrix.length;i++) {
            setAdditionalQubit(i,i);
        }
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

    @Override
    public List<Integer> getAffectedQubitIndex() {
        return this.affected;
    }

    @Override
    public String getCaption() {
        return "Oracle";
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
