package org.redfx.strange.gate;

import org.redfx.strange.Complex;
import org.redfx.strange.gate.SingleQubitGate;

public class RotationX extends Rotation {

    Complex[][] matrix;

    public RotationX(double theta, int idx) {
        super(theta, Axes.XAxis, idx);
    }

    @Override
    public Complex[][] getMatrix() {
        return matrix;
    }

    @Override
    public void setInverse(boolean v) {
        super.setInverse(v);
        matrix = Complex.conjugateTranspose(matrix);
    }

    @Override public String getCaption() {
        return "RotationX " + thetav;
    }

}
