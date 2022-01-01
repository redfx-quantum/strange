/*-
 * #%L
 * Strange
 * %%
 * Copyright (C) 2020 Johan Vos
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Johan Vos nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package org.redfx.strange.gate;


import org.redfx.strange.Complex;
import org.redfx.strange.Gate;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * <p>Oracle class.</p>
 *
 * @author alain
 * @version $Id: $Id
 */
public class Oracle implements Gate {

    private int mainQubit = 0;
    private List<Integer> affected = new LinkedList<>();
    private Complex[][] matrix;
    private String caption = "Oracle";
    private int span = 1;

    /**
     * <p>Constructor for Oracle.</p>
     *
     * @param i a int
     */
    public Oracle (int i) {
        this.mainQubit = i;
    }
    /**
     * <p>Constructor for Oracle.</p>
     *
     * @param matrix an array of {@link org.redfx.strange.Complex} objects
     */
    public Oracle(Complex[][] matrix) {
        this.matrix = matrix;
        sanitizeMatrix();
        span = (int)(Math.log(matrix.length)/Math.log(2));
        for (int i = 0; i < span;i++) {
            setAdditionalQubit(i,i);
        }
    }
 
    /** {@inheritDoc} */
    @Override
    public int getSize() {
        return span;
    }
    
    /**
     * <p>Setter for the field <code>caption</code>.</p>
     *
     * @param c a {@link java.lang.String} object
     */
    public void setCaption(String c) {
        this.caption = c;
    }

    /** {@inheritDoc} */
    @Override
    public void setMainQubitIndex(int idx) {
        this.mainQubit = 0;
    }

    /** {@inheritDoc} */
    @Override
    public int getMainQubitIndex() {
        return mainQubit;
    }

    /** {@inheritDoc} */
    @Override
    public void setAdditionalQubit(int idx, int cnt) {
        this.affected.add(idx);
    }

    /**
     * <p>getQubits.</p>
     *
     * @return a int
     */
    public int getQubits() {
        return span;
    }

    /** {@inheritDoc} */
    @Override
    public List<Integer> getAffectedQubitIndexes() {
        return this.affected;
    }

    /** {@inheritDoc} */
    @Override
    public int getHighestAffectedQubitIndex() {
        return Collections.max(getAffectedQubitIndexes());
    }

    /** {@inheritDoc} */
    @Override
    public String getCaption() {
        return this.caption;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "Oracle";
    }

    /** {@inheritDoc} */
    @Override
    public String getGroup() {
        return "Oracle";
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    public void setInverse(boolean inv) {
        if (inv) {
            this.matrix = Complex.conjugateTranspose(matrix);
        }
    }
}
