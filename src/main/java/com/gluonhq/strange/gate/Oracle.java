package com.gluonhq.strange.gate;


import com.gluonhq.strange.Complex;
import com.gluonhq.strange.Gate;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class Oracle implements Gate {

    private int mainQubit = 0;
    private List<Integer> affected = new LinkedList<>();
    private Complex[][] matrix;
    private String caption = "Oracle";
    private int span = 1;

    public Oracle (int i) {
        this.mainQubit = i;
    }

    /**
     * Create an Oracle based on the provided matrix.
     * Elements in this matrix may be <code>null</code> in which case they will be
     * replaced by <code>Complex.ZERO</code>
     * @param matrix the matrix describing this Oracle. If elements in this matrix are <code>null</code>, they
     *               will be replaced with <code>Complex.ZERO</code>.
     */
    public Oracle(Complex[][] matrix) {
        this.matrix = matrix;
        sanitizeMatrix();
        span = (int)(Math.log(matrix.length)/Math.log(2));
        for (int i = 0; i < span;i++) {
            setAdditionalQubit(i,i);
        }
    }

    public void setCaption(String c) {
        this.caption = c;
    }

    @Override
    public void setMainQubitIndex(int idx) {
        this.mainQubit = 0;
    }

    @Override
    public int getMainQubitIndex() {
        return mainQubit;
    }

    @Override
    public void setAdditionalQubit(int idx, int cnt) {
        this.affected.add(idx);
    }

    public int getQubits() {
        return span;
    }

    @Override
    public List<Integer> getAffectedQubitIndexes() {
        return this.affected;
    }

    @Override
    public int getHighestAffectedQubitIndex() {
        return Collections.max(getAffectedQubitIndexes());
    }

    @Override
    public String getCaption() {
        return this.caption;
    }

    @Override
    public String getName() {
        return "Oracle";
    }

    @Override
    public String getGroup() {
        return "Oracle";
    }

    @Override
    public Complex[][] getMatrix() {
        return matrix;
    }

    // replace null with Complex.ZERO
    private void sanitizeMatrix() {
        int rows = matrix.length;
        for (int i = 0;i < rows; i++) {
            Complex[] row = matrix[i];
            for (int j = 0; j < row.length; j++) {
                if (matrix[i][j] == null) {
                    matrix[i][j] = Complex.ZERO;
                }
            }
        }
    }
}
