package com.gluonhq.strange.cloudlink;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gate {

    private static final Map<String, Complex[][]> matrices = new HashMap<>();
    static {
        matrices.put("Cnot", new Complex[][]{
                {Complex.ONE,Complex.ZERO,Complex.ZERO,Complex.ZERO},
                {Complex.ZERO,Complex.ONE,Complex.ZERO,Complex.ZERO},
                {Complex.ZERO,Complex.ZERO,Complex.ZERO,Complex.ONE},
                {Complex.ZERO,Complex.ZERO,Complex.ONE,Complex.ZERO}
        });
        matrices.put("H", new Complex[][]{{ Complex.HC, Complex.HC }, { Complex.HC, Complex.HCN }});
        matrices.put("I", new Complex[][]{{Complex.ONE,Complex.ZERO}, {Complex.ZERO,Complex.ONE}});
        matrices.put("S", new Complex[][]{
                {Complex.ONE,Complex.ZERO,Complex.ZERO,Complex.ZERO},
                {Complex.ZERO,Complex.ZERO,Complex.ONE,Complex.ZERO},
                {Complex.ZERO,Complex.ONE,Complex.ZERO,Complex.ZERO},
                {Complex.ZERO,Complex.ZERO,Complex.ZERO,Complex.ONE}
        });
        matrices.put("X", new Complex[][]{{Complex.ZERO,Complex.ONE}, {Complex.ONE,Complex.ZERO}});
        matrices.put("Y", new Complex[][]{{Complex.ZERO,Complex.I.mul(-1)}, {Complex.I,Complex.ZERO}});
        matrices.put("Z", new Complex[][]{{Complex.ONE,Complex.ZERO}, {Complex.ZERO,Complex.ONE.mul(-1d)}});
    }

    private String caption;
    private String group;
    private List<Integer> affectedQubitIndex;

    public Gate() {
    }

    public Gate(String caption, String group, List<Integer> affectedQubitIndex) {
        this.caption = caption;
        this.group = group;
        this.affectedQubitIndex = affectedQubitIndex;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<Integer> getAffectedQubitIndex() {
        return affectedQubitIndex;
    }

    public void setAffectedQubitIndex(List<Integer> affectedQubitIndex) {
        this.affectedQubitIndex = affectedQubitIndex;
    }

    public void setMainQubit(int idx) {
        if (affectedQubitIndex != null && affectedQubitIndex.size() >= 1) {
            affectedQubitIndex.set(0, idx);
        }
    }

    public void setAdditionalQubit(int idx, int cnt) {
        if (affectedQubitIndex != null && affectedQubitIndex.size() >= 2) {
            affectedQubitIndex.set(1, idx);
        }
    }

    public Complex[][] getMatrix() {
        return matrices.get(getCaption());
    }
}
