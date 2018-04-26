package com.gluonhq.strange.cloudlink;

import java.util.ArrayList;
import java.util.List;

public class Step {

    private List<Gate> gates;

    public void addGate(Gate gate) {
        if (gates == null) {
            gates = new ArrayList<>();
        }
        gates.add(gate);
    }

    public List<Gate> getGates() {
        return gates;
    }

    public void setGates(List<Gate> gates) {
        this.gates = gates;
    }
}
