package com.gluonhq.strange.cloudlink;

import java.util.List;

public class Result {

    private List<Double> qubits;
    private List<Complex> probability;

    public Result() {
    }

    public Result(List<Double> qubits, List<Complex> probability) {
        this.qubits = qubits;
        this.probability = probability;
    }

    public List<Double> getQubits() {
        return qubits;
    }

    public void setQubits(List<Double> qubits) {
        this.qubits = qubits;
    }

    public List<Complex> getProbability() {
        return probability;
    }

    public void setProbability(List<Complex> probability) {
        this.probability = probability;
    }
}
