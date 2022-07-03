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

/**
 * <p>ProbabilitiesGate class.</p>
 *
 * @author alain
 * @version $Id: $Id
 */
public class ProbabilitiesGate extends InformalGate {

    private Complex[] probabilities;

    /**
     * <p>Constructor for ProbabilitiesGate.</p>
     *
     * @param idx a int
     */
    public ProbabilitiesGate(int idx) {
        super(idx);
    }

    /** {@inheritDoc} */
    @Override
    public String getCaption() {
        return "P";
    }
 
    /** {@inheritDoc} */
    @Override
    public int getSize() {
        return -1;
    }
    
    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "Probabilities";
    }
    
    /** {@inheritDoc} */
    @Override public void setInverse(boolean v) {
        // NOP
    }

    /**
     * Set the probability vector for the system at the point where this gate is located.
     * @param vector
     */
    public void setProbabilites(Complex[] vector) {
        this.probabilities = vector;
    }

    /**
     * Return the probability vector at the location of this gate.
     * @return an array containing probabilities (as complex numbers).
     */
    public Complex[] getProbabilities() {
        return this.probabilities;
    }
}
