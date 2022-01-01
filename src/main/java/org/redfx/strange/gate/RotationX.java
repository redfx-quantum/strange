package org.redfx.strange.gate;

/**
 * <p>RotationX class.</p>
 *
 * @author alain
 * @version $Id: $Id
 */
public class RotationX extends Rotation {

    /**
     * <p>Constructor for RotationX.</p>
     *
     * @param theta a double
     * @param idx a int
     */
    public RotationX(double theta, int idx) {
        super(theta, Axes.XAxis, idx);
    }

    /** {@inheritDoc} */
    @Override public String getCaption() {
        return "RotationX " + thetav;
    }

}
