package org.redfx.strange.gate;

import org.redfx.strange.Complex;
import org.redfx.strange.gate.SingleQubitGate;

public class RotationZ extends Rotation {

    public RotationZ(double theta, int idx) {
        super(theta, Axes.ZAxis, idx);
    }

    @Override public String getCaption() {
        return "RotationZ " + this.thetav;
    }

}
