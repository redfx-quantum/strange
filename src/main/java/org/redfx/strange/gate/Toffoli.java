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

import static org.redfx.strange.Complex.ONE;
import static org.redfx.strange.Complex.ZERO;

/**
 * <p>Toffoli class.</p>
 *
 * @author johan
 * @version $Id: $Id
 */
public class Toffoli extends ThreeQubitGate {
    
    Complex[][] matrix =  new Complex[][]{
        {ONE,ZERO,ZERO,ZERO,ZERO,ZERO,ZERO,ZERO},
        {ZERO,ONE,ZERO,ZERO,ZERO,ZERO,ZERO,ZERO},
        {ZERO,ZERO,ONE,ZERO,ZERO,ZERO,ZERO,ZERO},
        {ZERO,ZERO,ZERO,ONE,ZERO,ZERO,ZERO,ZERO},
        {ZERO,ZERO,ZERO,ZERO,ONE,ZERO,ZERO,ZERO},
        {ZERO,ZERO,ZERO,ZERO,ZERO,ONE,ZERO,ZERO},
        {ZERO,ZERO,ZERO,ZERO,ZERO,ZERO,ZERO,ONE},
        {ZERO,ZERO,ZERO,ZERO,ZERO,ZERO,ONE,ZERO}
    };

    /**
     * <p>Constructor for Toffoli.</p>
     *
     * @param a a int
     * @param b a int
     * @param c a int
     */
    public Toffoli (int a, int b, int c) {
        super(a,b,c);
    }

    /** {@inheritDoc} */
    @Override
    public Complex[][] getMatrix() {
        return matrix;
    }
 
    /** {@inheritDoc} */
    @Override
    public int getSize() {
        return 3;
    }
    
        
    /** {@inheritDoc} */
    @Override public void setInverse(boolean v) {
        // NOP
    }
    
    /** {@inheritDoc} */
    @Override public String getCaption() {
        return "CCnot";
    }
}
