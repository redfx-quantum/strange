package org.redfx.strange.gate;

import org.redfx.strange.Complex;

public class Chadamard extends TwoQubitGate {

    Complex[][] matrix =  new Complex[][]{
            {Complex.ONE,Complex.ZERO,Complex.ZERO,Complex.ZERO},
            {Complex.ZERO,Complex.ONE,Complex.ZERO,Complex.ZERO},
            {Complex.ZERO,Complex.ZERO,Complex.HC,Complex.HC},
            {Complex.ZERO,Complex.ZERO,Complex.HC,Complex.HCN}
    };

    /**
     * <p>Constructor for Chadamard.</p>
     */
    public Chadamard() {
    }

    /**
     * <p>Constructor for Chadamard.</p>
     *
     * @param a a int
     * @param b a int
     */
    public Chadamard(int a, int b) {
        super(a,b);
    }

    /** {@inheritDoc} */
    @Override
    public Complex[][] getMatrix() {
        return matrix;
    }

    /** {@inheritDoc} */
    @Override public String getCaption() {
        return "Chadamard";
    }
}
