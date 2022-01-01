package org.redfx.strange.gate;

/**
 * <p>RotationZ class.</p>
 *
 * @author alain
 * @version $Id: $Id
 */
public class RotationZ extends Rotation {

    /**
     * <p>Constructor for RotationZ.</p>
     *
     * @param theta a double
     * @param idx a int
     */
    public RotationZ(double theta, int idx) {
        super(theta, Axes.ZAxis, idx);
    }

    /** {@inheritDoc} */
    @Override public String getCaption() {
        return "RotationZ " + this.thetav;
    }

}
