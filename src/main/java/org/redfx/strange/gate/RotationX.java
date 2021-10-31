package org.redfx.strange.gate;

public class RotationX extends Rotation {

    public RotationX(double theta, int idx) {
        super(theta, Axes.XAxis, idx);
    }

    @Override public String getCaption() {
        return "RotationX " + thetav;
    }

}
