package org.redfx.strange.gate;

/**
 * <p>RotationY class.</p>
 *
 * @author alain
 * @version $Id: $Id
 */
public class RotationY extends Rotation {

    /**
     * <p>Constructor for RotationY.</p>
     *
     * @param theta a double
     * @param idx a int
     */
    public RotationY(double theta, int idx) {
        super(theta, Axes.YAxis, idx);
    }

    /** {@inheritDoc} */
    @Override public String getCaption() {
        return "RotationY " + thetav;
    }

}
