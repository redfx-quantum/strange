package com.gluonhq.strange.gate;

import com.gluonhq.strange.*;

public class ProbabilitiesGate extends InformalGate {

    public ProbabilitiesGate(int idx) {
        super(idx);
    }

    @Override
    public String getCaption() {
        return "P";
    }

    @Override
    public String getName() {
        return "Probabilities";
    }

}
