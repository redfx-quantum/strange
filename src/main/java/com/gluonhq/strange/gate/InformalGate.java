package com.gluonhq.strange.gate;

import com.gluonhq.strange.*;

import java.util.*;

public abstract class InformalGate implements Gate {

    private List<Integer> affected = new LinkedList<>();
    private int mainIndex;

    public InformalGate() {
        this(0);
    }

    public InformalGate(int idx) {
        this.mainIndex = idx;
        affected.add(idx);
    }

    @Override
    public void setMainQubitIndex(int idx) {

    }

    @Override
    public int getMainQubitIndex() {
        return 0;
    }

    @Override
    public void setAdditionalQubit(int idx, int cnt) {

    }

    @Override
    public int getHighestAffectedQubitIndex() {
        return Collections.max(affected);
    }

    @Override
    public List<Integer> getAffectedQubitIndexes() {
        return affected;
    }


    @Override
    public String getGroup() {
        return "informal";
    }

    @Override
    public Complex[][] getMatrix() {
        return new Complex[0][];
    }

}
