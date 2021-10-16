package org.redfx.strange.gate;

import org.redfx.strange.Complex;
import org.redfx.strange.gate.SingleQubitGate;

public class RotationZ extends Rotation {

    Complex[][] matrix;

    public RotationZ(double theta, int idx) {
        super(theta, Axes.ZAxis, idx);
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
        return "RotationZ " + this.thetav;
    }

}
