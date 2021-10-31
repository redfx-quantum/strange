package org.redfx.strange.gate;

import org.redfx.strange.Complex;
import org.redfx.strange.gate.SingleQubitGate;

public class RotationY extends Rotation {

    public RotationY(double theta, int idx) {
        super(theta, Axes.YAxis, idx);
    }

    @Override public String getCaption() {
        return "RotationY " + thetav;
    }

}
