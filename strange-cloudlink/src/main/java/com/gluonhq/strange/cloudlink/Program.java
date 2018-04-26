package com.gluonhq.strange.cloudlink;

import java.util.List;

public class Program {

    private int numberQubits;
    private List<Step> steps;

    public int getNumberQubits() {
        return numberQubits;
    }

    public void setNumberQubits(int numberQubits) {
        this.numberQubits = numberQubits;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}
