package com.quantumtest;

import org.redfx.strange.Complex;
import org.redfx.strange.gate.SingleQubitGate;

public class RotationX extends SingleQubitGate {

    Complex[][] matrix;
    private final double thetav;

    public RotationX(double theta, int idx) {
        super(idx);
        this.thetav = theta;
        matrix =  new Complex[][]{{new Complex(Math.cos(theta/2),0), new Complex(0,-Math.sin(theta/2))}, {new Complex(0,-Math.sin(theta/2)), new Complex(Math.cos(theta/2),0)}};
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
