package com.quantumtest;

import org.redfx.strange.Complex;
import org.redfx.strange.gate.SingleQubitGate;

public class RotationZ extends SingleQubitGate {

    Complex[][] matrix;
    private final double thetav;

    public RotationZ(double theta, int idx) {
        super(idx);
        this.thetav = theta;
        matrix =  new Complex[][]{{new Complex(0,Math.pow(Math.E,-theta/2)),Complex.ZERO}, {Complex.ZERO,new Complex(0,Math.pow(Math.E,theta/2))}};
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
        return "RotationZ " + thetav;
    }

}
